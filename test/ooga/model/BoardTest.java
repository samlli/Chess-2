package ooga.model;

import static org.junit.jupiter.api.Assertions.*;

import ooga.Location;
import ooga.Turn;
import ooga.Turn.PieceMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
  private Board board;

  @BeforeEach
  void setUp() {
    board = new Board();
  }

  @Test
  void testRemovingPiece() {
    Location loc1 = new Location(0,1);
    Location loc2 = new Location(0,7);
    Turn turn = board.movePiece(loc1, loc2);
    int expected = 1;
    int test = turn.getRemoved().size();
    assertEquals(expected, test);
  }

  @Test
  void testMovesCorrectly() {
    Location end = new Location(3,3);
    Location loc1 = new Location(1,3);
    Location loc2 = new Location(3,3);

    Turn turn = board.movePiece(loc1, loc2);
    PieceMove pieceMove = turn.getMoves().get(0);

    assertTrue(end.equals(pieceMove.getEndLocation()));
  }
}