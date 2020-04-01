name := "Lint"

version := "0.1"

scalaVersion := "2.12.6"

organization := "io.cat.ai"

libraryDependencies += "org.jctools" % "jctools-core" % "3.0.0"

enablePlugins(JmhPlugin)
mainClass in (Jmh, run) := Some("io.cat.ai.lint.JmhRunnerApp")