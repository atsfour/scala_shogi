package com.atsfour.shogi

sealed trait KomaKind {
  val label: String
  val nari: Option[KomaKind]
  val movability: Movability
}

case object Fu extends KomaKind {
  val label = "歩兵"
  val nari = Some(Tokin)
  val movability = Movability.Fu
}

case object Tokin extends KomaKind {
  val label = "と"
  val nari = None
  val movability = Movability.Kin
}
