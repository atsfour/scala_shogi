package com.github.atsfour.shogi

case class GameState(board: Board, teban: Side) {

  def moveKoma(from: CellIndex, to: CellIndex): GameState = {
    board.komaMap.get(from) match {
      case Some(k) if k.canMoveTo(board, from, to) => GameState(Board(board.komaMap - from + (to -> k)), teban.enemy)
      case _ => this
    }
  }

}

object GameState {
  val initial = GameState(Board.initial, Sente)
}
