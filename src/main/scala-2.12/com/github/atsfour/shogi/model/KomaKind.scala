package com.github.atsfour.shogi.model

sealed trait KomaKind {
  val label: String
  val movability: Movability
}

sealed trait NormalKomaKind extends KomaKind {
  val nari: Option[NariKomaKind]
}

sealed trait NariKomaKind extends KomaKind {
  val normal: NormalKomaKind
}

case object Fu extends NormalKomaKind {
  val label = "歩兵"
  val movability = Movability.Fu
  val nari = Some(Tokin)
}

case object Tokin extends NariKomaKind {
  val label = "と"
  val movability = Movability.Kin
  val normal = Fu
}

case object Kyosha extends NormalKomaKind {
  val label = "香車"
  val movability = Movability.Kyosha
  val nari = Some(NariKyo)
}

case object NariKyo extends NariKomaKind {
  val label = "成香"
  val movability = Movability.Kin
  val normal = Kyosha
}

case object Keima extends NormalKomaKind {
  val label = "桂馬"
  val nari = Some(NariKei)
  val movability = Movability.Keima
}

case object NariKei extends NariKomaKind {
  val label = "成桂"
  val movability = Movability.Kin
  val normal = Keima
}

case object Gin extends NormalKomaKind {
  val label = "銀将"
  val nari = Some(NariGin)
  val movability = Movability.Gin
}

case object NariGin extends NariKomaKind {
  val label = "成銀"
  val movability = Movability.Kin
  val normal = Gin
}

case object Kin extends NormalKomaKind {
  val label = "金将"
  val nari = None
  val movability = Movability.Kin
}

case object Hisha extends NormalKomaKind {
  val label = "飛車"
  val nari = Some(Ryuou)
  val movability = Movability.Hisha
}

case object Ryuou extends NariKomaKind {
  val label = "龍王"
  val movability = Movability.Ryuou
  val normal = Hisha
}

case object Kaku extends NormalKomaKind {
  val label = "角行"
  val nari = Some(Ryoma)
  val movability = Movability.Kaku
}

case object Ryoma extends NariKomaKind {
  val label = "龍馬"
  val movability = Movability.Ryoma
  val normal = Kaku
}

case object Ousho extends NormalKomaKind {
  val label = "王将"
  val nari = None
  val movability = Movability.Gyoku
}

case object Gyoku extends NormalKomaKind {
  val label = "玉将"
  val nari = None
  val movability = Movability.Gyoku
}

case object Dummy extends NormalKomaKind {
  val label = ""
  val nari = None
  val movability = Movability.Dummy
}
