name := """assignment"""

version := "1.0"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "com.assembla.scala-incubator" %% "graph-core" % "1.9.4"

libraryDependencies += "com.assembla.scala-incubator" %% "graph-constrained" % "1.9.0"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.5" % "test"

libraryDependencies += "com.lihaoyi" %% "scalarx" % "0.2.8"

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

