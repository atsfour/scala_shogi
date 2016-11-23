package com.github.atsfour.shogi.gui

import scalafx.geometry.Pos
import scalafx.scene.Group
import scalafx.scene.control.Label
import scalafx.scene.shape.Polygon
import scalafx.scene.paint.Color._
import scalafx.scene.text.Font

case class Koma(size: Double, label: String, rotated: Boolean) {

  private[this] val shape = {
    val vertices = List(50, 5, 80, 20, 90, 90, 10, 90, 20, 20).map(_ / 100.0 * size)
    val poly = Polygon(vertices: _*)
    poly.setFill(Sienna)
    poly.setStroke(Black)
    poly
  }
  private[this] val komaLabel = {
    val width = size * 0.4
    val height = size * 0.6
    val fontWidth = width
    val fontHeight = height / label.length
    val fontSize = fontWidth min fontHeight

    val chars = label.zipWithIndex.map {
      case (char, i) => {
        val label = new Label
        label.setText(char.toString)
        label.setFont(Font("Serif", fontSize))
        label.setMaxSize(fontSize, fontSize)
        label.setLayoutX((size - fontSize) / 2.0)
        label.setLayoutY(size * 0.2 + i * fontHeight)
        label.setAlignment(Pos.TopCenter)
        label
      }
    }
    new Group(chars: _*)
  }
  val element: Group = {
    val group = new Group(shape, komaLabel)
    if (rotated) group.setRotate(180)
    group
  }
}
