ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "WeatherAppSBT",
    libraryDependencies ++= Seq("org.jsoup" % "jsoup" % "1.17.2"),
    libraryDependencies ++= Seq("com.lihaoyi" % "ujson_2.13" % "3.0.0")
  )
