package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.controller.{OwnKomaSelected, ShogiController}
import com.github.atsfour.shogi.model.{Gote, Sente, Side}

import scalafx.scene.Group
import scalafx.scene.control.Label
import scalafx.scene.layout.FlowPane
import scalafx.scene.shape.Rectangle

case class OwnKomaField(ctrl: ShogiController, side: Side, fieldWidth: Double, fieldHeight: Double) {

  private[this] val rotated = side match {
    case Sente => false
    case Gote => true
  }
  private def ownKomaMap = side match {
    case Sente => ctrl.board.senteOwnKoma
    case Gote => ctrl.board.goteOwnKoma
  }

  private[this] val komas: List[Group] = ownKomaMap.filter {
    case (_,  n) => n > 0
  }.map {
    case (kind, num) => {
      val isSelected = ctrl.selectState match {
        case OwnKomaSelected(k, s) if k == kind && s == side => true
        case _ => false
      }
      val fillColor = if(isSelected) selectedColor else ownKomaSpaceColor
      val frame = Rectangle(cellSize, cellSize, fillColor)
      val koma = Koma(cellSize, kind.label, Sente)
      val numLabel = Label(num.toString)

      new Group {
        children = List(frame, koma.element, numLabel)
        rotate = if (rotated) 180 else 0
        onMouseClicked = { e => ctrl.ownKomaClicked(kind, side) }
      }
    }
  }.toList

  private[this] val pane = new FlowPane {
    minHeight = fieldHeight
    maxHeight = fieldHeight
    minWidth = fieldWidth
    maxWidth = fieldWidth
    children = komas
  }

  private[this] val rect = new Rectangle {
    height = fieldHeight
    width = fieldWidth
    stroke = white
    fill = ownKomaSpaceColor
  }

  val element = new Group{
    children = Seq(rect, pane)
  }

}
