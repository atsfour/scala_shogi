package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.controller.ShogiController
import com.github.atsfour.shogi.model.CellIndex

import scalafx.scene.layout.GridPane

case class Board(ctrl: ShogiController) {
  val element = {
    val pane = new GridPane
    CellIndex.all.foreach { cellIndex =>
      val x = 9 - cellIndex.suji
      val y = cellIndex.dan - 1
      val cell = Cell(ctrl, cellIndex, cellSize).element
      pane.add(cell, x, y)
    }
    pane
  }

}
