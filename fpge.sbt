import sbt._
import Settings._

lazy val root = project.root
  .setName("root")
  .setDescription("FP game engine build")
  .configureRoot
  .aggregate(core)

lazy val core = project.from("core")
  .setName("core")
  .setDescription("FP game engine wrapper around libGDX")
  .setInitialImport()
  .configureModule
  .configureTests()
  .settings(Compile / resourceGenerators += task[Seq[File]] {
    val file = (Compile / resourceManaged).value / "fpge-version.conf"
    IO.write(file, s"version=${version.value}")
    Seq(file)
  })

addCommandAlias("fullTest", ";test;scalastyle")
addCommandAlias("fullCoverageTest", ";coverage;test;fun:test;scalastyle")
