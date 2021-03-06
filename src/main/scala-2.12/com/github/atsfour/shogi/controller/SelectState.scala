package com.github.atsfour.shogi.controller

import com.github.atsfour.shogi.model.{CellIndex, NormalKomaKind, Side}

sealed trait SelectState

case class CellSelected(index: CellIndex) extends SelectState
case class OwnKomaSelected(kind: NormalKomaKind, side: Side) extends SelectState
case class ChoosingNari(from: CellIndex, to: CellIndex) extends SelectState
case object NoneSelected extends SelectState
