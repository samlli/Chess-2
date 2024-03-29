package ooga.model.EndConditionHandler;

import ooga.model.GameState;
import ooga.model.PieceInterface;
import ooga.model.PlayerInterface;

import java.util.*;

import static ooga.controller.Config.BoardBuilder.PIECE_TYPE;

/**
 * @authors purpose - the purpose of this file is to determine the elimination end condition which is satisfied
 * when enough of a certain piece have been eliminated
 * assumptions - it assumes that all the pieces and players are valid
 * dependencies - it depends on PieceInterface, PlayerInterface, and GameState
 * usage - the end condition runner loops through all the end conditions and calls checkmate to see if
 * a checkmate is met
 */
public class EliminationEndCondition implements EndConditionInterface {
  public static final String UNDERSCORE = "_";
  public static final String PIECE_TEAM_TYPE_FORMAT = "%s_%s";

  private List<PieceInterface> previousTurnPieces;
  private final Map<String, Integer> piecesToEliminate;

  /**
   * this builds the map of the pieces that needs to be eliminated
   *
   * @param propertiesMap the pieces that need to be eliminated
   * @param allpieces     all pieces, used to determine pieces eliminated
   */
  public EliminationEndCondition(Map<String, List<String>> propertiesMap, List<PieceInterface> allpieces) {
    Set<String> teams = new HashSet<>();
    previousTurnPieces = new ArrayList<>();
    for (PieceInterface piece : allpieces) {
      teams.add(piece.getTeam());
      previousTurnPieces.add(piece);
    }
    piecesToEliminate = new HashMap<>();
    Iterator<String> pieceIter = propertiesMap.get(PIECE_TYPE).iterator();
    while (pieceIter.hasNext()) {
      String pieceType = pieceIter.next();
      for (String team : teams) {
        String key = team + "_" + pieceType;
        piecesToEliminate.putIfAbsent(key, 0);
        piecesToEliminate.put(key, piecesToEliminate.get(key) + 1);
      }
    }
  }

  /**
   * elimination requires a certain number of pieces to be eliminated
   *
   * @param players all players
   * @return the winner if conditions are met
   */
  @Override
  public GameState isSatisfied(List<PlayerInterface> players) {
    List<PieceInterface> alivePieces = new ArrayList<>();
    for (PlayerInterface player : players) {
      for (PieceInterface piece : player.getPieces()) {
        alivePieces.add(piece);
      }
    }
    if (previousTurnPieces.size() == alivePieces.size()) {
      return null;
    }
    findMissingPiece(alivePieces);
    previousTurnPieces = alivePieces;
    return eliminatedAllTargets();
  }

  private void findMissingPiece(List<PieceInterface> alivePieces) {
    boolean found;
    for (PieceInterface p : previousTurnPieces) {
      found = false;
      for (PieceInterface alive : alivePieces) {
        if (p.equals(alive)) {
          found = true;
          break;
        }
      }
      if (!found) {
        logMissing(p);
      }
    }

  }

  private GameState eliminatedAllTargets() {
    HashMap<String, Integer> targetPiecesRemaining = getTargetPiecesRemaining();
    GameState winner = GameState.RUNNING;
    for (String team : targetPiecesRemaining.keySet()) {
      if (targetPiecesRemaining.get(team) <= 0) {
        winner = GameState.ENDED.getLoser(team);
        break;
      }
    }
    return (winner != GameState.RUNNING) ? winner : null;
  }

  private HashMap<String, Integer> getTargetPiecesRemaining() {
    HashMap<String, Integer> targetPiecesRemaining = new HashMap<>();
    for (String pieceString : piecesToEliminate.keySet()) {
      String[] pieceStringInfo = pieceString.split(UNDERSCORE);
      String team = pieceStringInfo[0];
      int piecesLeft = piecesToEliminate.get(pieceString);
      targetPiecesRemaining.putIfAbsent(team, 0);
      targetPiecesRemaining.put(team, targetPiecesRemaining.get(team) + piecesLeft);
    }
    return targetPiecesRemaining;
  }

  private void logMissing(PieceInterface missing) {
    String key = String.format(PIECE_TEAM_TYPE_FORMAT, missing.getTeam(), missing.getName());
    if (!piecesToEliminate.containsKey(key)) {
      return;
    }
    piecesToEliminate.put(key, piecesToEliminate.get(key) - 1);
  }
}
