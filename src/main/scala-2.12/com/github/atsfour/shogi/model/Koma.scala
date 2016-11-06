package com.github.atsfour.shogi.model

case class Koma(side: Side, kind: KomaKind) {
  def movableCells(board: Board, from: CellIndex) = kind.movability.movableCells(board, side, from)
  def canMoveTo(board: Board, from: CellIndex, to: CellIndex) = movableCells(board, from).contains(to)
}
