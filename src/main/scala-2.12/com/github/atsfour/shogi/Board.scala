package com.github.atsfour.shogi

case class Board(komaMap: Map[CellIndex, Koma]) {

  val cellIndices: Seq[CellIndex] = for {
    i <- 1 to 9
    j <- 1 to 9
  } yield CellIndex(i, j).get

  def moveKoma(from: CellIndex, to: CellIndex): Board = {
    komaMap.get(from) match {
      case Some(k) => Board(komaMap - from + (to -> k))
      case None => this
    }
  }

  def komaAt(cellIndex: CellIndex): Option[Koma] = komaMap.get(cellIndex)
  def movableCellsForKomaAt(cellIndex: CellIndex): Set[CellIndex] = {
    komaAt(cellIndex).fold(Set[CellIndex]())(k => k.movableCells(this, cellIndex))
  }

}

object Board {
  val initialBoard = Board(Map(CellIndex(4, 4).get -> Koma(Sente, Tokin)))
}
