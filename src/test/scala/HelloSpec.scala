package com.gilcu2.http4sexample.specs

import org.http4s._
import org.http4s.dsl._
import org.scalatest._
import com.gilcu2.http4sexample._

class HelloSpec extends FlatSpec with Matchers {

  "A Hello service" should "return hello" in {

    val request = Request(Method.GET, uri("/hello/juan"))
    val task = HelloWorld.service.run(request)
    val maybeResponse = task.unsafeRun
    val response=maybeResponse.toOption.get

    response.status should be( Status.Ok)

  }

}
