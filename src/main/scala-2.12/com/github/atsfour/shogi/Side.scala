package com.github.atsfour.shogi

sealed abstract class Side(val label: String) {
  def enemy = this match {
    case Sente => Gote
    case Gote => Sente
  }
}

case object Sente extends Side("先手")
case object Gote extends Side("後手")
