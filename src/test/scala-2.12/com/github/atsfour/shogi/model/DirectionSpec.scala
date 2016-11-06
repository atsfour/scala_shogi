package com.github.atsfour.shogi.model

import org.scalatest.{DiagrammedAssertions, FunSpec}

class DirectionSpec extends FunSpec with DiagrammedAssertions {

  describe("Direction") {
    describe("next") {

      val centerCell = CellIndex(5, 5).get

      it("returns next cell for player") {
        assert(Forward.next(Sente, centerCell) === CellIndex(5, 4))
        assert(Forward.next(Gote, centerCell) === CellIndex(5, 6))
        assert(KeimaRight.next(Sente, centerCell) === CellIndex(4, 3))
      }

      it("returns None if next cell is out of board") {
        assert(Forward.next(Sente, CellIndex(5, 1).get) === None)
        assert(Forward.next(Gote, CellIndex(5, 9).get) === None)
        assert(KeimaRight.next(Sente, CellIndex(5, 1).get) === None)
        assert(KeimaRight.next(Sente, CellIndex(5, 2).get) === None)
        assert(KeimaRight.next(Sente, CellIndex(1, 3).get) === None)
      }
    }
  }

}
