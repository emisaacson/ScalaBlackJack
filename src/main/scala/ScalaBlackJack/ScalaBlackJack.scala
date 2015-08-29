package ScalaBlackJack

import org.http4s._
import org.http4s.server._
import org.http4s.dsl._
import org.http4s.headers._

import _root_.argonaut._, Argonaut._
import org.http4s.argonaut._

import scala.collection.mutable.MutableList

object ScalaBlackJack {
  def getCards(seed: Long = (new scala.util.Random) nextLong) = {
    val newDeck = new Deck(seed)
    newDeck.Shuffle
    val cardArray = newDeck.Cards.map((c:Card)=>jString(c toString))
    
    Ok(Json.obj("cards" -> jArray(cardArray), "seed" -> jNumber(newDeck.Seed)))
      // necessary because no charset is set by default
      .putHeaders(`Content-Type`(MediaType.`application/json`, Charset.`UTF-8`))
  }
  
  def getSolution(seed: Long = (new scala.util.Random).nextLong) = {
    val newDeck = new Deck(seed)
    newDeck.Shuffle
    val cardArray = newDeck.Cards.map((c:Card)=>jString(c toString))
    
    val solver = new BlackJackSolver(seed)
    val optimalWinnings = solver.solve()
    
    //val optimalMoves = solver.parent.foreach(())
    
    Ok(Json.obj("cards" -> jArray(cardArray),
                "seed" -> jNumber(newDeck.Seed),
                "optimalWinnings" -> jNumber(optimalWinnings)))
      // necessary because no charset is set by default
      .putHeaders(`Content-Type`(MediaType.`application/json`, Charset.`UTF-8`))
  }
  
  val service = HttpService {
    case GET -> Root / "cards" => getCards()
    case GET -> Root / "cards" / "solve" => getSolution()
    case GET -> Root / "cards" / seed => getCards(seed toLong)
    case GET -> Root / "cards" / seed / "solve" => getSolution(seed toLong)
    case req@POST -> Root / "solver" / seed / "partial" => {
      
      // Is this the best way? I do not know but it doesn't look like it
      val body = req.bodyAsText(Charset.`UTF-8`).runLog.run.reduce((acc, s) => acc + s)
      
      implicit def PersonAddressDecodeJson: DecodeJson[GameStateFromJson] =
        DecodeJson(c => for {
      
          dealerCards <- (c --\ "dealerCards").as[Seq[String]]
          playerCards <- (c --\ "playerCards").as[Seq[String]]
          startOfHand <- (c --\ "startOfHand").as[Int]
      
        } yield GameStateFromJson(dealerCards, playerCards, startOfHand))

      val gameState = body.decodeOption[GameStateFromJson].get
      
      val dealerCards = gameState.dealerCards map Card.createCardFromRepr
      val playerCards = gameState.playerCards map Card.createCardFromRepr
      
      val solver = new BlackJackSolver(seed toLong)
      val optimalWinnings = solver.solve(gameState.startOfHand, MutableList(dealerCards:_*), MutableList(playerCards:_*))
      val optimalMoveRightNow = solver.parent(gameState.startOfHand)(gameState.startOfHand - dealerCards.length - playerCards.length)
      
      Ok(jString(optimalMoveRightNow.strRepr))
        // necessary because no charset is set by default
        .putHeaders(`Content-Type`(MediaType.`application/json`, Charset.`UTF-8`))
    }
  }
}