package com.gilcu2.http4sexample.specs

import org.http4s._
import org.http4s.dsl._
import org.scalatest._
import com.gilcu2.http4sexample._
import org.http4s.util.CaseInsensitiveString

class HelloSpec extends FlatSpec with Matchers {

  "Hello service" should "return hello name" in {

    val request = Request(Method.GET, uri("/hello/juan"))
    val task = HelloWorld.service.run(request)
    val maybeResponse = task.unsafeRun
    val response=maybeResponse.toOption.get

    response.status should be( Status.Ok)
    response.headers.get(CaseInsensitiveString("Content-Type")).get.value should be("application/json")

  }

  "inc service" should "return number+1" in {
    val number = 3
    val request = Request(Method.GET, Uri.unsafeFromString(s"/hello/$number"))
    val task = HelloWorld.service.run(request)
    val maybeResponse = task.unsafeRun
    val response = maybeResponse.toOption.get

    response.status should be(Status.Ok)
    response.body.toString.toInt should be(number + 1)

  }

}
