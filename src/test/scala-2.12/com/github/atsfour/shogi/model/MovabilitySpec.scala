package com.github.atsfour.shogi.model

import org.scalatest.{DiagrammedAssertions, FunSpec}

class MovabilitySpec extends FunSpec with DiagrammedAssertions {

  describe("Movability") {

    describe("MovableCells") {

      it("returns movable cells") {
        val centerCell = CellIndex(5, 5).get

        val centerKinResult = Set(CellIndex(5, 4).get, CellIndex(4, 4).get, CellIndex(6, 4).get, CellIndex(4, 5).get, CellIndex(6, 5).get, CellIndex(5, 6).get)
        assert(Movability.Kin.movableCells(Map.empty, Sente, centerCell) === centerKinResult)

        val centerKyoshaResult = Set(CellIndex(5, 4).get, CellIndex(5, 3).get, CellIndex(5, 2).get, CellIndex(5, 1).get)
        assert(Movability.Kyosha.movableCells(Map.empty, Sente, centerCell) === centerKyoshaResult)

        val centerKakuResult = Set(
          CellIndex(4, 4).get, CellIndex(3, 3).get, CellIndex(2, 2).get, CellIndex(1, 1).get,
          CellIndex(6, 4).get, CellIndex(7, 3).get, CellIndex(8, 2).get, CellIndex(9, 1).get,
          CellIndex(4, 6).get, CellIndex(3, 7).get, CellIndex(2, 8).get, CellIndex(1, 9).get,
          CellIndex(6, 6).get, CellIndex(7, 7).get, CellIndex(8, 8).get, CellIndex(9, 9).get
        )
        assert(Movability.Kaku.movableCells(Map.empty, Sente, centerCell) === centerKakuResult)

      }

      it("cannot move to cell that self side koma placed") {
        val komaMap = Map(CellIndex(5, 4).get -> Koma(Sente, Fu))
        val centerCell = CellIndex(5, 5).get

        val centerKinResult = Set(CellIndex(4, 4).get, CellIndex(6, 4).get, CellIndex(4, 5).get, CellIndex(6, 5).get, CellIndex(5, 6).get)
        assert(Movability.Kin.movableCells(komaMap, Sente, centerCell) === centerKinResult)

        assert(Movability.Kyosha.movableCells(komaMap , Sente, centerCell) === Set())

        assert(Movability.Keima.movableCells(komaMap, Sente, CellIndex(4, 6).get) === Set(CellIndex(3, 4).get))
      }

      it("straight movable koma cannot skip koma") {
        val komaMap = Map(CellIndex(5, 4).get -> Koma(Gote, Fu))
        val centerCell = CellIndex(5, 5).get
        assert(Movability.Kyosha.movableCells(komaMap , Sente, centerCell) === Set(CellIndex(5, 4).get))
      }

    }

    describe("puttableCells") {

      it("returns puttable cells") {

        assert(Movability.Kin.puttableCells(Map.empty, Sente) === CellIndex.all.toSet)
        assert(Movability.Kyosha.puttableCells(Map.empty, Sente) === CellIndex.all.filter(_.dan >= 2).toSet)
        assert(Movability.Keima.puttableCells(Map.empty, Sente) === CellIndex.all.filter(_.dan >= 3).toSet)

      }

      it("forbid nifu") {
        assert(Movability.Fu.puttableCells(Map.empty, Sente) === CellIndex.all.filter(_.dan >= 2).toSet)
        assert(Movability.Fu.puttableCells(Map(CellIndex(3, 2).get -> Koma(Sente, Fu)), Sente).forall(_.suji != 3))
      }

    }

  }

}
