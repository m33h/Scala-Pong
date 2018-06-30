package com.mtarsa.pong
package core

import sgl._
import geometry._
import scene._
import util._

trait PongScreenComponent {
  self: GraphicsProvider with InputProvider with GameStateComponent
  with WindowProvider with InputHelpersComponent
  with LoggingProvider with SystemProvider =>

  import Graphics._

  val NbRows = 30
  val NbCols = 30

  val TotalWidth = NbCols*20
  val TotalHeight = NbRows*20

  private implicit val LogTag = Logger.Tag("pong-screen")

  class PongScreen extends FixedTimestepGameScreen(1000/48) {

    override def name: String = "PongScreen"
    var rand = new java.util.Random
    var ballPos : Point = Point(rand.nextInt(NbCols), rand.nextInt(NbRows))
    var ballDir : Vec = Vec(1,-1)

    var player2Pallet : List[Point] = List(Point(NbCols/2-2, NbRows-1), Point(NbCols/2-1, NbRows-1), Point(NbCols/2, NbRows-1), Point(NbCols/2+1, NbRows-1), Point(NbCols/2+2, NbRows-1))
    var player1Pallet : List[Point] = List(Point(NbCols/2-2, 0), Point(NbCols/2-1, 0), Point(NbCols/2, 0), Point(NbCols/2+1, 0), Point(NbCols/2+2, 0))

    def gameOver(): Unit = {
      gameState.newScreen(new PongScreen())
    }

    def movePalletLeft(pallet : List[Point]): List[Point] = {
      val firstElem = Point(pallet.head.x-1, pallet.head.y)
      firstElem :: pallet.slice(0, pallet.size-1)
    }

    def movePalletRight(pallet : List[Point]): List[Point] = {
      val lastElem = Point(pallet.last.x + 1, pallet.last.y)
      (lastElem :: pallet.slice(1,pallet.size).reverse).reverse
    }

    override def fixedUpdate(): Unit = {
      val direction = new Vec(1,0);
      var userDirection: Vec = direction
      Input.processEvents(e => e match {
        case Input.KeyDownEvent(Input.Keys.M)  => player1Pallet = movePalletLeft(player1Pallet)
        case Input.KeyDownEvent(Input.Keys.K)  => player1Pallet = movePalletRight(player1Pallet)
        case Input.KeyDownEvent(Input.Keys.Q)  => player2Pallet = movePalletLeft(player2Pallet)
        case Input.KeyDownEvent(Input.Keys.A)  => player2Pallet = movePalletRight(player2Pallet)
        case _ => ()
      })
    }

    def drawSquare(canvas: Canvas, point: Point, paint: Paint) = {
      canvas.drawRect(point.x.toInt * 20, point.y.toInt * 20, 20, 20, paint)
    }

    def drawPallets(canvas : Canvas, p1 : List[Point], p2 : List[Point]): Unit = {
      (p1 ++ p2).map{ p => drawSquare(canvas, p, defaultPaint.withColor(Color.White)) }
    }

    def drawBall(canvas : Canvas): Unit = {
      drawSquare(canvas, ballPos, defaultPaint.withColor(Color.White))
    }

    def palletCollision(pallet : List[Point]): Boolean = {
      pallet.contains(Point(ballPos.x+ballDir.x, ballPos.y+ballDir.y))
    }

    def ballMove(): Unit = {
      if(ballPos.x < 0 || ballPos.x >= NbCols) {
        ballDir = Vec(-1 * ballDir.x, ballDir.y)
      }
      if(palletCollision(player1Pallet) || palletCollision(player2Pallet)) {
        ballDir = Vec(-1 * ballDir.x, -1 * ballDir.y)
      }
      if(ballPos.y < 0 || ballPos.y >= NbRows) {
        gameOver()
      }
      ballPos = Point(ballPos.x + ballDir.x * 0.5, ballPos.y + ballDir.y * 0.5)
    }

    override def render(canvas: Canvas): Unit = {
      canvas.drawRect(0, 0, WindowWidth, WindowHeight, defaultPaint.withColor(Color.Black))
      drawPallets(canvas, player1Pallet, player2Pallet)
      drawBall(canvas)
      ballMove()
    }
  }
}
