package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.controller.{ShogiController, NoneSelected, ChoosingNari, CellSelected}
import com.github.atsfour.shogi.model.{Gote, CellIndex}

import scalafx.geometry.Insets
import scalafx.scene.{Group, Node}
import scalafx.scene.shape.{Circle, Rectangle}

case class Cell(ctrl: ShogiController, cellIndex: CellIndex, cellSize: Double) {

  private[this] val fillColor = if (isSelected) selectedColor else boardColor
  private[this] val rect = new Rectangle{
    width = cellSize
    height = cellSize
    fill = fillColor
    stroke = black
  }

  private[this] val anchorElement = new Circle {
    centerX = cellSize / 2.0
    centerY =  cellSize / 2.0
    radius = cellSize / 5.0
    fill = anchorColor
  }

  val element = new Group {
    children = List(Some(rect), koma.map(_.element), anchor).flatten
    margin = Insets(-1)
    onMouseClicked = { e => ctrl.cellClicked(cellIndex) }
  }

  def anchor: Option[Node] = if (anchored) Some(anchorElement) else None

  def isSelected = ctrl.selectState == CellSelected(cellIndex)

  def koma = ctrl.gameState.board.komaAt(cellIndex).map(k => Koma(cellSize, k.kind.label, k.side == Gote))

  def anchored = ctrl.anchorCells.contains(cellIndex)

}
