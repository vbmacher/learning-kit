name := "solution3"
version := "1.0"
organization := "com.github"
scalaVersion := "2.12.1"
mainClass in assembly := Some("Solution")


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}