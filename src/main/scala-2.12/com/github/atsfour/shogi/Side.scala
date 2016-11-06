package com.github.atsfour.shogi

sealed trait Side {
  val enemy = this match {
    case Sente => Gote
    case Gote => Sente
  }
}

case object Sente extends Side
case object Gote extends Side
