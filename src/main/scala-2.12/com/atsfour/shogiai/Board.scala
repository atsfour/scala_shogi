package com.atsfour.shogiai

case class Board(komas: List[Koma]) {

  val cellIndice = (0 until 81).toList
  val cells: List[Option[Koma]] = cellIndice.map { n =>
    komas.find(_.index == n)
  }

  def moveKoma(from: Int, to: Int): Board = komas.zipWithIndex.find(_._1.index == from) match {
    case Some((koma, i)) => Board(komas.updated(i, koma.move(to)))
    case None => this
  }

}
