package mzc.bmstools.bmstable

import java.net.URL

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.util.Try

/**
  * Parses the data of a bms table
  *
  * @param tableurls The header/data urls associated with the table
  */
class TableDataParser(tableurls: BMSTableURLs) {
  // TODO: can we enable caching this data somehow to avoid downloading it every time?
  val datajson: Try[List[Map[String, String]]] = {
    val input = tableurls.dataurl.map(new URL(_).openStream)

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    input.map(mapper.readValue(_, classOf[List[Map[String, String]]]))
  }

  /**
    * Attempt to obtain the list of charts from the downloaded json, returning only the default attributes
    *
    * @return The list of table entries
    */
  def getDefaultData: Try[List[TableEntry]] = {
    datajson.map(_.map(
      listentry => TableEntry(listentry("md5"), listentry("level"))
    ))
  }
}

object TableDataParser {
  //
  /**
    * Construct a BMSTable object from a single table url
    *
    * @param tableurl The url of the main bms table
    * @return A TableParser object
    */
  def apply(tableurl: String): TableDataParser = {
    new TableDataParser(new BMSTableURLs(tableurl))
  }
}
