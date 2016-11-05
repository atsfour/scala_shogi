
import com.github.atsfour.shogi._

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Label
import scalafx.scene.text.Font
import scalafx.scene.{Node, Group, Scene}
import scalafx.scene.layout.GridPane
import scalafx.scene.paint.Color._
import scalafx.scene.shape.{Circle, Polygon, Rectangle}

object ShogiBoard extends JFXApp {
  var board: Board = Board.initialBoard
  var selectedCellIndex: Option[CellIndex] = None

  val stageHeight = 800
  val stageWidth = 800
  val boardSize = (stageHeight min stageWidth) * 0.9
  val boardMargin = Insets(boardSize * 0.05)
  val cellSize = boardSize / 9.0

  val boardScene = new Scene {
    fill = White
    content = boardObj(board)
  }

  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Scala Shogi"
    width = stageHeight
    height = stageWidth
    scene = boardScene
  }

  def boardObj(board: Board) = {
    val pane = new GridPane
    val anchorCells = selectedCellIndex.fold(Set.empty[CellIndex])(board.movableCellsForKomaAt)
    board.cellIndices.foreach { c =>
      val x = 9 - c.xPos
      val y = c.yPos - 1
      pane.add(cellObj(c, anchorCells.contains(c)), x, y)
    }
    pane
  }

  def cellObj(cellIndex: CellIndex, anchored: Boolean): Group = {
    val fillColor = if (selectedCellIndex.contains(cellIndex)) LightBlue else Burlywood

    val rect = Rectangle(cellSize, cellSize, fillColor)
    rect.setStroke(Black)
    val koma: Option[Node] = board.komaAt(cellIndex).map(k => komaObj(k))
    val anchor: Option[Node] = if (anchored) Some(anchorObj) else None

    val cell = new Group {
      children = List(Some(rect), koma, anchor).flatten
    }

    cell.setOnMouseClicked(e => {
      selectedCellIndex match {
        case Some(from) => {
          board = board.moveKoma(from, cellIndex)
          selectedCellIndex = None
        }
        case None => selectedCellIndex = Some(cellIndex)
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
    obj
  }

  def anchorObj: Node = Circle(
    cellSize / 2.0,
    cellSize / 2.0,
    cellSize / 5.0,
    SlateGray
  )

  def repaint(): Unit = {
    boardScene.content = boardObj(board)
  }
}
