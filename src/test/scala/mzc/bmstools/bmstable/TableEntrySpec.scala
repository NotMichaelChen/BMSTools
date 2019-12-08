package mzc.bmstools.bmstable

import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec

import spray.json._
import mzc.bmstools.bmstable.TableEntryProtocol._

class TableEntrySpec extends AnyFlatSpec with Matchers {
  it should "Decode a TableEntry" in {
    val testEntry =
      """
        |{
        |  "md5": "asdf",
        |  "level": "1"
        |}
        |""".stripMargin

    testEntry.parseJson.convertTo[TableEntry] shouldBe TableEntry("asdf", "1")
  }

  it should "Decode a new type extending BaseTableEntry" in {
    case class NewTableEntry(md5: String, level: String, title: String) extends BaseTableEntry

    object NewTableEntryProtocol extends DefaultJsonProtocol {
      implicit val newTableEntryFormat: RootJsonFormat[NewTableEntry] = jsonFormat3(NewTableEntry)
    }

    val testEntry =
      """
        |{
        |  "md5": "asdf",
        |  "level": "1",
        |  "title": "A title"
        |}
        |""".stripMargin

    import NewTableEntryProtocol._

    testEntry.parseJson.convertTo[NewTableEntry] shouldBe NewTableEntry("asdf", "1", "A title")
  }
}
