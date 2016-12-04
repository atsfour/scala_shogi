package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.controller.{ChoosingNari, ShogiController}

import scalafx.scene.Group
import scalafx.scene.control.Label
import scalafx.scene.layout.FlowPane
import scalafx.scene.shape.Rectangle

case class ActionField(ctrl: ShogiController) {

  private[this] def defaultButton(buttonText: String, action: => Unit): Group = {
    val label = new Label(buttonText)
    val rect = new Rectangle {
      width = 40
      height = 20
      stroke = black
      fill = buttonColor
    }
    new Group {
      onMouseClicked = { _ => action }
      children = Seq(rect, label)
    }
  }

  private[this] def nariButton = defaultButton("成り", ctrl.nariClicked(true))
  private[this] def narazuButton = defaultButton("不成", ctrl.nariClicked(false))

  private[this] def buttons = ctrl.selectState match {
    case ChoosingNari(_, _) => Seq(nariButton, narazuButton)
    case _ => Seq()
  }

  val element = new FlowPane {
    children = buttons
  }

}
