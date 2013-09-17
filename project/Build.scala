import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "DrillMaster"
    val appVersion      = "2.1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
	  jdbc,
	  anorm
    )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions += "-feature"
  )

}
