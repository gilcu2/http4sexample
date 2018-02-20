package com.gilcu2.http4sexample


import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import cats.effect._
import fs2.Scheduler
import org.http4s._
import org.http4s.dsl.io._

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

case class Tick(seconds: Long)

object ElasticFs2 {

  import com.alessandromarrella.fs2_elastic.Client
  import com.alessandromarrella.fs2_elastic.syntax.search._
  import org.elasticsearch.action.search.SearchRequest

  val indexName = "a-products-de/product"
  val keepAlive = 10 minutes


  def buildClient = {

    Client.fromHosts[IO](new HttpHost("localhost", 9200))
  }

  lazy val client = buildClient


  def searchStream: Stream[IO, (RestHighLevelClient, SearchResponse)] = {
    client.searchScroll(new SearchRequest(indexName), keepAlive)
  }

}


object Streaming {

  // fs2's `time` module needs an implicit `Scheduler`
  implicit val scheduler = Scheduler.fromFixedDaemonPool(2)
  // scheduler: fs2.Scheduler = Scheduler(java.util.concurrent.ScheduledThreadPoolExecutor@59348245[Running, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0])

  // fs2 `Async` needs an implicit `Strategy`
  implicit val strategy = Strategy.fromExecutionContext(scala.concurrent.ExecutionContext.Implicits.global)
  // strategy: fs2.Strategy = Strategy

  // An infinite stream of the periodic elapsed time
  val seconds: fs2.Stream[Task, FiniteDuration] = time.awakeEvery[Task](1.second)

  val service = HttpService {

    case GET -> Root / "seconds" =>
      Ok(seconds.map(tick => s"${tick.toSeconds} "))

    case GET -> Root / "secondsJson" =>
      Ok(seconds.map(tick => Tick(tick.toSeconds).asJson.noSpaces))


  }

}
