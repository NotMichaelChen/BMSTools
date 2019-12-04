import mzc.sbt.Versions

name := "BMSTools"

version := "1.2"

scalaVersion := Versions.Scala

libraryDependencies ++= Seq(
  "net.ruippeixotog" %% "scala-scraper" % Versions.ScalaScraper,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % Versions.JacksonScala,
  "org.scalatest" %% "scalatest" % Versions.ScalaTest % Test
)

assemblyMergeStrategy in assembly := {
  case PathList(xs @ _*) if xs.last == "module-info.class" => MergeStrategy.first
  case x => (assemblyMergeStrategy in assembly).value(x)
}