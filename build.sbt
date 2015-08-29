name := "ScalaBlackJack"
version := "1.0"
scalaVersion := "2.11.7"
mainClass := Some("ScalaBlackJack.ScalaBlackJackServer")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % "0.9.1",
  "org.http4s" %% "http4s-dsl"         % "0.9.1",
  "org.http4s" %% "http4s-argonaut"    % "0.9.1",
  "org.specs2" %% "specs2-core" % "3.6.4" % "test",
  "org.specs2" %% "specs2-junit" % "3.6.4" % "test",
  "org.slf4j" % "slf4j-simple" % "1.7.12"
)

scalacOptions in Test ++= Seq("-Yrangepos")