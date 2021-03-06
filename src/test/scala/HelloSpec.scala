package com.gilcu2.http4sexample.specs

import com.gilcu2.http4sexample.HelloWorld.{Hello, User}
import org.http4s.dsl._
import org.scalatest._
import com.gilcu2.http4sexample._
import io.circe.Json
import org.http4s.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s.client.blaze.PooledHttp1Client
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder
import org.http4s._
import org.http4s.client._




class HelloSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  val port = 55555
  val builder = BlazeBuilder.bindLocal(port).mountService(HelloWorld.service)
  val httpClient = PooledHttp1Client()

  var server: Server = _

  override def beforeAll = server = builder.run

  override def afterAll = server.shutdownNow()

  "Hello service" should "return hello name" in {
    val request = GET(Uri.unsafeFromString(s"http://localhost:$port/hello/juan"))
    val task = httpClient.expect[Json](request)
    val response = task.unsafeRun

    response should be(Json.obj("message" -> Json.fromString("Hello, juan")))
  }

  "inc service" should "return number+1" in {
    val number = 3
    val request = GET(Uri.unsafeFromString(s"http://localhost:$port/inc/$number"))
    val task = httpClient.expect[String](request)
    val response = task.unsafeRun
    response should be("4")

  }

  "weather service" should "return temperature" in {
    val country = "Cuba"
    val year = 1996
    val request = GET(Uri.unsafeFromString(
      s"http://localhost:$port/weather/temperature?country=$country&year=$year"))
    val task = httpClient.expect[String](request)
    val response = task.unsafeRun
    response should be(s"Average temperature for $country in $year was: " + (year + country.length))

  }

  "Hello service" should "return hello json" in {
    val name = "Juan"
    val request = GET(Uri.unsafeFromString(s"http://localhost:$port/helloJson/$name"))
    val task = httpClient.expect[Json](request)
    val response = task.unsafeRun

    response should be(Hello(name).asJson)
  }

  "Hello service" should "post json" in {
    val name = "Juan"
    val request = POST(Uri.unsafeFromString(s"http://localhost:$port/hello"), User(name).asJson)
    val task = httpClient.expect[Json](request)
    val response = task.unsafeRun

    response should be(Hello(name).asJson)
  }

}
