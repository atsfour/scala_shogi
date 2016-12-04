package com.github.atsfour.shogi.model

private[model] case class Board(komaMap: Map[CellIndex, Koma]) {

  private[model] def removed(index: CellIndex) = this.copy(komaMap = komaMap - index)
  private[model] def updated(index: CellIndex, koma: Koma) = this.copy(komaMap = komaMap + (index -> koma))
  private[model] def moveKoma(from: CellIndex, to: CellIndex) = komaAt(from) match {
    case Some(k) => removed(from).updated(to, k)
    case None => this
  }

  def komaAt(cellIndex: CellIndex): Option[Koma] = komaMap.get(cellIndex)

  def isOute(side: Side): Boolean = komaMap.find { case (c, k) =>
    k == Koma(side, Ousho) || k == Koma(side, Gyoku)
  }.exists { case (gc, _) =>
    komaMap.exists { case (cell, koma) => koma.side == side.enemy && koma.canTheoreticallyMoveTo(komaMap, cell, gc) }
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
    val sente = initialSenteKomaMap.flatMap { case ((x, y), kind) => CellIndex(x, y).map(_ -> Koma(Sente, kind))}
    val gote = sente.map { case (cell, koma) => cell.rotated -> Koma(Gote, koma.kind) }.updated(CellIndex(5, 1).get, Koma(Gote, Gyoku))
    sente ++ gote
  }

  private[model] val initial = Board(initialKomaMap)
  private[model] val empty = Board(Map.empty)
}
