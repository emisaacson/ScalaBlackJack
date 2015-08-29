package ScalaBlackJack

import org.http4s.server.blaze.BlazeBuilder

object ScalaBlackJackServer extends App {
  BlazeBuilder.bindHttp(8090)
    .mountService(ScalaBlackJack.service, "/")
    .run
    .awaitShutdown()
}
