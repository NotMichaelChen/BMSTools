package mzc.bmstools.bmstable

import spray.json._
import DefaultJsonProtocol._

import scala.util.Try

/**
  * Parses the data of a bms table
  *
  * @param tableurls The header/data urls associated with the table
  */
class TableDataParser(tableurls: BMSTableURLs) {
  // TODO: can we enable caching this data somehow to avoid downloading it every time?
  private val rawDataJson: Try[List[JsValue]] = {
    for {
      header <- tableurls.dataurl
      input  <- Util.downloadWebpage(header)
      data   <- Try(input.parseJson)
      // Assume a list of objects
    } yield data.convertTo[List[JsValue]]
  }

  val datajson: Try[List[Map[String, String]]] = rawDataJson.map(_.map(_.convertTo[Map[String, String]]))

  /**
    * Attempt to obtain the list of charts from the downloaded json, returning only the default attributes
    *
    * @return The list of table entries
    */
  def getDefaultData: Try[List[TableEntry]] = {
    datajson.map(
      _.map(
        listentry => TableEntry(listentry("md5"), listentry("level"))
      )
    )
  }

  /**
    * Attempt to obtain the list of charts from the data json decoded as a list of the provided type `A`
    *
    * @tparam A The type to decode each chart into
    * @return The list of table entries decoded as a list of `A`'s
    */
  def getData[A <: BaseTableEntry: JsonReader]: Try[List[A]] = {
    rawDataJson.map(_.map(_.convertTo[A]))
  }
}

object TableDataParser {

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
