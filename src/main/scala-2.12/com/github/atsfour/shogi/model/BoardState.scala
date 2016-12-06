package com.github.atsfour.shogi.model

case class BoardState(board: Board, turn: Int, senteOwnKoma: Map[NormalKomaKind, Int], goteOwnKoma: Map[NormalKomaKind, Int]) {

  val teban : Side = if (turn % 2 == 1) Sente else Gote

  private[model] val isSente: Boolean = teban == Sente
  private[model] val tebanOwnKoma = if (isSente) senteOwnKoma else goteOwnKoma

  def play(play: Play): BoardState = play.play(this)

  def movableCellsForKomaAt(cellIndex: CellIndex): Set[CellIndex] = {
    board.komaAt(cellIndex).fold(Set[CellIndex]())(k => k.movableCells(board, cellIndex))
  }

  def puttableCellsForKoma(kind: NormalKomaKind): Set[CellIndex] = {
    Koma(teban, kind).puttableCells(board)
  }

  def tebanIsOute: Boolean = board.isOute(teban)

  def tebanIsTsumi: Boolean = {
    val canMove = board.komaMap.exists { case (cell, koma) =>
      koma.side == teban && movableCellsForKomaAt(cell).nonEmpty
    }
    val canPut = tebanOwnKoma.exists { case (kind, num) =>
      num > 0 && puttableCellsForKoma(kind).nonEmpty
    }
    !canMove && !canPut
  }

  def canNari(from: CellIndex, to: CellIndex, koma: Koma): Boolean = {
    val nariDefined = koma.kind match {
      case k: NormalKomaKind => k.nari.isDefined
      case _ => false
    }
    nariDefined && from != to && (from.isEnemySideCell(teban) || to.isEnemySideCell(teban))
  }

  def canChooseNari(from: CellIndex, to: CellIndex): Boolean = {
    board.komaAt(from).exists { koma =>
      val canChooseNarazu = koma.canPutAt(Board.empty, to)
      canNari(from, to, koma) && canChooseNarazu
    }
  }

}

object BoardState {
  val empty = BoardState(Board.empty, 1, Map.empty, Map.empty)
  val initial = BoardState(Board.initial, 1, Map.empty, Map.empty)
}
