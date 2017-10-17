package com.gilcu2.http4sexample

import scala.concurrent.duration._
// import scala.concurrent.duration._

import fs2.time
// import fs2.time

import fs2.{Scheduler, Strategy, Task}
// import fs2.{Scheduler, Strategy, Task}

import org.http4s._
// import org.http4s._

import org.http4s.dsl._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

case class Tick(seconds: Long)

object Streaming {

  // fs2's `time` module needs an implicit `Scheduler`
  implicit val scheduler = Scheduler.fromFixedDaemonPool(2)
  // scheduler: fs2.Scheduler = Scheduler(java.util.concurrent.ScheduledThreadPoolExecutor@59348245[Running, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0])

  // fs2 `Async` needs an implicit `Strategy`
  implicit val strategy = Strategy.fromExecutionContext(scala.concurrent.ExecutionContext.Implicits.global)
  // strategy: fs2.Strategy = Strategy

  // An infinite stream of the periodic elapsed time
  val seconds = time.awakeEvery[Task](1.second)

  val service = HttpService {

    case GET -> Root / "seconds" =>
      Ok(seconds.map(tick => s"${tick.toSeconds} "))

    case GET -> Root / "secondsJson" =>
      Ok(seconds.map(tick => Tick(tick.toSeconds).asJson.noSpaces))
  }

}
