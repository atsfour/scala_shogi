package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.controller.ShogiController

import scalafx.geometry.Pos
import scalafx.scene.Group
import scalafx.scene.control.Label
import scalafx.scene.shape.Rectangle

case class InformationSpace(ctrl: ShogiController) {

  def rect = new Rectangle{
    width = mainWidth
    height = infoHeight
    fill = infoFieldColor
    stroke = black
  }

  def label = new Label {
    text = ctrl.infoText
    alignment = Pos.Center
  }

  val element = new Group(rect, label)

}
