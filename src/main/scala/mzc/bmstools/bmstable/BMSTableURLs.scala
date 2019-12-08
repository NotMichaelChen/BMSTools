package mzc.bmstools.bmstable

import java.net.URI

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.util.Try

/**
  * Represents all relevant url's associated with a bms table
  *
  * @param tableurl The url of the main bms table
  */
case class BMSTableURLs(tableurl: String) {
  // Pull the header url from the webpage
  val headerurl: Try[String] = {
    val browser = JsoupBrowser()
    Try(browser.get(tableurl))
      .map(doc => {
        val rawurl = doc >> attr("content")("meta[name=bmstable]")
        BMSTableURLs.parseURL(tableurl, rawurl)
      })
  }

  // Pull the data url from the json obtained from the header url
  val dataurl: Try[String] = {
    for {
      header   <- headerurl
      rawInput <- Util.downloadWebpage(header)
      // TODO: Hacky, find alternative to cleaning input
      input  = rawInput.dropWhile(_ != '{')
      parsed = input.parseJson.convertTo[Map[String, JsValue]]
      url    = BMSTableURLs.parseURL(header, parsed("data_url").convertTo[String])
    } yield url
  }
}

object BMSTableURLs {
  // Parses a base URL and a url from bmstable and produces the actual url
  private def parseURL(baseurl: String, rawurl: String): String = {
    if (rawurl.startsWith("http"))
      rawurl
    else {
      new URI(baseurl)
        .resolve(rawurl)
        .toString
    }
  }
}
