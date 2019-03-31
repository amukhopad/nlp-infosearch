
name := "spark-search"

version := "0.1"

scalaVersion := "2.11.12"

version := "1.0"

lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "com.amukhopad",
  scalaVersion := "2.11.12",
  test in assembly := {}
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.last
}

val sparkVersion = "2.4.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-mllib" % sparkVersion

libraryDependencies += "com.johnsnowlabs.nlp" %% "spark-nlp" % "2.0.0"
libraryDependencies += "com.databricks" %% "spark-xml" % "0.5.0"
