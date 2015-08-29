package ScalaBlackJack

/**
 * @author Administrator
 */
case class GameStateFromJson(
  dealerCards:Seq[String],
  playerCards:Seq[String],
  startOfHand:Int
)