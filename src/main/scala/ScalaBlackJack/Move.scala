package ScalaBlackJack

/**
 * @author Administrator
 */
class Move(val strRepr: String) {}
object Move {
  val Stay = new Move("Stay")
  val Hit = new Move("Hit")
  val DoubleDown = new Move("Double Down")
  val Split = new Move("Split")
}