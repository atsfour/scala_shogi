package com.github.atsfour.shogi

sealed abstract class Movability(aroundDirections: Set[Direction], straightDirections: Set[Direction] = Set.empty) {

  def movableCells(board: Board, player: Player, from: CellIndex): Set[CellIndex] = {

    val aroundCells = aroundDirections.flatMap(_.next(player, from)).filterNot(c => board.komaMap.get(c).exists(_.player == player))

    val straightCells = {
      def go(acc: Set[CellIndex], current: CellIndex, direction: Direction): Set[CellIndex] = direction.next(player, current) match {
        case None => acc
        case Some(next) => board.komaMap.get(next) match {
          case Some(k) if k.player == player => acc
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

  case object Fu extends Movability(Set(Forward))

  case object Kyosha extends Movability(Set.empty, Set(Forward))

  case object Keima extends Movability(Set(KeimaLeft, KeimaRight))

  case object Gin extends Movability(Set(Forward, ForwardLeft, ForwardRight, BackLeft, BackRight))

  case object Kin extends Movability(Set(Forward, ForwardLeft, ForwardRight, Left, Right, Back))

  case object Hisha extends Movability(Set.empty, Set(Forward, Left, Right, Back))

  case object Ryuou extends Movability(Set(ForwardLeft, ForwardRight, BackLeft, BackRight), Set(Forward, Left, Right, Back))

  case object Kaku extends Movability(Set.empty, Set(ForwardLeft, ForwardRight, BackLeft, BackRight))

  case object Ryoma extends Movability(Set(Forward, Left, Right, Back), Set(ForwardLeft, ForwardRight, BackLeft, BackRight))

  case object Gyoku extends Movability(Set(Forward, ForwardLeft, ForwardRight, Left, Right, Back, BackLeft, BackRight))

}
