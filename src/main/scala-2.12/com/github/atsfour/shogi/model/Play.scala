package com.github.atsfour.shogi.model

sealed trait Play {

  def play: Board => Board

  private def remove(index: CellIndex): Board => Board = board => {
    board.copy(komaMap = board.komaMap - index)
  }
  private def update(index: CellIndex, koma: Koma): Board => Board = board => {
    board.copy(komaMap = board.komaMap + (index -> koma))
  }

  protected def addOwnKoma(kind: NormalKomaKind): Board => Board = board => {
    val result = board.tebanOwnKoma.updated(kind, board.tebanOwnKoma.getOrElse(kind, 0) + 1)
    if (board.isSente) board.copy(senteOwnKoma = result) else board.copy(goteOwnKoma = result)
  }

  protected def removeOwnKoma(kind: NormalKomaKind): Board => Board = board => {
    val result = board.tebanOwnKoma.updated(kind, board.tebanOwnKoma.getOrElse(kind, 0) - 1)
    if (board.isSente) board.copy(senteOwnKoma = result) else board.copy(goteOwnKoma = result)
  }

  protected def getKomaIfEnemy(index: CellIndex): Board => Board = board => {
    board.komaAt(index) match {
      case Some(k) if k.side == board.teban.enemy => addOwnKoma(k.normalKind).andThen(remove(index))(board)
      case _ => board
    }
  }

  protected def moveKoma(from: CellIndex, to: CellIndex, koma: Koma): Board => Board = remove(from) andThen update(to, koma)

  protected def putKoma(at: CellIndex, koma: Koma): Board => Board = update(at, koma)

  protected def nariIfCan(at: CellIndex, koma: Koma): Board => Board = update(at, koma.nariIfDefined)

  protected def exceedTurn: Board => Board = board => board.copy(turn = board.turn + 1)

}

case class Move(from: CellIndex, to: CellIndex, nariSelect: Boolean) extends Play {
  def play: Board => Board = board => {
    board.komaAt(from) match {
      case Some(k) if k.canMoveTo(board, from, to) => {
        val nari = nariSelect || (board.canNari(from, to, k) && !board.canChooseNari(from, to))
        getKomaIfEnemy(to)
          .andThen(moveKoma(from, to, k))
          .andThen { if(nari) nariIfCan(to, k) else identity }
          .andThen(exceedTurn)(board)
      }
      case _ => board
    }
  }
}

case class Put(at: CellIndex, kind: NormalKomaKind) extends Play {
  def play: Board => Board = board => {
    val hasKoma = board.tebanOwnKoma.getOrElse(kind, 0) >= 1
    val canPut = Koma(board.teban, kind).canPutAt(board, at)
    if (hasKoma && canPut) {
      removeOwnKoma(kind)
        .andThen(putKoma(at, Koma(board.teban, kind)))
        .andThen(exceedTurn)(board)
    }
    else board
  }
}
