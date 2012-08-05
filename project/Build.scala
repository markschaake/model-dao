import sbt._
import Keys._
import org.ensime.sbt.Plugin.Settings.ensimeConfig
import org.ensime.sbt.util.SExp._

object AppBuild extends Build {

  val appName = "modeldao"

  lazy val root = Project(id = appName,
    base = file("."),
    settings = Project.defaultSettings ++ Seq(

	  scalacOptions += "-deprecation",
	  publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/projects/markschaake.github.com/snapshots"))),

      organization := "markschaake",
      version := "0.1.0-SNAPSHOT",
	  isSnapshot := true,

      // ensime config
      ensimeConfig := sexp(
        // formatter, make sure to keep in sync settings with eclipse settings
        key(":formatting-prefs"), sexp(
          key(":alignSingleLineCaseStatements"), true))))

}
