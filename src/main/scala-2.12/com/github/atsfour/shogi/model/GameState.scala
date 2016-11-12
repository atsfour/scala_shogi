package com.github.atsfour.shogi.model

case class GameState(board: Board, turn: Int, senteOwnKoma: Map[KomaKind, Int], goteOwnKoma: Map[KomaKind, Int]) {

  val teban : Side = if (turn % 2 == 1) Sente else Gote
  val infoText = s"第 ${turn} 手 ${teban.label}の手番です"

  private val isSente: Boolean = teban == Sente
  private val tebanOwnKoma = if (isSente) senteOwnKoma else goteOwnKoma


  private def addOwnKoma(kind: KomaKind): GameState = {
    val result = tebanOwnKoma.updated(kind, tebanOwnKoma.getOrElse(kind, 0) + 1)
    if (isSente) this.copy(senteOwnKoma = result) else this.copy(goteOwnKoma = result)
  }

  private def removeOwnKoma(kind: KomaKind): GameState = {
    val result = tebanOwnKoma.updated(kind, tebanOwnKoma.getOrElse(kind, 0) - 1)
    if (isSente) this.copy(senteOwnKoma = result) else this.copy(goteOwnKoma = result)
  }

  private def exceedTurn: GameState = this.copy(turn = turn + 1)

  private def getKomaIfEnemy(index: CellIndex): GameState = {
    board.komaAt(index) match {
      case Some(k) if k.side != teban => this.copy(board = board.removed(index)).addOwnKoma(k.kind)
      case _ => this
    }
  }

  def moveKoma(from: CellIndex, to: CellIndex): GameState = {
    board.komaMap.get(from) match {
      case Some(k) if k.canMoveTo(board, from, to) => {
        getKomaIfEnemy(to).copy(board = board.removed(from).updated(to, k)).exceedTurn
      }
      case _ => this
    }
  }

  def putKoma(at: CellIndex, kind: KomaKind): GameState = {
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

  def puttableCellsForKoma(kind: KomaKind): Set[CellIndex] = {
    kind.movability.puttableCells(board, teban)
  }

}

object GameState {
  val initial = GameState(Board.initial, 1, Map.empty, Map.empty)
}
