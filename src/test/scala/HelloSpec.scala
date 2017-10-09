package com.gilcu2.http4sexample.specs

import org.http4s._
import org.http4s.dsl._
import org.scalatest._
import com.gilcu2.http4sexample._
import io.circe.Json
import org.http4s.circe._
import org.http4s.client.blaze.PooledHttp1Client
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder


class HelloSpec extends FlatSpec with Matchers with BeforeAndAfterAll {
  val builder = BlazeBuilder.bindLocal(55555).mountService(HelloWorld.service)
  val httpClient = PooledHttp1Client()

  var server: Server = _

  override def beforeAll = server = builder.run

  override def afterAll = server.shutdownNow()

  "Hello service" should "return hello name" in {
    val request = Request(Method.GET, uri("http://localhost:55555/hello/juan"))
    val task = httpClient.expect[Json](request)
    val response = task.unsafeRun

    response should be(Json.obj("message" -> Json.fromString("Hello, juan")))
  }

  "inc service" should "return number+1" in {
    val number = 3
    val request = Request(Method.GET, Uri.unsafeFromString(s"/inc/$number"))
    val task = HelloWorld.service.run(request)
    val maybeResponse = task.unsafeRun
    val response = maybeResponse.toOption.get

    response.status should be(Status.Ok)
    response.body.toString.toInt should be(number + 1)

  }

  "weather service" should "return temperature" in {
    val number = 3
    val request = Request(Method.GET, Uri.unsafeFromString(s"/hello/$number"))
    val task = HelloWorld.service.run(request)
    val maybeResponse = task.unsafeRun
    val response = maybeResponse.toOption.get

    response.status should be(Status.Ok)
    response.body.toString.toInt should be(number + 1)

  }

}
