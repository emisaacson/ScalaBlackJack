package ScalaBlackJack

import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.middleware.CORS
import org.http4s.server.middleware.CORSConfig

object ScalaBlackJackServer extends App {
  BlazeBuilder.bindHttp(8090)
    .mountService(
        CORS(ScalaBlackJack.service, CORS.DefaultCORSConfig), "/")
    .run
    .awaitShutdown()
}
