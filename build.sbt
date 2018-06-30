import sbtcrossproject.{crossProject, CrossType}

val scalaVer = "2.12.0"
val scalaNativeVer = "2.11.8"
val sglGitHubLink = s"git://github.com/regb/scala-game-library.git"

lazy val commonSettings = Seq(
  version        := "1.0",
  scalaVersion   := scalaVer,
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
)

lazy val commonNativeSettings = Seq(
  scalaVersion  := scalaNativeVer
)

lazy val sglCoreJVM = ProjectRef(uri(sglGitHubLink), "coreJVM")
lazy val sglCoreNative = ProjectRef(uri(sglGitHubLink), "coreNative")
lazy val sglCoreJS = ProjectRef(uri(sglGitHubLink), "coreJS")
lazy val sglDesktopNative = ProjectRef(uri(sglGitHubLink), "desktopNative")
lazy val sglDesktop = ProjectRef(uri(sglGitHubLink), "desktopAWT")
lazy val sglHtml5 = ProjectRef(uri(sglGitHubLink), "html5")

lazy val core = (crossProject(JSPlatform, JVMPlatform, NativePlatform).crossType(CrossType.Pure) in file("./core"))
  .settings(commonSettings: _*)
  .settings(name := "sgl-snake-core")
  .jvmSettings(
    exportJars := true
  )
  .nativeSettings(scalaVersion := scalaNativeVer)
  .jvmConfigure(_.dependsOn(sglCoreJVM))
  .jsConfigure(_.dependsOn(sglCoreJS))
  .nativeConfigure(_.dependsOn(sglCoreNative))

lazy val coreJVM = core.jvm
lazy val coreJS = core.js
lazy val coreNative = core.native

lazy val desktop = (project in file("./desktop"))
  .settings(commonSettings: _*)
  .settings(
    name := "sgl-snake-desktop"
  )
  .dependsOn(sglCoreJVM, sglDesktop, coreJVM)

lazy val OS = sys.props("os.name").toLowerCase
lazy val LinuxName = "Linux"
lazy val MacName = "Mac OS X"

def isLinux(name: String): Boolean = name.startsWith(LinuxName.toLowerCase)
def isMac(name: String): Boolean = name.startsWith(MacName.toLowerCase)
