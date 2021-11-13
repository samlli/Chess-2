package ooga.model;

import java.util.List;

public interface MoveVectorInterface {
    List<Piece.Vector> getMoveVectors();

    int getRowVector(int index);

    int getColVector(int index);
}