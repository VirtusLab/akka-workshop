name := "akka-workshop-distributor"

organization := "com.virtuslab"

resolvers += "Workshop Repository" at "http://headquarters:8081/artifactory/libs-release-local"

version := "2.0.0"

scalaVersion := "2.12.5"

val akkaVersion = "2.5.11"

libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.11",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

//  realm=Artifactory Realm
//  host=localhost
//  user=xxx
//  password=xxx

credentials += Credentials(Path.userHome / ".ivy2" / ".local-credentials")

publishTo := Some("Workshop Repository" at "http://headquarters:8081/artifactory/libs-release-local")