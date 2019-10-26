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
    val doc = Try(browser.get(tableurl))
    val rawurl = doc.map(_ >> attr("content")("meta[name=bmstable]"))
    rawurl.map(BMSTableURLs.parseURL(tableurl, _))
  }

  // Pull the data url from the json obtained from the header url
  val dataurl: Try[String] = {
    val input = headerurl.map(new URL(_).openStream)

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    val parsedJson = input.map(mapper.readValue(_, classOf[Map[String, String]]))
    parsedJson.map(json => BMSTableURLs.parseURL(tableurl, json("data_url")))
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