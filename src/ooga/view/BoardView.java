package ooga.view;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import ooga.Location;
import ooga.Turn;
import ooga.controller.Controller;
import ooga.controller.ControllerInterface;
import ooga.controller.InvalidPieceConfigException;
import ooga.controller.PieceViewBuilder;

public class BoardView extends Group implements BoardViewInterface {

    private final Color DEFAULT_COLOR_1 = Color.web("#f3dab0");
    private final Color DEFAULT_COLOR_2 = Color.web("#bb885b");
    private final String DEFAULT_PIECE_STYLE = "companion";

    private ControllerInterface controller;
    private Location startLocation;
    private BoardSquare[][] background;
    private List<PieceView> pieceList;

    public BoardView(Controller controller, List<PieceViewBuilder> pieceViews, int row, int col) {
        this.controller = controller;
        startLocation = null;
        pieceList = new ArrayList<>();
        initializeBoardView(pieceViews, row, col);
    }

    @Override
    public void initializeBoardView(List<PieceViewBuilder> pieceViews, int row, int col) {
        renderBackground(row, col);
        renderInitialChessPieces(pieceViews, DEFAULT_PIECE_STYLE);
        this.setOnMouseClicked(e -> {
            try {
                clickBoard(e);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (InvalidPieceConfigException invalidPieceConfigException) {
                invalidPieceConfigException.printStackTrace();
            }
        });
    }

    private void clickBoard(MouseEvent mouse) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, FileNotFoundException, InvalidPieceConfigException {
        Location clickLocation = new Location((int) mouse.getY() / 60, (int) mouse.getX() / 60);
        if (mouse.getButton() == MouseButton.SECONDARY) {
            background[clickLocation.getRow()][clickLocation.getCol()].annotate();
            return;
        }

        //user doesn't have piece selected and clicks on new piece
        //And add logic and is the same team

        if (startLocation == null) {
            if (controller.canMovePiece(clickLocation)) {
                selectPiece(clickLocation);
            } else {
                unselectPiece();
            }
        } else { //user selects new location with piece
            if (isLegalMove(clickLocation)) { //user clicks new location
                controller.movePiece(startLocation, clickLocation);
                unselectPiece();
            } else if (controller.canMovePiece(clickLocation)) {
                unselectPiece();
                selectPiece(clickLocation);
            }
            // if user clicks an illegal move
        }
    }

    private void selectPiece(Location location) {
        startLocation = location;
        background[location.getRow()][location.getCol()].select();
        showLegalMoves(location);
    }

    private void unselectPiece() {
        startLocation = null;
        for (int i = 0; i < background.length; i++) {
            for (int j = 0; j < background[0].length; j++) {
                background[i][j].resetBoardSquare();
            }
        }
    }

    private void movePiece(Location start, Location end) {
        for (PieceView pieceView : pieceList) {
            if (start.equals(pieceView.location)) {
                pieceView.moveTo(end);
            }
        }
    }

    private void removePiece(Location location) {

        for(PieceView pieceView : pieceList){
            if(pieceView.location.equals(location) ){
                this.getChildren().remove(pieceView);
            }
        }
    }

    private void renderBackground(int row, int col) {
        background = new BoardSquare[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Location location = new Location(i, j);
                BoardSquare boardSquare = new BoardSquare(location, null);
                background[i][j] = boardSquare;
                this.getChildren().add(boardSquare);
            }
        }
        changeColors(DEFAULT_COLOR_1, DEFAULT_COLOR_2);
    }

    private void renderInitialChessPieces(List<PieceViewBuilder> pieceViews, String style) {
        for(PieceViewBuilder piece : pieceViews) {
            PieceView newPiece = new PieceView(piece.getTeam(), piece.getName(), style, piece.getLocation());
            pieceList.add(newPiece);
            this.getChildren().add(newPiece);
        }
    }

    @Override
    public void changeColors(Color color1, Color color2) {
        for (int i = 0; i < background.length; i++) {
            for (int j = 0; j < background[0].length; j++) {
                Color color = (i + j) % 2 == 0 ? color1 : color2;
                background[i][j].setColor(color);
            }
        }
    }

    @Override
    public void changePieceStyle(String style) {
        for (PieceView pieceView : pieceList) {
            if (pieceView != null) {
                pieceView.changeStyle(style);
            }
        }
    }

    @Override
    public void resetBoard() {
        clearBoard();
        try {
            controller.resetGame();
        }
        catch (Exception ignored) {
        }
    }

    private void clearBoard() {

        for(PieceView pieceView : pieceList) {
            if (pieceView != null) {
                removePiece(pieceView.location);
            }
        }
    }

    private boolean isLegalMove(Location clickLocation) {
        for (Location legalSquare : controller.getLegalMoves(startLocation)) {
            if (clickLocation.equals(legalSquare)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateBoardView(Turn turn) {
        for (Location removed : turn.getRemoved()) {
            removePiece(removed);
        }
        for (Turn.PieceMove move : turn.getMoves()) {
            movePiece(move.getStartLocation(), move.getEndLocation());
        }
        if (turn.getCheckedSquare() != null){
          showCheck(turn.getCheckedSquare());
        }
    }

    @Override
    public void showLegalMoves(Location location) {
        List<Location> legalMoves = controller.getLegalMoves(location);
        for (Location squareLoc : legalMoves) {
            BoardSquare square = background[squareLoc.getRow()][squareLoc.getCol()];
            square.highlight();
        }
    }

    private void showCheck(Location location){
        background[location.getRow()][location.getCol()].check();
    }
}