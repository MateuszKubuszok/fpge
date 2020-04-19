import sbt._
import Settings._

lazy val root = project.root.setName("fpge").setDescription("FP game engine build").configureRoot.aggregate(core)

val core = project
  .from("core")
  .setName("core")
  .setDescription("FP game engine wrapper around libGDX")
  .configureModule
  .configureTests()
  .settings(
    Compile / resourceGenerators += task[Seq[File]] {
      val file = (Compile / resourceManaged).value / "fpge-version.conf"
      IO.write(file, s"version=${version.value}")
      Seq(file)
    }
  )

val game = project
  .from("game")
  .setName("game")
  .setDescription("Game implementation")
  .setInitialImport()
  .configureModule
  .configureTests()
  .dependsOn(core)

addCommandAlias("fullTest", ";test;scalastyle")
addCommandAlias("fullCoverageTest", ";coverage;test;fun:test;scalastyle")
