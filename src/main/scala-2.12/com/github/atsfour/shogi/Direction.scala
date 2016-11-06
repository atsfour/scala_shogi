package com.github.atsfour.shogi

sealed abstract class Direction(diffX: Int, diffY: Int) {

  def next(player: Side, from: CellIndex): Option[CellIndex] = {
    val (dx, dy) = player match {
      case Sente => (diffX, diffY)
      case Gote => (-diffX, -diffY)
    }
    CellIndex(from.xPos + dx, from.yPos + dy)
  }

}

case object Forward extends Direction(0, -1)
case object Back extends Direction(0, 1)
case object Right extends Direction(-1, 0)
case object Left extends Direction(1, 0)
case object ForwardRight extends Direction(-1, -1)
case object ForwardLeft extends Direction(1, -1)
case object BackRight extends Direction(-1, 1)
case object BackLeft extends Direction(1, 1)
case object KeimaRight extends Direction (-1, -2)
case object KeimaLeft extends Direction (1, -2)
