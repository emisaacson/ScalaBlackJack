package ScalaBlackJack

/**
 * @author Administrator
 */
class Deck(val Seed : Long = (new scala.util.Random).nextLong()) {
  private val _Cards : Array[Card] = Array.tabulate(52)(n => {
    val CardNumber = (n % 13) + 1
    val SuitNumber = n / 13
    
    val CardValue = CardNumber match {
      case 1 => List[Int](1, 11) // Aces
      case faceCard if faceCard > 10 => List[Int](10)
      case numberCard => List[Int](numberCard)
    }
      
    val CardSymbol = CardNumber match {
      case 11 => "J"
      case 12 => "Q"
      case 13 => "K"
      case 1 => "A"
      case numberCard => numberCard.toString()
    }

    val CardSuit : Suit = SuitNumber match {
      case 0 => Suit.Hearts
      case 1 => Suit.Clubs
      case 2 => Suit.Spades
      case 3 => Suit.Diamonds
    }

    new Card(CardValue, CardSymbol, CardSuit)
  })
  
  /**
   * The cards of the current shuffle
   */
  def Cards = _Cards.toList
  
  
  /**
   * Shuffles the deck via Fisher-Yates shuffle
   * 
   * @return The Deck
   */
  def Shuffle : Deck = {
    // (Knuth-) Fisher-Yates shuffle.
    // Could also use built-in scala.util.Random.shuffle method
    Cards.length - 1 to 1 by -1 foreach { i => {
        val rand = (new scala.util.Random(Seed)).nextInt(i+1)
         
        val tmp = _Cards(i)
        _Cards(i) = _Cards(rand)
        _Cards(rand) = tmp
      }
    }
    
    this
  }
}