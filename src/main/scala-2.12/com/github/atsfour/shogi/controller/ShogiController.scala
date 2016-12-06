package com.github.atsfour.shogi.controller

import com.github.atsfour.shogi.gui.ShogiBoard
import com.github.atsfour.shogi.model._

class ShogiController(gui: ShogiBoard) {

  var selectState: SelectState = NoneSelected
  var board: Board = Board.initial
  val scene = gui.shogiScene(this)

  def infoText: String = {
    val tebanInfo = s"第 ${board.turn} 手 ${board.teban.label}の手番です"
    val selectInfo = selectState match {
      case CellSelected(_) => "移動するマスを選択してください"
      case OwnKomaSelected(_, _) => "駒を打つマスを選択してください"
      case ChoosingNari(_, _) => "成り、不成を選択してください"
      case NoneSelected => "駒を選択してください"
    }
    val outeInfo = {
      if (board.isTsumi(board.teban)) Some(s"詰みです。${board.teban.enemy.label}の勝利です")
      else if (board.isOute(board.teban)) Some("王手です")
      else  None
    }
    Seq(Some(tebanInfo), Some(selectInfo), outeInfo).flatten.mkString("\n")
  }

  def anchorCells: Set[CellIndex] = selectState match {
    case CellSelected(idx) => board.movableCellsForKomaAt(idx)
    case OwnKomaSelected(kind, _) => board.puttableCellsForKoma(kind)
    case _ => Set()
  }

  def initialize: Unit = {
    board = Board.initial
    cancelSelect()
    repaint()
  }

  def cellClicked(cellIndex: CellIndex): Unit = {
    selectState match {
      case CellSelected(from) => {
        if (board.canChooseNari(from, cellIndex)) {
          selectState = ChoosingNari(from, cellIndex)
          repaint()
        }
        else {
          board = board.play(Move(from, cellIndex, false))
          cancelSelect()
          repaint()
        }
      }
      case OwnKomaSelected(kind, _) => {
        board = board.play(Put(cellIndex, kind))
        cancelSelect()
        repaint()
      }
      case NoneSelected => {
        val isMySideKoma = board.komaAt(cellIndex).exists(_.side == board.teban)
        val isMovable = board.movableCellsForKomaAt(cellIndex).nonEmpty
        if (isMySideKoma && isMovable) selectState = CellSelected(cellIndex)
        repaint()
      }
      case ChoosingNari(_, _) => ()
      case _ => {
        cancelSelect()
        repaint()
      }
    }
  }

  def ownKomaClicked(kind: NormalKomaKind, side: Side): Unit = {
    selectState match {
      case ChoosingNari(_, _) => ()
      case _ => {
        if (board.teban == side) selectState = OwnKomaSelected(kind, side)
        else cancelSelect()
        repaint()
      }
    }
  }

  def nariClicked(nari: Boolean): Unit = {
    selectState match {
      case ChoosingNari(from, to) => {
        board = board.play(Move(from, to, nari))
        cancelSelect()
        repaint()
      }
      case _ => ()
    }
  }

  def cancelSelect(): Unit = {
    selectState = NoneSelected
  }

  def repaint(): Unit = {
    scene.content = gui.mainPane(this)
  }

}
