import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "squadXML"
    val appVersion      = "1.2.2"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
