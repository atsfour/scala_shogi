package com.github.atsfour.shogi.model

import com.github.atsfour.shogi.model.{Fu => FuKind}

private[model] sealed abstract class Movability(aroundDirections: Set[Direction], straightDirections: Set[Direction] = Set.empty) {

  private[model] def puttableCells(komaMap: KomaMap, side: Side): Set[CellIndex] = {
    CellIndex.all.toSet.filter { c =>
      komaMap.get(c).isEmpty && movableCells(Map.empty, side, c).nonEmpty
    }
  }

  private[model] def movableCells(komaMap: KomaMap, side: Side, from: CellIndex): Set[CellIndex] = {

    val aroundCells = aroundDirections.flatMap(_.next(side, from)).filterNot(c => komaMap.get(c).exists(_.side == side))

    val straightCells = {
      def go(acc: Set[CellIndex], current: CellIndex, direction: Direction): Set[CellIndex] = direction.next(side, current) match {
        case None => acc
        case Some(next) => komaMap.get(next) match {
          case Some(k) if k.side == side => acc
          case Some(k) => acc + next
          case None => go(acc + next, next, direction)
        }
      }
      straightDirections.flatMap(d => go(Set(), from, d))
    }

    aroundCells ++ straightCells
  }
}

object Movability {

  case object Fu extends Movability(Set(Forward)) {
    // forbid nifu
    private[model] override def puttableCells(komaMap: Map[CellIndex, Koma], side: Side): Set[CellIndex] =
      super.puttableCells(komaMap, side).filterNot { idx =>
        komaMap.exists{ case (idxx, koma) => koma.kind == FuKind && koma.side == side && idx.suji == idxx.suji }
      }
  }

  case object Kyosha extends Movability(Set.empty, Set(Forward))

  case object Keima extends Movability(Set(KeimaLeft, KeimaRight))

  case object Gin extends Movability(Set(Forward, ForwardLeft, ForwardRight, BackLeft, BackRight))

  case object Kin extends Movability(Set(Forward, ForwardLeft, ForwardRight, Left, Right, Back))

  case object Hisha extends Movability(Set.empty, Set(Forward, Left, Right, Back))

  case object Ryuou extends Movability(Set(ForwardLeft, ForwardRight, BackLeft, BackRight), Set(Forward, Left, Right, Back))

  case object Kaku extends Movability(Set.empty, Set(ForwardLeft, ForwardRight, BackLeft, BackRight))

  case object Ryoma extends Movability(Set(Forward, Left, Right, Back), Set(ForwardLeft, ForwardRight, BackLeft, BackRight))

  case object Gyoku extends Movability(Set(Forward, ForwardLeft, ForwardRight, Left, Right, Back, BackLeft, BackRight))

  // for guard koma
  case object Dummy extends Movability(Set(), Set())

}
