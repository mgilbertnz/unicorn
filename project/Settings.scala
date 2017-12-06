import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.pgp.PgpKeys
import sbtrelease.ReleasePlugin.autoImport._
import sbt.Keys._
import sbt._
import xerial.sbt.Sonatype

object Settings {

  val scala_2_11 = "2.11.11"
  val scala_2_12 = "2.12.2"

  val alsoOnTest = "compile->compile;test->test"

  // settings for ALL modules, including parent
  val common = Seq(
    organization := "org.virtuslab",

    parallelExecution in Test := false,
    testOptions in Test += Tests.Argument("-oDF"),
    publishTo := {
      val nexus = "http://nexus.financialplatforms.co.nz:8081/nexus/content/repositories/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "snapshots")
      else
        Some("releases" at nexus + "releases")
    },
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishMavenStyle := true
  )

  val core = common ++ Seq(
    scalaVersion := scala_2_11,
    crossScalaVersions := Seq(scala_2_11, scala_2_12),
    releaseCrossBuild := true
  )

  val play = common ++ Seq(
    scalaVersion := scala_2_11,
    crossScalaVersions := Seq(scala_2_11, scala_2_12),
    releaseCrossBuild := true
  )

  // common settings for play and core modules
  val parent = common ++ Seq(
    scalaVersion := scala_2_11,
    crossScalaVersions := Seq(scala_2_11, scala_2_12),
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
    scoverage.ScoverageKeys.coverageFailOnMinimum := true
  ) ++ SbtScalariform.scalariformSettings
}
