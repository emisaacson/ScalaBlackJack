package ScalaBlackJack

import scala.collection.mutable.Stack
import scala.collection.mutable.MutableList
import scala.collection.mutable.HashMap

/**
 * A class to determine the optimal score in a hand of blackjack
 *
 * @param gameID A seed to pass to the pseudorandom number generator. Identical gameIDs will always produce a deck in the same order.
 */
class BlackJackSolver(gameID: Long = (new scala.util.Random).nextLong(), customDeck: List[Card] = null) {
  
  val gameDeck = if (customDeck != null) customDeck else (new Deck(gameID)).Shuffle.Cards
  val numberOfCards = gameDeck.length
  val memo = new HashMap[Int, Double]
  val parent = new HashMap[Int, HashMap[Int, Move]]
  
  def solve(
		startOfHand: Int = numberOfCards,
    dealerCards: MutableList[Card] = new MutableList,
    playerCards: MutableList[Card] = new MutableList,
    playerStrategy: Option[Move] = None
  ) : Double = {
    
    val restOfDeck: Stack[Card] =
      Stack(gameDeck.reverse
                    .drop(numberOfCards - startOfHand + playerCards.length + dealerCards.length) : _*)
    
    // Deal player and dealer each 2 cards, if necessary
    if (playerCards.length == 0) {
      0 to 1 foreach { i => {
          try {
            playerCards += restOfDeck.pop()
          }
          catch {
            // No cards left, game over
            case nse: NoSuchElementException => return 0
          }
          
          try {
            dealerCards += restOfDeck.pop()
          }
          catch {
            // No cards left, game over
            case nse: NoSuchElementException => return 0
          }
        }
      }
    
      // Did dealer blackjack?
      val initialDealerHand = BlackJackSolver.getHandValue(dealerCards)
      if (initialDealerHand.last == 21) {
        return -1 + solve(restOfDeck.length)
      }
      
      // Did player blackjack?
      val initialPlayerHand = BlackJackSolver.getHandValue(playerCards)
      if (initialPlayerHand.last == 21) {
        return 1.5 + solve(restOfDeck.length)
      }
    }
    
    // Did player bust?
    val currentPlayerHand = BlackJackSolver.getHandValue(playerCards)
    if (currentPlayerHand.length == 0) {
      return -1 + solve(restOfDeck.length)
    }
    
    // Player plays
    if (playerStrategy == None) {
      if (memo.contains(startOfHand) && restOfDeck.length == startOfHand - 4) {
        return memo(startOfHand)
      }
      
      val valueOfStay = solve(startOfHand, MutableList(dealerCards:_*), MutableList(playerCards:_*), Some(Move.Stay))
      val valueOfHit = solve(startOfHand, MutableList(dealerCards:_*), MutableList(playerCards:_*), Some(Move.Hit))
      
      if (!parent.contains(startOfHand)) {
        parent(startOfHand) = new HashMap[Int, Move]
      }
      
      if (valueOfStay >= valueOfHit) {
        parent(startOfHand)(restOfDeck.length) = Move.Stay
      }
      else {
        parent(startOfHand)(restOfDeck.length) = Move.Hit
      }
      
      if (restOfDeck.length == startOfHand - 4) {
        memo(startOfHand) = Math.max(valueOfHit, valueOfStay)
      }
      
      return Math.max(valueOfHit, valueOfStay)
    }
    
    playerStrategy match {
      case Some(Move.Hit) => {
        try {
          playerCards += restOfDeck.pop
          return solve(startOfHand, MutableList(dealerCards:_*), MutableList(playerCards:_*), None)
        }
        catch {
          // No cards left, game over
          case nse: NoSuchElementException => return 0
        }
      }
      case Some(Move.Stay) => {}
      // These are TODO
      case Some(Move.DoubleDown) => {}
      case Some(Move.Split) => {}
      case Some(_) => throw new Exception("Hmmmm...")
      case None => throw new Exception("Should be a strategy at this point.")
    }
    
    // Dealer plays
    var dealerHand = BlackJackSolver.getHandValue(dealerCards)
    while ((dealerHand.length == 1 && dealerHand.head < 17) ||
           (dealerHand.length > 1 && dealerHand.last < 18)) {
      try {
        dealerCards += restOfDeck.pop
      }
      catch {
        // No cards left, game over
        case nse: NoSuchElementException => return 0
      }
      dealerHand = BlackJackSolver.getHandValue(dealerCards)
    }
    
    // Did dealer bust?
    if (dealerHand.length == 0) {
      return 1 + solve(restOfDeck.length)
    }
    
    // Who wins?
    val playerHand = BlackJackSolver.getHandValue(playerCards)
    dealerHand = BlackJackSolver.getHandValue(dealerCards)
    
    //dealer wins
    if (playerHand.last < dealerHand.last) {
      return -1 + solve(restOfDeck.length)
    }
    //player wins
    else if (dealerHand.last < playerHand.last) {
      return 1 + solve(restOfDeck.length)
    }
    //tie
    else {
      return solve(restOfDeck.length)
    }
  }
}

object BlackJackSolver {

  /**
   * Generates all legal values for a current hand.
   * 
   * If no the hand has no legal values, an empty list
   * is returned
   * @param hand A sequence of Card classes
   * @return A List of legal hand values
   */
  def getHandValue(hand: Seq[Card]) : List[Int] = {
    
    /** A helper function to add a single card's value(s) to a list of possible hand values */
    def _addCardValueToTotal(accumulatedValues: List[Int], card: Card) = {
      if (card.Value.length == 1) {
        accumulatedValues map { c => c + card.Value(0) }
      }
      else if (card.Value.length == 2) {
        accumulatedValues.foldLeft[List[Int]](List[Int]()) {
          (innerAccumulatedValues: List[Int], currentValue: Int) =>
            innerAccumulatedValues ++ List(currentValue + card.Value(0), currentValue + card.Value(1))
        }
      }
      else {
        throw new Exception("Card cannot have more than 2 values.")
      }
    }
    
    // After accumulating the value of all cards, remove illegal
    // totals (anything over 21) and return a sorted unique list
    // of the remaining
    hand.foldLeft [List[Int]](List[Int](0)) {
      _addCardValueToTotal
    }.filter(21 >=).sorted.distinct
  }
}