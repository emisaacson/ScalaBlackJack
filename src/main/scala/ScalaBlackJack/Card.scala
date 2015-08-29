package ScalaBlackJack

/**
 * @author Administrator
 */
class Card(val Value: List[Int], val Symbol: String, val Suit: Suit) {
  if (Value.length > 2) {
    throw new Exception("Card cannot have more than 2 values.")
  }
  
  override def toString() = Symbol + Suit.Symbol
}
object Card {
  def createCardFromRepr(strRepr: String) = {
    val suitRepr = strRepr takeRight 1
    val suit = suitRepr match {
      case Suit.Clubs.Symbol => Suit.Clubs
      case Suit.Hearts.Symbol => Suit.Hearts
      case Suit.Diamonds.Symbol => Suit.Diamonds
      case Suit.Spades.Symbol => Suit.Spades
    }
    
    val valRepr = strRepr.substring(0, strRepr.length() - 1)
    val vals = valRepr match {
      case "A" => List(1,11)
      case "J" | "Q" | "K" => List(10)
      case numberCard => List(numberCard.toInt)
    }
    
    new Card(vals, valRepr, suit)
  }
}