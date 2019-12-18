import mzc.sbt.Versions

name := "BMSTools"

organization := "mzc.bmstools"
version := "3.0"

scalaVersion := Versions.Scala

libraryDependencies ++= Seq(
  "net.ruippeixotog" %% "scala-scraper" % Versions.ScalaScraper,
  "io.spray"         %% "spray-json"    % Versions.SprayJson,
  "org.scalatest"    %% "scalatest"     % Versions.ScalaTest % Test
)
