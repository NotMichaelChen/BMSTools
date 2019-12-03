package mzc.bmstools.bmstable

import java.net.URL

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

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
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    headerurl.map(header => {
      val input = new URL(header).openStream
      val parsedJson = mapper.readValue(input, classOf[Map[String, String]])
      BMSTableURLs.parseURL(tableurl, parsedJson("data_url"))
    })
  }
}

object BMSTableURLs {
  // Parses a base URL and a url from bmstable and produces the actual url
  private def parseURL(baseurl: String, rawurl: String): String = {
    if(rawurl.startsWith("http"))
      rawurl
    else {
      val base = new URL(baseurl)
      val newurl = new URL(base, rawurl.stripPrefix("/"))
      newurl.toString
    }
  }
}