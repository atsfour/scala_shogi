package com.github.atsfour.shogi.model

case class Board(komaMap: KomaMap, turn: Int, senteOwnKoma: Map[NormalKomaKind, Int], goteOwnKoma: Map[NormalKomaKind, Int]) {

  val teban : Side = if (turn % 2 == 1) Sente else Gote

  private[model] val isSente: Boolean = teban == Sente
  private[model] val tebanOwnKoma = if (isSente) senteOwnKoma else goteOwnKoma

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

  def isTsumi(side: Side): Boolean = {
    val canMove = komaMap.exists { case (cell, koma) =>
      koma.side == side && movableCellsForKomaAt(cell).nonEmpty
    }
    val canPut = tebanOwnKoma.exists { case (kind, num) =>
      num > 0 && puttableCellsForKoma(kind).nonEmpty
    }
    !canMove && !canPut
  }

  def play(play: Play): Board = play.play(this)

  def movableCellsForKomaAt(cellIndex: CellIndex): Set[CellIndex] = {
    komaAt(cellIndex).fold(Set[CellIndex]())(k => k.movableCells(this, cellIndex))
  }

  def puttableCellsForKoma(kind: NormalKomaKind): Set[CellIndex] = {
    Koma(teban, kind).puttableCells(this)
  }

  def canNari(from: CellIndex, to: CellIndex, koma: Koma): Boolean = {
    val nariDefined = koma.kind match {
      case k: NormalKomaKind => k.nari.isDefined
      case _ => false
    }
    nariDefined && from != to && (from.isEnemySideCell(teban) || to.isEnemySideCell(teban))
  }

  def canChooseNari(from: CellIndex, to: CellIndex): Boolean = {
    komaAt(from).exists { koma =>
      val canChooseNarazu = koma.canPutAt(Board.empty, to)
      canNari(from, to, koma) && canChooseNarazu
    }
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

  val empty = Board(Map.empty, 1, Map.empty, Map.empty)
  val initial = Board(initialKomaMap, 1, Map.empty, Map.empty)
}
