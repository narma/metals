package tests

import scala.meta.internal.metals.BuildTools

object SbtDetectionSuite extends BaseSuite {
  def checkNotSbt(name: String, layout: String): Unit = {
    checkSbt(name, layout, isTrue = false)
  }
  def checkSbt(name: String, layout: String, isTrue: Boolean = true): Unit = {
    test(name) {
      val workspace = FileLayout.fromString(layout)
      workspace.toFile.deleteOnExit()
      val isSbt = new BuildTools(workspace).isSbt
      if (isTrue) assert(isSbt)
      else assert(!isSbt)
    }
  }

  checkNotSbt(
    "build.sbt",
    """|/build.sbt
       |lazy val a = project
       |""".stripMargin
  )

  checkSbt(
    "sbt.version",
    """|/project/build.properties
       |sbt.version = 0.13
       |/build.sbt
       |lazy val a = project
       |""".stripMargin
  )

  checkSbt(
    "build.scala",
    """|/project/build.properties
       |sbt.version = 0.13
       |/project/Build.scala
       |import sbt._
       |""".stripMargin
  )

  checkNotSbt(
    "gradle-property",
    """|/project/build.properties
       |gradle.version = 0.13
       |/build.sbt
       |lazy val a = project
       |""".stripMargin
  )

}
