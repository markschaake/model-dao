scalaVersion := "2.10.0-RC1"

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.11" % "test")

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                    "releases"  at "http://oss.sonatype.org/content/repositories/releases")

