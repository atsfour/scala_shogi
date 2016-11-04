import com.atsfour.shogiai.{Board, Koma}

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
  var board: Board = Board(List(Koma("歩", 40)))
  var selectedCellIndex: Option[Int] = None

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
    board.cells.zipWithIndex.foreach {
      case (optKoma, index) => {
        val x = index % 9
        val y = index / 9
        pane.add(cellObj(optKoma, index), x, y)
      }
    }
    pane
  }

  def cellObj(komaOpt: Option[Koma], index: Int): Group = {
    val fillColor = if (selectedCellIndex.contains(index)) LightBlue else Burlywood
    val grid = {
      val rect = Rectangle(80, 80, fillColor)
      rect.setStroke(Black)
      rect
    }
    val group = new Group {
      children = List(Some(grid), komaOpt.map(komaObj)).flatten
    }
    group.setOnMouseClicked(e => {
      selectedCellIndex match {
        case Some(num) => {
          board = board.moveKoma(num, index)
          selectedCellIndex = None
        }
        case None => selectedCellIndex = Some(index)
      }
      repaint
    })
    group
  }

  def komaObj(koma: Koma): Group = {
    val komaShape = {
      val poly = Polygon(40, 10, 60, 20, 70, 70, 10, 70, 20, 20)
      poly.setFill(Sienna)
      poly.setStroke(Black)
      poly
    }
    val komaLabel = {
      val label = new Label
      label.setText(koma.kind)
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

  def repaint: Unit = {
    boardScene.content = boardObj(board)
  }
}
