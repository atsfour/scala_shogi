package com.github.atsfour.shogi.model

case class GameState(board: Board, turn: Int, senteOwnKoma: Map[NormalKomaKind, Int], goteOwnKoma: Map[NormalKomaKind, Int]) {

  val teban : Side = if (turn % 2 == 1) Sente else Gote

  private val isSente: Boolean = teban == Sente
  private val tebanOwnKoma = if (isSente) senteOwnKoma else goteOwnKoma


  private def addOwnKoma(kind: NormalKomaKind): GameState = {
    val result = tebanOwnKoma.updated(kind, tebanOwnKoma.getOrElse(kind, 0) + 1)
    if (isSente) this.copy(senteOwnKoma = result) else this.copy(goteOwnKoma = result)
  }

  private def removeOwnKoma(kind: NormalKomaKind): GameState = {
    val result = tebanOwnKoma.updated(kind, tebanOwnKoma.getOrElse(kind, 0) - 1)
    if (isSente) this.copy(senteOwnKoma = result) else this.copy(goteOwnKoma = result)
  }

  private def getKomaIfEnemy(index: CellIndex): GameState = {
    board.komaAt(index) match {
      case Some(k) if k.side != teban => this.copy(board = board.removed(index)).addOwnKoma(k.normalKind)
      case _ => this
    }
  }

  private def exceedTurn: GameState = this.copy(turn = turn + 1)

  private def canNari(from: CellIndex, to: CellIndex, koma: Koma): Boolean = {
    val nariDefined = koma.kind match {
      case k: NormalKomaKind => k.nari.isDefined
      case _ => false
    }
    nariDefined && (from.isEnemySideCell(teban) || to.isEnemySideCell(teban))
  }

  private def nariIfCan(at: CellIndex, koma: Koma): GameState = {
    this.copy(board = board.updated(at, koma.nariIfDefined))
  }

  def canChooseNari(from: CellIndex, to: CellIndex): Boolean = {
    board.komaAt(from).exists { koma =>
      val canChooseNarazu = koma.canPutAt(Board.empty, to)
      canNari(from, to, koma) && canChooseNarazu
    }
  }

  def tebanIsOute: Boolean = board.isOute(teban)

  def playMoveKoma(from: CellIndex, to: CellIndex, nariSelect: Boolean): GameState = {
    board.komaAt(from) match {
      case Some(k) if k.canMoveTo(board, from, to) => {
        val nari = nariSelect || (canNari(from, to, k) && !canChooseNari(from, to))
        val moved = getKomaIfEnemy(to).copy(board = board.removed(from).updated(to, k))
        val result = if (nari) moved.nariIfCan(to, k) else moved
        result.exceedTurn
      }
      case _ => this
    }
  }

  def playPutKoma(at: CellIndex, kind: NormalKomaKind): GameState = {
    val hasKoma = tebanOwnKoma.getOrElse(kind, 0) >= 1
    val canPut = Koma(teban, kind).canPutAt(board, at)
    if (hasKoma && canPut) {
      this.copy(board = board.updated(at, Koma(teban, kind))).removeOwnKoma(kind).exceedTurn
    }
    else this
  }

  def movableCellsForKomaAt(cellIndex: CellIndex): Set[CellIndex] = {
    board.komaAt(cellIndex).fold(Set[CellIndex]())(k => k.movableCells(board, cellIndex))
  }

  def puttableCellsForKoma(kind: NormalKomaKind): Set[CellIndex] = {
    kind.movability.puttableCells(board, teban)
  }

}

object GameState {
  val initial = GameState(Board.initial, 1, Map.empty, Map.empty)
}
