package mzc.bmstools.bmstable

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Success, Try}

class BMSTableURLsSpec extends AnyFlatSpec with Matchers {
  def serverAvailable(url: String): Boolean = {
    Try(JsoupBrowser().get(url)).isSuccess
  }

  def tableTest(mainUrl: String, headerUrl: String, dataUrl: String): Unit = {
    assume(serverAvailable(mainUrl))

    val res = BMSTableURLs(mainUrl)
    res.headerurl shouldBe Success(headerUrl)
    res.dataurl shouldBe Success(dataUrl)
  }

  it should "handle distinct URLs (LN Table)" in {
    tableTest(
      "http://flowermaster.web.fc2.com/lrnanido/gla/LN.html",
      "http://flowermaster.web.fc2.com/lrnanido/gla/header.json",
      "http://flowermaster.web.fc2.com/lrnanido/gla/score.json"
    )
  }

  it should "handle relative URL paths (Insane 2nd Table)" in {
    tableTest(
      "http://rattoto10.jounin.jp/table_insane.html",
      "http://rattoto10.jounin.jp/js/insane_header.json",
      "http://rattoto10.jounin.jp/js/insane_data.json"
    )
  }

  it should "handle relative URL files (10k Table)" in {
    tableTest(
      "https://notepara.com/glassist/10k",
      "https://notepara.com/glassist/10k/head.json",
      "https://notepara.com/glassist/10k/body.json"
    )
  }

  it should "handle relative URLs using dots (Jack Table)" in {
    tableTest(
      "http://infinity.s60.xrea.com/bms/gla/",
      "http://infinity.s60.xrea.com/bms/gla/header.json",
      "http://infinity.s60.xrea.com/bms/gla/data.json"
    )
  }
}
