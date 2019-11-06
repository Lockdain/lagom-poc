organization in ThisBuild := "ru.msk.java"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `lagom-poc` = (project in file("."))
  .aggregate(`lagom-poc-api`, `lagom-poc-impl`, `lagom-poc-stream-api`, `lagom-poc-stream-impl`)

lazy val `lagom-poc-api` = (project in file("lagom-poc-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-poc-impl` = (project in file("lagom-poc-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`lagom-poc-api`)

lazy val `lagom-poc-stream-api` = (project in file("lagom-poc-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-poc-stream-impl` = (project in file("lagom-poc-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`lagom-poc-stream-api`, `lagom-poc-api`)
