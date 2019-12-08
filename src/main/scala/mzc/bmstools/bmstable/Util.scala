package mzc.bmstools.bmstable

import scala.io.Source
import scala.util.{Try, Using}

object Util {
  def downloadWebpage(url: String): Try[String] = {
    Using(Source.fromURL(url))(_.mkString)
  }
}
