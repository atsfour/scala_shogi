package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.controller.ShogiController

import scalafx.scene.layout.GridPane

case class Board(ctrl: ShogiController) {
  val element = {
    val pane = new GridPane
    ctrl.gameState.board.cellIndices.foreach { cellIndex =>
      val x = 9 - cellIndex.suji
      val y = cellIndex.dan - 1
      val cell = Cell(ctrl, cellIndex, cellSize).element
      pane.add(cell, x, y)
    }
    pane
  }

}
