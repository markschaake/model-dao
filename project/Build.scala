import sbt._
import Keys._

object AppBuild extends Build {

  val appName = "modeldao"

  lazy val root = Project(id = appName,
    base = file("."),
    settings = Project.defaultSettings ++ Seq(

	  scalacOptions += "-deprecation",
	  publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/projects/markschaake.github.com/snapshots"))),

      organization := "markschaake",
      version := "0.1.0-SNAPSHOT",
	  isSnapshot := true))

}
