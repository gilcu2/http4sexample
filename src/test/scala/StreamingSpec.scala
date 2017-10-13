import com.gilcu2.http4sexample.Streaming
import org.http4s.Uri
import org.http4s.client.blaze.PooledHttp1Client
import org.http4s._
import org.http4s.client._
import org.http4s.dsl._
import org.http4s.server.blaze.BlazeBuilder
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class StreamingSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  "seconds service" should "return a stream of seconds" in {

    val request = GET(uri("/seconds"))
    val task = Streaming.service.run(request)
    val response = task.unsafeRun

  }

}
