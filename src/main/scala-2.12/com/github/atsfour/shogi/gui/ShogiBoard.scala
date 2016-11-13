package com.github.atsfour.shogi.gui


import com.github.atsfour.shogi.model._

import scalafx.application.JFXApp
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Label
import scalafx.scene.layout.{BorderPane, FlowPane, GridPane}
import scalafx.scene.paint.Color._
import scalafx.scene.shape.{Circle, Polygon, Rectangle}
import scalafx.scene.text.Font
import scalafx.scene.{Group, Node, Scene}

object ShogiBoard extends JFXApp {
  var state: GameState = GameState.initial
  var selectState: SelectState = NoneSelected

  val stageHeight = 800
  val stageWidth = 800
  val mainWidth = stageWidth
  val mainHeight = stageHeight
  val infoHeight = mainHeight * 0.05
  val rightWidth = mainWidth * 0.3
  val boardSize = mainWidth * 0.7
  val cellSize = boardSize / 9.0

  val shogiScene = new Scene {
    fill = White
    content = mainPane(state)
  }

  stage = new JFXApp.PrimaryStage {
    title.value = "atsfour shogi"
    width = stageHeight
    height = stageWidth
    scene = shogiScene
  }

  def mainPane(state: GameState) = {
    val pane = new BorderPane {
      maxWidth = stageWidth
      maxHeight = stageHeight
      top = infoObj(state)
      right = rightInfoObj(state)
      center = boardObj(state.board)
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

  def rightInfoObj(state: GameState): Node = {
    val pane = new BorderPane {
      maxHeight = boardSize
      maxWidth = rightWidth
      top = ownKomaObj(state, Gote)
      bottom = ownKomaObj(state, Sente)
    }
    pane
  }

  def ownKomaObj(state: GameState, side: Side) = {
    val width = rightWidth / 3.0
    val height = rightWidth / 4.0
    val ownKomaSize = height

    val ownKomaMap = if (side == Sente) state.senteOwnKoma else state.goteOwnKoma
    val komas: List[Node] = ownKomaMap.filter {
      case (_,  n) => n > 0
    }.map {
      case (kind, num) => {
        val fillColor = selectState match {
          case OwnKomaSelected(k, s) if k == kind && s == side => LightBlue
          case _ => White
        }
        val frame = Rectangle(width, height, fillColor)
        val koma = komaObj(Koma(Sente, kind), ownKomaSize)
        val label = Label(num.toString)
        val group = new Group {
          children = List(frame, koma, label)
        }
        if (side == Gote) group.setRotate(180)
        group.setOnMouseClicked { e =>
          selectState match {
            case ChoosingNari => ()
            case _ => {
              if (state.teban == side) selectState = OwnKomaSelected(kind, side)
              else NoneSelected
            }
          }
          repaint()
        }
        group
      }
    }.toList
    new FlowPane {
      children = komas
    }
  }

  def boardObj(board: Board) = {
    val pane = new GridPane
    val anchorCells: Set[CellIndex] = selectState match {
      case CellSelected(idx) => state.movableCellsForKomaAt(idx)
      case OwnKomaSelected(kind, _) => state.puttableCellsForKoma(kind)
      case _ => Set()
    }
    board.cellIndices.foreach { c =>
      val x = 9 - c.xPos
      val y = c.yPos - 1
      val cell = cellObj(c, anchorCells.contains(c))
      pane.add(cell, x, y)
    }
    val nariSelectDialog: Option[Node] = ???
    new Group(List(pane, nariSelectDialog).flatten:_*)
  }

  def cellObj(cellIndex: CellIndex, anchored: Boolean): Group = {
    val isSelected = selectState match {
      case CellSelected(idx) => cellIndex == idx
      case _ => false
    }
    val fillColor = if (isSelected) LightBlue else Burlywood

    val rect = Rectangle(cellSize, cellSize, fillColor)
    rect.setStroke(Black)
    val koma: Option[Node] = state.board.komaAt(cellIndex).map(k => komaObj(k))
    val anchor: Option[Node] = if (anchored) Some(anchorObj) else None

    val cell = new Group {
      children = List(Some(rect), koma, anchor).flatten
      margin = Insets(-1)
    }

    cell.setOnMouseClicked(e => {
      selectState match {
        case CellSelected(from) => {
          state = state.moveKoma(from, cellIndex)
          if (state.canChooseNari(from, cellIndex)) selectState = ChoosingNari
          else selectState = NoneSelected
        }
        case OwnKomaSelected(kind, _) => {
          state = state.putKoma(cellIndex, kind)
          selectState = NoneSelected
        }
        case NoneSelected => {
          val isMySideKoma = state.board.komaAt(cellIndex).exists(_.side == state.teban)
          if (isMySideKoma) selectState = CellSelected(cellIndex)
        }
        case _ => ()
      }
      repaint()
    })
    cell
  }

  def komaObj(koma: Koma, cellSize: Double = cellSize): Group = {
    val komaShape = {
      val vertices = List(50, 5, 80, 20, 90, 90, 10, 90, 20, 20).map(_ / 100.0 * cellSize)
      val poly = Polygon(vertices :_*)
      poly.setFill(Sienna)
      poly.setStroke(Black)
      poly
    }
    val komaLabel = {
      val text = koma.kind.label
      val width = cellSize * 0.4
      val height = cellSize * 0.6
      val fontWidth = width
      val fontHeight = height / text.length
      val fontSize = fontWidth min fontHeight

      val chars = text.zipWithIndex.map {
        case (char, i) => {
          val label = new Label
          label.setText(char.toString)
          label.setFont(Font("Serif", fontSize))
          label.setMaxSize(fontSize, fontSize)
          label.setLayoutX((cellSize - fontSize) / 2.0)
          label.setLayoutY(cellSize * 0.2 + i * fontHeight)
          label.setAlignment(Pos.TopCenter)
          label
        }
      }
      new Group(chars :_*)
    }
    val obj = new Group(komaShape, komaLabel)
    if (koma.side == Gote) obj.setRotate(180)
    obj
  }

  def anchorObj: Node = Circle(
    cellSize / 2.0,
    cellSize / 2.0,
    cellSize / 5.0,
    SlateGray
  )

  def repaint(): Unit = {
    shogiScene.content = mainPane(state)
  }
}
