package com.github.atsfour.shogi

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

case object Kyosha extends KomaKind {
  val label = "香車"
  val nari = Some(NariKyo)
  val movability = Movability.Kyosha
}

case object NariKyo extends KomaKind {
  val label = "成香"
  val nari = None
  val movability = Movability.Kin
}

case object Keima extends KomaKind {
  val label = "桂馬"
  val nari = Some(NariKei)
  val movability = Movability.Keima
}

case object NariKei extends KomaKind {
  val label = "成桂"
  val nari = None
  val movability = Movability.Kin
}

case object Gin extends KomaKind {
  val label = "銀将"
  val nari = Some(NariGin)
  val movability = Movability.Gin
}

case object NariGin extends KomaKind {
  val label = "成銀"
  val nari = None
  val movability = Movability.Kin
}

case object Kin extends KomaKind {
  val label = "金将"
  val nari = None
  val movability = Movability.Kin
}

case object Hisha extends KomaKind {
  val label = "飛車"
  val nari = Some(Ryuou)
  val movability = Movability.Hisha
}

case object Ryuou extends KomaKind {
  val label = "龍王"
  val nari = None
  val movability = Movability.Ryuou
}

case object Kaku extends KomaKind {
  val label = "角行"
  val nari = Some(Ryoma)
  val movability = Movability.Kaku
}

case object Ryoma extends KomaKind {
  val label = "龍馬"
  val nari = None
  val movability = Movability.Ryoma
}

case object Ousho extends KomaKind {
  val label = "王将"
  val nari = None
  val movability = Movability.Gyoku
}

case object Gyoku extends KomaKind {
  val label = "玉将"
  val nari = None
  val movability = Movability.Gyoku
}
