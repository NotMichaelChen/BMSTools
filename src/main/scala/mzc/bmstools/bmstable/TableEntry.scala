package mzc.bmstools.bmstable

import spray.json.DefaultJsonProtocol
import spray.json._

/**
  * Trait representing an entry in the table
  * Extend this trait to pick up more attributes in the data json. Note that additional attributes
  *  should have the same name as the attributes in the json
  *
  * Note that you will also need to provide a protocol extending DefaultJsonProtocol. See the `TableEntry`
  *  case class and object for an example extending BaseTableEntry
  */
trait BaseTableEntry {
  val md5: String
  val level: String
}

case class TableEntry(md5: String, level: String) extends BaseTableEntry

object TableEntryProtocol extends DefaultJsonProtocol {
  implicit val tableEntryFormat: RootJsonFormat[TableEntry] = jsonFormat2(TableEntry)
}
