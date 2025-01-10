package main.computer;

import main.computer.move.Move;
import main.game.Board;
import main.game.Square;
import main.game.gui.components.JPiece;
import main.game.gui.components.JSquare;
import main.game.pieces.Pawn;
import main.game.pieces.Piece;
import main.game.pieces.Queen;
import utilities.GameUtilities;
import utilities.GameUtilities.COLOR;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Computer {
    Board board;
    COLOR side;
    JLayeredPane game;

    JPanel boardPanel;
    JPanel piecePanel;

    public Computer(Board board, COLOR side, JLayeredPane game){
        this.board = board;
        this.side = side;
        this.game = game;

        piecePanel = (JPanel) game.getComponents()[0];
        boardPanel = (JPanel) game.getComponents()[1];
    }

    /**
     * Let the computer generate a move.
     * @return A valid move for the computer to play
     */
    public Move getMove(){
        ArrayList<Move> allMoves = new ArrayList<>();

        for(Piece piece : board.pieces){
            if(piece.side != side) continue;

            ArrayList<Square> destinations = board.validateMoves(piece, piece.getMoves());
            for(Square destination : destinations){
                Move move = new Move(piece, destination);
                allMoves.add(move);
            }
        }

        // Decision-Making Algorithm
        int index = (int)(Math.random() * allMoves.size());

        return allMoves.get(index);
    }

    /**
     * Plays a given move on to the game board
     * @param move A given move to play
     */
    public void playMove(Move move){
        JSquare destination = getSquareComponent(move.destination);
        JPiece piece = getPieceComponent(move.piece);

        if(destination.square.piece != null){
            destination.pieceComponent.destroy();
        }

        if(move.piece instanceof Pawn){
            if(Math.abs(destination.square.y - move.piece.current.y) == 2) { // Check if the pawn has moved two spaces forward
                ((Pawn) move.piece).justMoved = true; // Set this flag to true to be able to check for en passant
            }

            if(destination.square.x - move.piece.current.x != 0){ // Check if the pawn has captured something (en passant or diagonally)
                if(destination.square.piece == null){ // The move is en passant
                    int backward = move.piece.side == GameUtilities.COLOR.LIGHT ? 1 : -1;

                    JSquare enPassant = getSquareComponent(move.destination.x, move.destination.y + backward);
                    enPassant.pieceComponent.destroy();
                }
            }

            if(destination.square.y == 0 || destination.square.y == 7){ // Promote the pawn
                Piece promoted = new Queen(side, board);
                piece.replace(promoted);
            }
        }
        piece.move(destination);
    }

    /**
     * Get the java swing component of a given piece
     * @param piece Piece to get the component of
     * @return Java Swing component
     */
    public JPiece getPieceComponent(Piece piece){
        for(Component component : piecePanel.getComponents()){
            if(!(component instanceof JPiece comp)) continue;

            if(comp.piece == piece){
                return comp;
            }
        }
        return null;
    }

    /**
     * Get the java swing component of a given square
     * @param square Square to get the component of
     * @return Java Swing component
     */
    public JSquare getSquareComponent(Square square){
        return (JSquare) boardPanel.getComponentAt(square.x * GameUtilities.SQUARE_WIDTH + (GameUtilities.SQUARE_WIDTH/2), square.y * GameUtilities.SQUARE_HEIGHT + (GameUtilities.SQUARE_HEIGHT / 2));
    }

    /**
     * Get the java swing component of a given square's coordinates
     * @param x x coordinate
     * @param y y coordinate
     * @return Java Swing component
     */
    public JSquare getSquareComponent(int x, int y){
        return (JSquare) boardPanel.getComponentAt(x * GameUtilities.SQUARE_WIDTH + (GameUtilities.SQUARE_WIDTH/2), y * GameUtilities.SQUARE_HEIGHT + (GameUtilities.SQUARE_HEIGHT / 2));
    }

    /**
     * Get the color that the computer is playing
     * @return Side the computer is playing
     */
    public COLOR getSide(){
        return side;
    }
}
