import mzc.sbt.Versions

name := "BMSTools"

version := "1.0"

scalaVersion := Versions.Scala

libraryDependencies ++= Seq(
  "net.ruippeixotog" %% "scala-scraper" % Versions.ScalaScraper,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % Versions.JacksonScala
)

