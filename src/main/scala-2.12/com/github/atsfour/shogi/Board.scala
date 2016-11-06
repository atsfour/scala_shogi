package com.github.atsfour.shogi

case class Board(komaMap: Map[CellIndex, Koma]) {

  val cellIndices: Seq[CellIndex] = for {
    i <- 1 to 9
    j <- 1 to 9
  } yield CellIndex(i, j).get

  def komaAt(cellIndex: CellIndex): Option[Koma] = komaMap.get(cellIndex)
  def movableCellsForKomaAt(cellIndex: CellIndex): Set[CellIndex] = {
    komaAt(cellIndex).fold(Set[CellIndex]())(k => k.movableCells(this, cellIndex))
  }

}

object Board {
  private val initialSenteKomaMap: Map[(Int, Int), KomaKind] = {
    val fuList = (1 to 9).map(i => (i, 7) -> Fu).toList
    val other = List(
      (2, 8) -> Hisha, (8, 8) -> Kaku,
      (1 ,9) -> Kyosha, (2 ,9) -> Keima, (3 ,9) -> Gin,  (4 ,9) -> Kin,  (5 ,9) -> Ousho,
      (6 ,9) -> Kin, (7 ,9) -> Gin, (8 ,9) -> Keima,  (9 ,9) -> Kyosha
    )
    Map(fuList ++ other :_*)
  }
  private val initialKomaMap = {
    val sente = initialSenteKomaMap.map { case ((x, y), kind) => CellIndex(x, y).get -> Koma(Sente, kind)}
    val gote = sente.map { case (cell, koma) => cell.rotated -> Koma(Gote, koma.kind) }
    sente ++ gote
  }
  val initial= Board(initialKomaMap)
}
