package com.github.atsfour.shogi.gui


import com.github.atsfour.shogi.controller.ShogiController
import com.github.atsfour.shogi.model.{Gote, Sente, GameState}

import scalafx.application.JFXApp
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.scene.{Group, Node, Scene}

trait ShogiBoard {

  def shogiScene(ctrl: ShogiController) = new Scene {
    fill = White
    content = mainPane(ctrl)
  }

  def mainPane(ctrl: ShogiController) = {
    val pane = new BorderPane {
      maxWidth = stageWidth
      maxHeight = stageHeight
      top = infoObj(ctrl.gameState)
      right = rightInfoObj(ctrl)
      center = Board(ctrl).element
    }
    pane
  }

  def infoObj(state: GameState): Node = {
    val rect = Rectangle(mainWidth, infoHeight, WhiteSmoke)
    val label = new Label {
      text = state.infoText
      alignment = Pos.Center
    }
    rect.setStroke(Black)
    new Group(rect, label)
  }

  def rightInfoObj(ctrl: ShogiController): Node = {
    val pane = new BorderPane {
      maxHeight = boardSize
      maxWidth = rightWidth
      top = OwnKomaField(ctrl, Gote, ownKomaFiledHeight, ownKomaFiledWidth).element
      bottom = OwnKomaField(ctrl, Sente, ownKomaFiledHeight, ownKomaFiledWidth).element
    }
    pane
  }

}

object ShogiBoard extends ShogiBoard

object Main extends JFXApp {

  val ctrl = new ShogiController(ShogiBoard)

  stage = {
    new JFXApp.PrimaryStage {
      title.value = "atsfour shogi"
      height = stageHeight
      width = stageWidth
      scene = ctrl.scene
    }
  }

}
