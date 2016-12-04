package com.github.atsfour.shogi.controller

import com.github.atsfour.shogi.gui.ShogiBoard
import com.github.atsfour.shogi.model._

class ShogiController(gui: ShogiBoard) {

  var selectState: SelectState = NoneSelected
  var gameState: GameState = GameState.initial
  val scene = gui.shogiScene(this)

  def infoText: String = {
    val tebanInfo = s"第 ${gameState.turn} 手 ${gameState.teban.label}の手番です"
    val selectInfo = selectState match {
      case CellSelected(_) => "移動するマスを選択してください"
      case OwnKomaSelected(_, _) => "駒を打つマスを選択してください"
      case ChoosingNari(_, _) => "成り、不成を選択してください"
      case NoneSelected => "駒を選択してください"
    }
    val outeInfo = if (gameState.tebanIsOute) Some("王手です") else None
    Seq(Some(tebanInfo), Some(selectInfo), outeInfo).flatten.mkString("\n")
  }

  def anchorCells: Set[CellIndex] = selectState match {
    case CellSelected(idx) => gameState.movableCellsForKomaAt(idx)
    case OwnKomaSelected(kind, _) => gameState.puttableCellsForKoma(kind)
    case _ => Set()
  }

  def cellClicked(cellIndex: CellIndex): Unit = {
    selectState match {
      case CellSelected(from) => {
        if (gameState.canChooseNari(from, cellIndex)) {
          selectState = ChoosingNari(from, cellIndex)
        }
        else {
          gameState = gameState.play(Move(from, cellIndex, false))
          cancelSelect
        }
      }
      case OwnKomaSelected(kind, _) => {
        gameState = gameState.play(Put(cellIndex, kind))
        cancelSelect
      }
      case NoneSelected => {
        val isMySideKoma = gameState.board.komaAt(cellIndex).exists(_.side == gameState.teban)
        if (isMySideKoma) selectState = CellSelected(cellIndex)
      }
      case ChoosingNari(_, _) => ()
      case _ => cancelSelect
    }
    repaint()
  }

  def ownKomaClicked(kind: NormalKomaKind, side: Side): Unit = {
    selectState match {
      case ChoosingNari(_, _) => ()
      case _ => {
        if (gameState.teban == side) selectState = OwnKomaSelected(kind, side)
        else cancelSelect
      }
    }
    repaint()
  }

  def nariClicked(nari: Boolean): Unit = {
    selectState match {
      case ChoosingNari(from, to) => {
        gameState = gameState.play(Move(from, to, nari))
      }
      case _ => ()
    }
    cancelSelect
    repaint()
  }

  def cancelSelect: Unit = {
    selectState = NoneSelected
    repaint()
  }

  def repaint(): Unit = {
    scene.content = gui.mainPane(this)
  }

}
