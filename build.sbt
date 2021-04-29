name := """journal"""
organization := "jyk"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

enablePlugins(JavaAppPackaging)

javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.2",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "org.postgresql" % "postgresql" % "42.2.11",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.tensorflow" % "tensorflow-core-platform" % "0.2.0",
  "org.tensorflow" % "ndarray" % "0.2.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.8",
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "naver.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "naver.binders._"
