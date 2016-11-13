package com.github.atsfour.shogi.model

case class Koma(side: Side, kind: KomaKind) {

  def movableCells(board: Board, from: CellIndex) = kind.movability.movableCells(board, side, from)
  def canMoveTo(board: Board, from: CellIndex, to: CellIndex) = movableCells(board, from).contains(to)
  def canPutAt(board: Board, index: CellIndex) = kind.movability.puttableCells(board, side).contains(index)

  def nariIfDefined: Koma = kind match {
    case k: NormalKomaKind => this.copy(kind = k.nari.getOrElse(k))
    case _ => this
  }

  def normalKind: NormalKomaKind = kind match {
    case k: NormalKomaKind => k
    case nk: NariKomaKind => nk.normal
  }
}
