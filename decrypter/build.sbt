name := "akka-workshop-decrypter"

organization := "com.virtuslab"

resolvers += "Workshop Repository" at "http://headquarters:8081/artifactory/libs-release-local"

version := "1.0.6"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "commons-codec" % "commons-codec" % "1.9"
)

//  realm=Artifactory Realm
//  host=localhost
//  user=xxx
//  password=xxx

credentials += Credentials(Path.userHome / ".ivy2" / ".local-credentials")

publishTo := Some("Workshop Repository" at "http://headquarters:8081/artifactory/libs-release-local")