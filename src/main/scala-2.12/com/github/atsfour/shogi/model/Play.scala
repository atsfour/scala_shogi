package com.github.atsfour.shogi.model

sealed trait Play {

  def play: GameState => GameState

  protected def addOwnKoma(kind: NormalKomaKind): GameState => GameState = state => {
    val result = state.tebanOwnKoma.updated(kind, state.tebanOwnKoma.getOrElse(kind, 0) + 1)
    if (state.isSente) state.copy(senteOwnKoma = result) else state.copy(goteOwnKoma = result)
  }

  protected def removeOwnKoma(kind: NormalKomaKind): GameState => GameState = state => {
    val result = state.tebanOwnKoma.updated(kind, state.tebanOwnKoma.getOrElse(kind, 0) - 1)
    if (state.isSente) state.copy(senteOwnKoma = result) else state.copy(goteOwnKoma = result)
  }

  protected def getKomaIfEnemy(index: CellIndex): GameState => GameState = state => {
    state.board.komaAt(index) match {
      case Some(k) if k.side == state.teban.enemy => addOwnKoma(k.normalKind)(state.copy(board = state.board.removed(index)))
      case _ => state
    }
  }

  protected def moveKoma(from: CellIndex, to: CellIndex, koma: Koma): GameState => GameState = state => {
    state.copy(board = state.board.removed(from).updated(to, koma))
  }

  protected def putKoma(at: CellIndex, koma: Koma): GameState => GameState = state => {
    state.copy(board = state.board.updated(at, koma))
  }

  protected def nariIfCan(at: CellIndex, koma: Koma): GameState => GameState = state => {
    state.copy(board = state.board.updated(at, koma.nariIfDefined))
  }

  protected def exceedTurn: GameState => GameState = state => state.copy(turn = state.turn + 1)

}

case class Move(from: CellIndex, to: CellIndex, nariSelect: Boolean) extends Play {
  def play: GameState => GameState = state => {
    state.board.komaAt(from) match {
      case Some(k) if k.canMoveTo(state.board, from, to) => {
        val nari = nariSelect || (state.canNari(from, to, k) && !state.canChooseNari(from, to))
        getKomaIfEnemy(to)
          .andThen(moveKoma(from, to, k))
          .andThen { if(nari) nariIfCan(to, k) else identity }
          .andThen(exceedTurn)(state)
      }
      case _ => state
    }
  }
}

case class Put(at: CellIndex, kind: NormalKomaKind) extends Play {
  def play: GameState => GameState = state => {
    val hasKoma = state.tebanOwnKoma.getOrElse(kind, 0) >= 1
    val canPut = Koma(state.teban, kind).canPutAt(state.board, at)
    if (hasKoma && canPut) {
      removeOwnKoma(kind)
        .andThen(putKoma(at, Koma(state.teban, kind)))
        .andThen(exceedTurn)(state)
    }
    else state
  }
}
