package com.github.atsfour.shogi.model

case class GameState(board: Board, turn: Int, senteOwnKoma: Map[KomaKind, Int], goteOwnKoma: Map[KomaKind, Int]) {

  val teban : Side = if (turn % 2 == 1) Sente else Gote

  val infoText = s"第 ${turn} 手 ${teban.label}の手番です"

  private def addOwnKoma(kind: KomaKind): GameState = teban match {
    case Sente => this.copy(senteOwnKoma = senteOwnKoma.updated(kind, senteOwnKoma.getOrElse(kind, 0) + 1))
    case Gote => this.copy(goteOwnKoma = goteOwnKoma.updated(kind, goteOwnKoma.getOrElse(kind, 0) + 1))
  }

  private def getKomaIfEnemy(index: CellIndex): GameState = {
    board.komaAt(index) match {
      case Some(k) if k.side != teban => this.copy(board = Board(board.komaMap - index)).addOwnKoma(k.kind)
      case _ => this
    }
  }

  def moveKoma(from: CellIndex, to: CellIndex): GameState = {
    board.komaMap.get(from) match {
      case Some(k) if k.canMoveTo(board, from, to) => {
        getKomaIfEnemy(to).copy(board = Board(board.komaMap - from + (to -> k)), turn = turn + 1)
      }
      case _ => this
    }
  }

}

object GameState {
  val initial = GameState(Board.initial, 1, Map.empty, Map.empty)
}
