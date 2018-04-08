name := "workshop-server"

organization := "com.virtuslab"

version := "2.0.0"

resolvers += "Workshop Repository" at "http://headquarters:8081/artifactory/libs-release-local"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.5"

val akkaVersion = "2.5.11"
libraryDependencies ++= Seq(
  guice,
  ws,
  specs2 % Test,
  "commons-codec" % "commons-codec" % "1.11",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)