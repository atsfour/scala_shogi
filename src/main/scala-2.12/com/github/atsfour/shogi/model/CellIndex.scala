package com.github.atsfour.shogi.model

import scala.util.Try

trait CellIndex {
  val suji: Int
  val dan: Int
  private def side: Option[Side] = dan match {
    case i if i >= 7 => Some(Sente)
    case i if i <= 3 => Some(Gote)
    case _ => None
  }

  /**
    * get rotated cell index by 180 degrees
    * @return rotated cell index
    */
  def rotated: CellIndex

  def isEnemySideCell(selfSide: Side): Boolean = side.contains(selfSide.enemy)
}

object CellIndex {
  private case class CellIndexImpl(suji: Int, dan: Int) extends CellIndex {
    assert(suji >= 1 && suji <= 9)
    assert(dan >= 1 && dan <= 9)
    def rotated: CellIndex = this.copy(suji = 10 - this.suji, dan = 10 - this.dan)
  }

  // to avoid assertion error, it returns option
  def apply(x: Int, y: Int): Option[CellIndex] = Try(CellIndexImpl(x, y)).toOption
}
