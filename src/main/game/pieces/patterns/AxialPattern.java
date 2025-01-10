package main.game.pieces.patterns;

import main.game.Board;
import main.game.Pattern;
import main.game.Square;
import main.game.pieces.Rook;

import java.util.ArrayList;

public class AxialPattern implements Pattern {
    @Override
    public ArrayList<Square> getMoves(Square current, Board board) {
        ArrayList<Square> moves = new ArrayList<>();

        // HORIZONTAL MOVES
        for(int i = current.x + 1; i < Board.FILES; i++) { // Get a square using file/rank
            Square temp = board.getSquare(i, current.y);
            if(temp.piece == null) {
                moves.add(temp); // Add to possible moves if the square is empty or has an opposing piece
            } else if (temp.piece.side != current.piece.side) {
                moves.add(temp);
                break;
            } else {
                break;
            }
        }

        for(int i = current.x-1; i >= 0; i--) {
            Square temp = board.getSquare(i, current.y);
            if(temp.piece == null) {
                moves.add(temp); // Add to possible moves if the square is empty or has an opposing piece
            } else if (temp.piece.side != current.piece.side) {
                moves.add(temp);
                break;
            } else {
                break;
            }
        }

        // VERTICAL MOVES
        for(int i = current.y+1; i < Board.RANKS; i++) {
            Square temp = board.getSquare(current.x, i);
            if(temp.piece == null) {
                moves.add(temp); // Add to possible moves if the square is empty or has an opposing piece
            } else if (temp.piece.side != current.piece.side) {
                moves.add(temp);
                break;
            } else {
                break;
            }
        }

        for(int i = current.y-1; i >= 0; i--) {
            Square temp = board.getSquare(current.x, i);
            if(temp.piece == null) {
                moves.add(temp); // Add to possible moves if the square is empty or has an opposing piece
            } else if (temp.piece.side != current.piece.side) {
                moves.add(temp);
                break;
            } else {
                break;
            }
        }

        return moves;
    }
}
