import com.gilcu2.http4sexample.{HelloWorld, Streaming}
import fs2.{NonEmptyChunk, Task}
import org.http4s.Uri
import org.http4s.client.blaze.PooledHttp1Client
import org.http4s._
import org.http4s.client._
import org.http4s.dsl._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import java.nio.charset.Charset

import scala.concurrent.duration._

class StreamingSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  val port = 55555
  val builder = BlazeBuilder.bindLocal(port).mountService(Streaming.service)
  val httpClient = PooledHttp1Client()

  var server: Server = _

  override def beforeAll = server = builder.run

  override def afterAll = server.shutdownNow()

  "seconds service" should "return a stream of seconds" in {

    val request = GET(Uri.unsafeFromString(s"http://localhost:$port/seconds"))
    val response: fs2.Stream[Task, NonEmptyChunk[Byte]] = httpClient.streaming(request)(resp => resp.body.chunks).take(3)

    val result = response.runLog.unsafeRunFor(4.seconds)
    val s = result.map(_.map(_.toChar).toArray.mkString.stripSuffix(" ").toLong)
    s.size should be > 2

  }

}
