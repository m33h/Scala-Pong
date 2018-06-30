package com.mtarsa.pong
package core

import sgl._
import sgl.util._

trait AbstractApp extends PongScreenComponent {
  this: GameApp with InputHelpersComponent =>

  override def startingScreen: GameScreen = new PongScreen

}
