package com.github.atsfour.shogi

case class Koma(player: Player, kind: KomaKind) {
  def movableCells(board: Board, from: CellIndex) = kind.movability.movableCells(board, player, from)
  def canMoveTo(board: Board, from: CellIndex, to: CellIndex) = movableCells(board, from).contains(to)
}
