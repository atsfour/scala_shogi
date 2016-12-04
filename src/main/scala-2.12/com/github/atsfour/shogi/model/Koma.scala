package com.github.atsfour.shogi.model

case class Koma(side: Side, kind: KomaKind) {

  private def theoreticalMovableCells(komaMap: KomaMap, from: CellIndex) = {
    kind.movability.movableCells(komaMap, side, from)
  }

  private[model] def canTheoreticallyMoveTo(komaMap: KomaMap, from: CellIndex, to: CellIndex) =
    theoreticalMovableCells(komaMap, from).contains(to)

  private[model] def movableCells(board: Board, from: CellIndex) = {
    def boardAfterMove(to: CellIndex) = board.moveKoma(from, to)
    theoreticalMovableCells(board.komaMap, from).filter(!boardAfterMove(_).isOute(side))
  }

  private[model] def canMoveTo(board: Board, from: CellIndex, to: CellIndex) =
    movableCells(board, from).contains(to)

  private[model] def puttableCells(board: Board) = {
    val isOute = board.isOute(side)
    def boardAfterPut(cellIndex: CellIndex) = board.updated(cellIndex, Koma(side, Dummy))
    kind.movability.puttableCells(board.komaMap, side).filter(c =>
      !isOute || !boardAfterPut(c).isOute(side)
    )
  }

  private[model] def canPutAt(board: Board, at: CellIndex) = {
    puttableCells(board).contains(at)
  }

  private[model] def nariIfDefined: Koma = kind match {
    case k: NormalKomaKind => this.copy(kind = k.nari.getOrElse(k))
    case _ => this
  }

  private[model] def normalKind: NormalKomaKind = kind match {
    case k: NormalKomaKind => k
    case nk: NariKomaKind => nk.normal
  }
}
