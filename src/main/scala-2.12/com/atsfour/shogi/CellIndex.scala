package com.atsfour.shogi

import scala.util.Try

trait CellIndex {
  val xPos: Int
  val yPos: Int

  /**
    * get rotated cell index by 180 degrees
    * @return rotated cell index
    */
  def rotated: CellIndex
}

object CellIndex {
  private case class CellIndexImpl(xPos: Int, yPos: Int) extends CellIndex {
    assert(xPos >= 1 && xPos <= 9)
    assert(yPos >= 1 && yPos <= 9)
    def rotated: CellIndex = this.copy(xPos = 10 - this.xPos, yPos = 10 - this.yPos)
  }
  def apply(x: Int, y: Int): Option[CellIndex] = Try(CellIndexImpl(x, y)).toOption
}
