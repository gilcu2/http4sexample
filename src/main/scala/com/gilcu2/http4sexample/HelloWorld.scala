package com.gilcu2.http4sexample

import java.time.Year

import io.circe._
import org.http4s._
import org.http4s.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import org.http4s.server._
import org.http4s.dsl._
import fs2.Task

object HelloWorld {

  implicit val yearQueryParamDecoder: QueryParamDecoder[Year] =
    QueryParamDecoder[Int].map(Year.of)

  object CountryQueryParamMatcher extends QueryParamDecoderMatcher[String]("country")

  object YearQueryParamMatcher extends QueryParamDecoderMatcher[String]("year")

  def getAverageTemperatureForCountryAndYear(country: String, year: String): Task[Int] =
    Task.delay(year.toInt + country.length)

  case class User(name: String)

  case class Hello(greeting: String)

  val service = HttpService {

    // Get string from path, return json
    case GET -> Root / "hello" / name =>
      Ok(Json.obj("message" -> Json.fromString(s"Hello, ${name}")))

    // Get number from path
    case request@GET -> Root / "inc" / IntVar(userId) =>
      Ok((userId + 1).toString)

    // Query aparameters
    case request@GET -> Root / "weather" / "temperature" :? CountryQueryParamMatcher(country) +& YearQueryParamMatcher(year) =>
      Ok(getAverageTemperatureForCountryAndYear(country, year).map(s"Average temperature for $country in $year was: " + _))

    // json management
    case GET -> Root / "helloJson" / name =>
      Ok(Hello(name).asJson)

    // POST with json
    case req@POST -> Root / "hello" =>
      for {
      // Decode a User request
        user <- req.as(jsonOf[User])
        // Encode a hello response
        resp <- Ok(Hello(user.name).asJson)
      } yield (resp)
  }




}
