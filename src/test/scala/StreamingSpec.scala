import com.gilcu2.http4sexample.{HelloWorld, Streaming}
import fs2.Task
import org.http4s.Uri
import org.http4s.client.blaze.PooledHttp1Client
import org.http4s._
import org.http4s.client._
import org.http4s.dsl._
import org.http4s.server.blaze.BlazeBuilder
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent.duration._

class StreamingSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  val port = 55555
  val builder = BlazeBuilder.bindLocal(port).mountService(Streaming.service)
  val httpClient = PooledHttp1Client()

  "seconds service" should "return a stream of seconds" in {

    //    val request = Request(Method.GET, uri("/seconds"))
    val request = GET(Uri.unsafeFromString(s"http://localhost:$port/seconds"))
    val response: fs2.Stream[Task, Byte] = httpClient.streaming(request)(resp => resp.body).take(2)


    //
    //    val task = Streaming.service.run(request)
    //    val response = task.unsafeRunFor(4.seconds).toOption.get.body.runLast.unsafeRunFor(4.seconds).toString
    response.size should be > 0

  }

}
