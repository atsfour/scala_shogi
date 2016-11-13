package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.model.{NormalKomaKind, Side, CellIndex}

sealed trait SelectState

case class CellSelected(index: CellIndex) extends SelectState
case class OwnKomaSelected(kind: NormalKomaKind, side: Side) extends SelectState
case object ChoosingNari extends SelectState
case object NoneSelected extends SelectState
