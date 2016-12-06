package com.github.atsfour.shogi.controller

import com.github.atsfour.shogi.gui.ShogiBoard
import com.github.atsfour.shogi.model._

class ShogiController(gui: ShogiBoard) {

  var selectState: SelectState = NoneSelected
  var boardState: BoardState = BoardState.initial
  val scene = gui.shogiScene(this)

  def infoText: String = {
    val tebanInfo = s"第 ${boardState.turn} 手 ${boardState.teban.label}の手番です"
    val selectInfo = selectState match {
      case CellSelected(_) => "移動するマスを選択してください"
      case OwnKomaSelected(_, _) => "駒を打つマスを選択してください"
      case ChoosingNari(_, _) => "成り、不成を選択してください"
      case NoneSelected => "駒を選択してください"
    }
    val outeInfo = {
      if (boardState.tebanIsTsumi) Some(s"詰みです。${boardState.teban.enemy.label}の勝利です")
      else if (boardState.tebanIsOute) Some("王手です")
      else  None
    }
    Seq(Some(tebanInfo), Some(selectInfo), outeInfo).flatten.mkString("\n")
  }

  def anchorCells: Set[CellIndex] = selectState match {
    case CellSelected(idx) => boardState.movableCellsForKomaAt(idx)
    case OwnKomaSelected(kind, _) => boardState.puttableCellsForKoma(kind)
    case _ => Set()
  }

  def initialize: Unit = {
    boardState = BoardState.initial
    cancelSelect()
    repaint()
  }

  def cellClicked(cellIndex: CellIndex): Unit = {
    selectState match {
      case CellSelected(from) => {
        if (boardState.canChooseNari(from, cellIndex)) {
          selectState = ChoosingNari(from, cellIndex)
          repaint()
        }
        else {
          boardState = boardState.play(Move(from, cellIndex, false))
          cancelSelect()
          repaint()
        }
      }
      case OwnKomaSelected(kind, _) => {
        boardState = boardState.play(Put(cellIndex, kind))
        cancelSelect()
        repaint()
      }
      case NoneSelected => {
        val isMySideKoma = boardState.board.komaAt(cellIndex).exists(_.side == boardState.teban)
        val isMovable = boardState.movableCellsForKomaAt(cellIndex).nonEmpty
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
        if (boardState.teban == side) selectState = OwnKomaSelected(kind, side)
        else cancelSelect()
        repaint()
      }
    }
  }

  def nariClicked(nari: Boolean): Unit = {
    selectState match {
      case ChoosingNari(from, to) => {
        boardState = boardState.play(Move(from, to, nari))
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
