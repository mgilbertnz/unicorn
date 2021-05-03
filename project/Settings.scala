import sbtrelease.ReleasePlugin.autoImport._
import sbt.Keys._
import sbt._

object Settings {

  val scala_2_12 = "2.12.13"
  val scala_2_13 = "2.13.5"

  val alsoOnTest = "compile->compile;test->test"

  // settings for ALL modules, including parent
  val common = Seq(
    organization := "com.financialplatforms",

    Test / parallelExecution := false,
    Test / testOptions += Tests.Argument("-oDF"),
    publishTo := {
      val nexus = "https://nexus.financialplatforms.co.nz/repository/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "snapshots")
      else
        Some("releases" at nexus + "releases")
    },
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishMavenStyle := true
  )

  val core = common ++ Seq(
    scalaVersion := scala_2_13,
    crossScalaVersions := Seq(scala_2_12, scala_2_13),
    releaseCrossBuild := true
  )

  val play = common ++ Seq(
    scalaVersion := scala_2_13,
    crossScalaVersions := Seq(scala_2_12, scala_2_13),
    releaseCrossBuild := true
  )

  // common settings for play and core modules
  val parent = common ++ Seq(
    scalaVersion := scala_2_13,
    crossScalaVersions := Seq(scala_2_12, scala_2_13),
    releaseCrossBuild := true,
    resolvers += Resolver.typesafeRepo("releases"),
    resolvers += Resolver.sonatypeRepo("releases"),
    resolvers += Resolver.sonatypeRepo("snapshots"),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-unchecked",
      "-Xlint",
      "-Xfatal-warnings"
    ),
    updateOptions := updateOptions.value.withCachedResolution(true),
  )
}
