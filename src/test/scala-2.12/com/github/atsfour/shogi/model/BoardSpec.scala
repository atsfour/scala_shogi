package com.github.atsfour.shogi.model

import org.scalatest.{DiagrammedAssertions, FunSpec}

class BoardSpec extends FunSpec with DiagrammedAssertions {

  describe("Board") {

    describe("isOute") {

      it("returns true if enemy koma can move to my gyoku") {
        val board0 = Board.empty
          .updated(CellIndex(5, 1).get, Koma(Gote, Gyoku))
          .updated(CellIndex(5, 2).get, Koma(Sente, Kin))

        assert(board0.isOute(Sente) === false)
        assert(board0.isOute(Gote) === true)

        val board1 = Board.empty
          .updated(CellIndex(5, 1).get, Koma(Sente, Gyoku))
          .updated(CellIndex(5, 2).get, Koma(Sente, Hisha))

        assert(board1.isOute(Sente) === false)
        assert(board1.isOute(Gote) === false)

      }

    }

  }

}
