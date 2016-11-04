package com.atsfour.shogiai

case class Koma(kind: String, index: Int) {
  val xPos = index % 9
  val yPos = index /9

  def move(to: Int): Koma = this.copy(index = to)
}
