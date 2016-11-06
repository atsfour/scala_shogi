package com.github.atsfour.shogi

sealed trait Side
case object Sente extends Side
case object Gote extends Side
