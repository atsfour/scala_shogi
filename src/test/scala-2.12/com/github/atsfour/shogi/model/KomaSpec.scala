package com.github.atsfour.shogi.model

import org.scalatest.{DiagrammedAssertions, FunSpec}

class KomaSpec extends FunSpec with DiagrammedAssertions {

  describe("Koma") {

    describe("movableCells") {

      it("reject gyoku suicide") {
        val koma = Koma(Gote, Gyoku)
        val board = Board.empty
          .updated(CellIndex(5, 1).get, koma)
          .updated(CellIndex(1, 2).get, Koma(Sente, Hisha))
        val from = CellIndex(5, 1).get
        val expected = Set(
          CellIndex(4, 1).get, CellIndex(6, 1).get
        )
        assert(koma.movableCells(board, from) === expected)
      }

      it("reject hiraki oute") {
        val koma0 = Koma(Gote, Kyosha)
        val board0 = Board.empty
          .updated(CellIndex(6, 1).get, Koma(Gote, Gyoku))
          .updated(CellIndex(3, 1).get, koma0)
          .updated(CellIndex(1, 1).get, Koma(Sente, Hisha))
        val from0 = CellIndex(3, 1).get
        assert(koma0.movableCells(board0, from0) === Set())

        val koma1 = Koma(Gote, Hisha)
        val from1 = CellIndex(3, 1).get
        val board1 = board0.updated(from1, koma1)
        val expected1 = Set(
          CellIndex(1, 1).get, CellIndex(2, 1).get, CellIndex(4, 1).get, CellIndex(5, 1).get
        )
        assert(koma1.movableCells(board1, from1) === expected1)
      }

      it("cannot leave oute") {
        val koma0 = Koma(Gote, Kin)
        val board0 = Board.empty
          .updated(CellIndex(6, 1).get, Koma(Gote, Gyoku))
          .updated(CellIndex(3, 2).get, koma0)
          .updated(CellIndex(1, 1).get, Koma(Sente, Hisha))
        val from0 = CellIndex(3, 2).get
        assert(koma0.movableCells(board0, from0) === Set(CellIndex(3, 1).get))
      }

      it("can get koma that makes oute") {
        val koma0 = Koma(Gote, Kin)
        val board0 = Board.empty
          .updated(CellIndex(5, 1).get, Koma(Gote, Gyoku))
          .updated(CellIndex(4, 1).get, koma0)
          .updated(CellIndex(5, 2).get, Koma(Sente, Kin))
        val from0 = CellIndex(4, 1).get
        assert(koma0.movableCells(board0, from0) === Set(CellIndex(5, 2).get))
      }

    }

    describe("puttableCells") {

      it("cannot leave oute") {
        val board0 = Board.empty
          .updated(CellIndex(5, 1).get, Koma(Gote, Gyoku))
          .updated(CellIndex(1, 1).get, Koma(Sente, Hisha))
        val expected0 = Set(
          CellIndex(4, 1).get, CellIndex(3, 1).get, CellIndex(2, 1).get
        )

        assert(Koma(Gote, Kin).puttableCells(board0) === expected0)
        assert(Koma(Sente, Kin).puttableCells(board0) === CellIndex.all.filterNot(board0.komaMap.isDefinedAt).toSet)
      }

    }

  }

}
