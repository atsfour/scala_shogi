package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.controller.{OwnKomaSelected, ShogiController}
import com.github.atsfour.shogi.model.{Gote, Sente, Side}

import scalafx.scene.Group
import scalafx.scene.control.Label
import scalafx.scene.layout.FlowPane
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color._

case class OwnKomaField(ctrl: ShogiController, side: Side, width: Double, height: Double) {

  private[this] val ownKomaSize = height
  private[this] val rotated = side match {
    case Sente => false
    case Gote => true
  }
  private def ownKomaMap = side match {
    case Sente => ctrl.gameState.senteOwnKoma
    case Gote => ctrl.gameState.goteOwnKoma
  }

  private[this] val komas: List[Group] = ownKomaMap.filter {
    case (_,  n) => n > 0
  }.map {
    case (kind, num) => {
      val isSelected = ctrl.selectState match {
        case OwnKomaSelected(k, s) if k == kind && s == side => true
        case _ => false
      }
      val fillColor = if(isSelected) LightBlue else White
      val frame = Rectangle(width, height, fillColor)
      val koma = Koma(ownKomaSize, kind.label, rotated)
      val numLabel = Label(num.toString)

      new Group {
        children = List(frame, koma.element, numLabel)
        rotate = if (rotated) 180 else 0
        onMouseClicked = { e => ctrl.ownKomaClicked(kind, side) }
      }
    }
  }.toList

  val element = new FlowPane {
    children = komas
  }

}
