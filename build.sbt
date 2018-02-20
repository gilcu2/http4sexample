organization := "com.gilcu2"
name := "http4sexample"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.12.3"

val Http4sVersion = "0.17.4"
val LogbackVersion = "1.2.3"
val circeVersion = "0.8.0"
val elasticSearchVersion = "5.6.2"

libraryDependencies ++= Seq(
 "org.http4s"     %% "http4s-blaze-server" % Http4sVersion,
 "org.http4s"     %% "http4s-blaze-client" % Http4sVersion,
 "org.http4s"     %% "http4s-circe"        % Http4sVersion,
 "org.http4s"     %% "http4s-dsl"          % Http4sVersion,
  "ch.qos.logback" % "logback-classic" % LogbackVersion,
  // Optional for auto-derivation of JSON codecs
 "io.circe" %% "circe-generic" % circeVersion,
  // Optional for string interpolation to JSON model
 "io.circe" %% "circe-literal" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elasticSearchVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-jackson" % elasticSearchVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-streams" % elasticSearchVersion,
  //  "com.alessandromarrella" %% "fs2-elastic" % "fs2ElasticVersion",
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"