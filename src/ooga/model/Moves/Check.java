package ooga.model.Moves;

import java.util.ArrayList;
import java.util.List;
import ooga.Location;
import ooga.model.PieceInterface;
import ooga.model.PlayerInterface;

public class Check {

  public Check() {

  }

  public boolean exists(List<PlayerInterface> players){
    List<PieceInterface> allPieces = getAllPieces(players);
    for (PlayerInterface player : players){
      PieceInterface king = findKing(player.getTeam(), allPieces);
//      System.out.println(player.getTeam() + " " + king.getLocation());
      List<PieceInterface> attackingPieces = getAttackingPieces(player.getTeam(), allPieces);
      if (underAttack(king.getLocation(), attackingPieces, allPieces)){
        return true;
      }
    }
    return false;
  }

  private List<PieceInterface> getAllPieces(List<PlayerInterface> players) {
    List<PieceInterface> allPieces = new ArrayList<>();
    for (PlayerInterface player : players){
      allPieces.addAll(player.getPieces());
    }
    return allPieces;
  }

  /**
   * Checks if the king is under attack from enemy pieces
   * @param attackingTeam is the list of pieces attacking the king
   * @return true if the king is under attack from list of pieces
   */
  private boolean underAttack(Location kingLocation, List<PieceInterface> attackingTeam, List<PieceInterface> allPieces) {
    for(PieceInterface attackingPiece : attackingTeam) {
      List<Move> attackingMoves = attackingPiece.getMoves();
      for(Move attackingMove : attackingMoves) {
        if(kingLocation.inList(attackingMove.findAllEndLocations(attackingPiece, allPieces))) {
          return true;
        }
      }

    }
    return false;
  }

  private List<PieceInterface> getAttackingPieces(String attackedTeam, List<PieceInterface> allPieces) {
    List<PieceInterface> attackingPieces = new ArrayList<>();
    for(PieceInterface piece : allPieces) {
      if(!piece.getTeam().equals(attackedTeam)) {
        attackingPieces.add(piece);
      }
    }
    return attackingPieces;
  }

  private PieceInterface findKing(String team, List<PieceInterface> pieces) {
    for(PieceInterface piece : pieces) {
      if(team.equals(piece.getTeam()) && piece.getName().equals("K")) {
        return piece;
      }
    }
    return null;
  }
}
