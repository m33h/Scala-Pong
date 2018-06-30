package com.mtarsa.pong
package desktop

import core._

import sgl.{InputHelpersComponent, GameLoopStatisticsComponent}
import sgl.awt._
import sgl.awt.util._


object Main extends AbstractApp with AWTApp
  with InputHelpersComponent with VerboseStdErrLoggingProvider with GameLoopStatisticsComponent {

  override val TargetFps = Some(60)

  override val frameDimension = (TotalWidth, TotalHeight)

}
