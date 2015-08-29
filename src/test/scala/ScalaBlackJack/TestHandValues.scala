package ScalaBlackJack

import org.specs2._
import ScalaBlackJack._

/**
 * @author Administrator
 */

class TestHandValues extends SpecWithJUnit { def is = s2"""

 Testing the value of some hands:
   []        = [0]                        $h1
   [A]       = [1,11]                     $h2
   [2]       = [2]                        $h3
   [2,A]     = [3,13]                     $h4
   [A,A,A]   = [3,13]                     $h5
   [A,A,A,7] = [10,20]                    $h6
                                          """

  val Ace = new Card(List(1,11), "A", Suit.Clubs)
  val Two = new Card(List(2), "2", Suit.Clubs)
  val Seven = new Card(List(7), "7", Suit.Clubs)

  def h1 = BlackJackSolver.getHandValue(List()) mustEqual List(0)
  def h2 = BlackJackSolver.getHandValue(List(Ace)) mustEqual List(1,11)
  def h3 = BlackJackSolver.getHandValue(List(Two)) mustEqual List(2)
  def h4 = BlackJackSolver.getHandValue(List(Two, Ace)) mustEqual List(3,13)
  def h5 = BlackJackSolver.getHandValue(List(Ace, Ace, Ace)) mustEqual List(3,13)
  def h6 = BlackJackSolver.getHandValue(List(Ace, Ace, Ace, Seven)) mustEqual List(10,20)
}
