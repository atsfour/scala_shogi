package com.github.atsfour.shogi

import scalafx.scene.paint.Color._

package object gui {

  val stageHeight = 800
  val stageWidth = 800
  val mainWidth = stageWidth
  val mainHeight = stageHeight
  val infoHeight = mainHeight * 0.1
  val rightWidth = mainWidth * 0.3
  val boardSize = mainWidth * 0.7
  val cellSize = boardSize / 9.0
  val ownKomaFiledHeight = cellSize * 4
  val ownKomaFiledWidth = cellSize * 4

  val komaColor = Sienna
  val boardColor = Burlywood
  val selectedColor = LightBlue
  val anchorColor = SlateGray
  val ownKomaSpaceColor = Tan
  val buttonColor = Salmon
  val white = White
  val black = Black

  val infoFieldColor = WhiteSmoke

}
