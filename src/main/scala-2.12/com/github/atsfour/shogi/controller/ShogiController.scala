package com.github.atsfour.shogi.controller

import com.github.atsfour.shogi.gui.ShogiBoard
import com.github.atsfour.shogi.model.{Side, NormalKomaKind, GameState, CellIndex}

class ShogiController(gui: ShogiBoard) {

  var selectState: SelectState = NoneSelected
  var gameState: GameState = GameState.initial
  val scene = gui.shogiScene(this)

  def anchorCells: Set[CellIndex] = selectState match {
    case CellSelected(idx) => gameState.movableCellsForKomaAt(idx)
    case OwnKomaSelected(kind, _) => gameState.puttableCellsForKoma(kind)
    case _ => Set()
  }

  def cellClicked(cellIndex: CellIndex) = {
    selectState match {
      case CellSelected(from) => {
        gameState = gameState.moveKoma(from, cellIndex)
        if (gameState.canChooseNari(from, cellIndex)) selectState = ChoosingNari
        else selectState = NoneSelected
      }
      case OwnKomaSelected(kind, _) => {
        gameState = gameState.putKoma(cellIndex, kind)
        selectState = NoneSelected
      }
      case NoneSelected => {
        val isMySideKoma = gameState.board.komaAt(cellIndex).exists(_.side == gameState.teban)
        if (isMySideKoma) selectState = CellSelected(cellIndex)
      }
      case _ => ()
    }
    repaint()
  }
  def ownKomaClicked(kind: NormalKomaKind, side: Side) = {
    selectState match {
      case ChoosingNari => ()
      case _ => {
        if (gameState.teban == side) selectState = OwnKomaSelected(kind, side)
        else NoneSelected
      }
    }
    repaint()
  }

  def repaint(): Unit = {
    scene.content = gui.mainPane(this)
  }

}
