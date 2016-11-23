package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.controller.ShogiController

import scalafx.scene.layout.GridPane

case class Board(ctrl: ShogiController) {
  val element = {
    val pane = new GridPane
    ctrl.gameState.board.cellIndices.foreach { c =>
      val x = 9 - c.xPos
      val y = c.yPos - 1
      val cell = Cell(ctrl, c, cellSize).element
      pane.add(cell, x, y)
    }
    pane
  }

}
