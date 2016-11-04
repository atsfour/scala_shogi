import com.atsfour.shogi._

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.text.Font
import scalafx.scene.{Group, Scene}
import scalafx.scene.layout.GridPane
import scalafx.scene.paint.Color._
import scalafx.scene.shape.{Polygon, Rectangle}

object ShogiBoard extends JFXApp {
  var board: Board = Board.initialBoard
  var selectedCellIndex: Option[CellIndex] = None

  val boardScene = new Scene {
    fill = White
    content = boardObj(board)
  }

  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Scala Shogi"
    width = 800
    height = 800
    scene = boardScene
  }

  def boardObj(board: Board) = {
    val pane = new GridPane
    board.cellIndices.foreach { c =>
      val x = 9 - c.xPos
      val y = c.yPos - 1
      pane.add(cellObj(c), x, y)
    }
    pane
  }

  def cellObj(cellIndex: CellIndex): Group = {
    val fillColor = if (selectedCellIndex.contains(cellIndex)) LightBlue else Burlywood
    val grid = {
      val rect = Rectangle(80, 80, fillColor)
      rect.setStroke(Black)
      rect
    }
    val group = new Group {
      children = List(Some(grid), board.komaMap.get(cellIndex).map(k => komaObj(k.kind))).flatten
    }
    group.setOnMouseClicked(e => {
      selectedCellIndex match {
        case Some(from) => {
          board = board.moveKoma(from, cellIndex)
          selectedCellIndex = None
        }
        case None => selectedCellIndex = Some(cellIndex)
      }
      repaint
    })
    group
  }

  def komaObj(kind: KomaKind): Group = {
    val komaShape = {
      val poly = Polygon(40, 10, 60, 20, 70, 70, 10, 70, 20, 20)
      poly.setFill(Sienna)
      poly.setStroke(Black)
      poly
    }
    val komaLabel = {
      val label = new Label
      label.setText(kind.label)
      label.setFont(Font(30))
      label.setMaxSize(30, 30)
      label.setLayoutX(25)
      label.setLayoutY(25)
      label.setAlignment(Pos.Center)
      label
    }
    val obj = new Group(komaShape, komaLabel)
    obj
  }

  def repaint(): Unit = {
    boardScene.content = boardObj(board)
  }
}
