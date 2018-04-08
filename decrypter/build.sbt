name := "akka-workshop-decrypter"

organization := "com.virtuslab"

resolvers += "Workshop Repository" at "http://headquarters:8081/artifactory/libs-release-local"

version := "2.0.0"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "commons-codec" % "commons-codec" % "1.11"
)

//  realm=Artifactory Realm
//  host=localhost
//  user=xxx
//  password=xxx

credentials += Credentials(Path.userHome / ".ivy2" / ".local-credentials")

publishTo := Some("Workshop Repository" at "http://headquarters:8081/artifactory/libs-release-local")