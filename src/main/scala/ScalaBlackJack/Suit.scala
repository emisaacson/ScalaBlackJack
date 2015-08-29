package ScalaBlackJack

/**
 * @author Administrator
 */
sealed class Suit(val Symbol: String)
object Suit {
  val Hearts = new Suit("♥")
  val Spades = new Suit("♠")
  val Clubs = new Suit("♣")
  val Diamonds = new Suit("♦")
}

