package mzc.bmstools.bmstable

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.util.Try

class TableDataParserSpec extends AnyFlatSpec with Matchers {
  // TODO: Add tests for rest of methods

  def serverAvailable(url: String): Boolean = {
    Try(JsoupBrowser().get(url)).isSuccess
  }

  // TODO: Test could be written in a more unit-like way
  "getData" should "Decode a list of charts into a custom case class" in {
    assume(serverAvailable("http://rattoto10.jounin.jp/table_insane.html"))

    case class CustomTableEntry(md5: String, level: String, title: String) extends BaseTableEntry

    object CustomTableEntryProtocol extends DefaultJsonProtocol {
      implicit val newTableEntryFormat: RootJsonFormat[CustomTableEntry] = jsonFormat3(CustomTableEntry)
    }

    import CustomTableEntryProtocol._

    val parser         = TableDataParser("http://rattoto10.jounin.jp/table_insane.html")
    val dataTry        = parser.getData[CustomTableEntry]
    val defaultDataTry = parser.getDefaultData

    dataTry.isSuccess shouldBe true

    for {
      defaultData <- defaultDataTry
      data        <- dataTry

      _ = defaultData.length shouldEqual data.length
    } yield ()
  }
}
