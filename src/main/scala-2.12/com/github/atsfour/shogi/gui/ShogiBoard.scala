package com.github.atsfour.shogi.gui

import com.github.atsfour.shogi.controller.ShogiController
import com.github.atsfour.shogi.model.{Gote, Sente}

import scalafx.application.JFXApp
import scalafx.scene.layout.BorderPane
import scalafx.scene.{Node, Scene}

trait ShogiBoard {

  def shogiScene(ctrl: ShogiController) = new Scene {
    fill = white
    content = mainPane(ctrl)
  }

  def mainPane(ctrl: ShogiController) = {
    val pane = new BorderPane {
      maxWidth = stageWidth
      maxHeight = stageHeight
      top = InformationSpace(ctrl).element
      right = rightSideNode(ctrl)
      center = Board(ctrl).element
    }
    pane
  }

  def rightSideNode(ctrl: ShogiController): Node = {
    val pane = new BorderPane {
      maxHeight = boardSize
      maxWidth = rightWidth
      top = OwnKomaField(ctrl, Gote, ownKomaFiledHeight, ownKomaFiledWidth).element
      center = ActionField(ctrl).element
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
