package com.github.atsfour.shogi

sealed trait Player

case object Sente extends Player
case object Gote extends Player
