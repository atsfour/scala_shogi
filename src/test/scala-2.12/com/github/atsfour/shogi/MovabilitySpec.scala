package com.github.atsfour.shogi

import org.scalatest.{DiagrammedAssertions, FunSpec}

class MovabilitySpec extends FunSpec with DiagrammedAssertions {

  describe("Movability") {

    describe("MovableCells") {

      it("returns movable cells") {
        val emptyBoard = Board(Map.empty)
        val centerCell = CellIndex(5, 5).get

        val centerKinResult = Set(CellIndex(5, 4).get, CellIndex(4, 4).get, CellIndex(6, 4).get, CellIndex(4, 5).get, CellIndex(6, 5).get, CellIndex(5, 6).get)
        assert(Movability.Kin.movableCells(emptyBoard, Sente, centerCell) === centerKinResult)

        val centerKyoshaResult = Set(CellIndex(5, 4).get, CellIndex(5, 3).get, CellIndex(5, 2).get, CellIndex(5, 1).get)
        assert(Movability.Kyosha.movableCells(emptyBoard, Sente, centerCell) === centerKyoshaResult)

        val centerKakuResult = Set(
          CellIndex(4, 4).get, CellIndex(3, 3).get, CellIndex(2, 2).get, CellIndex(1, 1).get,
          CellIndex(6, 4).get, CellIndex(7, 3).get, CellIndex(8, 2).get, CellIndex(9, 1).get,
          CellIndex(4, 6).get, CellIndex(3, 7).get, CellIndex(2, 8).get, CellIndex(1, 9).get,
          CellIndex(6, 6).get, CellIndex(7, 7).get, CellIndex(8, 8).get, CellIndex(9, 9).get
        )
        assert(Movability.Kaku.movableCells(emptyBoard, Sente, centerCell) === centerKakuResult)

      }

      it("cannot move to cell that self side koma placed") {
        val board = Board(Map(CellIndex(5, 4).get -> Koma(Sente, Fu)))
        val centerCell = CellIndex(5, 5).get

        val centerKinResult = Set(CellIndex(4, 4).get, CellIndex(6, 4).get, CellIndex(4, 5).get, CellIndex(6, 5).get, CellIndex(5, 6).get)
        assert(Movability.Kin.movableCells(board, Sente, centerCell) === centerKinResult)

        assert(Movability.Kyosha.movableCells(board , Sente, centerCell) === Set())

        assert(Movability.Keima.movableCells(board, Sente, CellIndex(4, 6).get) === Set(CellIndex(3, 4).get))
      }

      it("straight movable koma cannot skip koma") {
        val board = Board(Map(CellIndex(5, 4).get -> Koma(Gote, Fu)))
        val centerCell = CellIndex(5, 5).get
        assert(Movability.Kyosha.movableCells(board , Sente, centerCell) === Set(CellIndex(5, 4).get))
      }

    }

  }

}
