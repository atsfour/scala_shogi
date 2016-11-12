package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.model.{Side, KomaKind, CellIndex}

sealed trait SelectState

case class CellSelected(index: CellIndex) extends SelectState
case class OwnKomaSelected(kind: KomaKind, side: Side) extends SelectState
case object NoneSelected extends SelectState
