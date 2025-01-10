package main.game.pieces.patterns;

import main.game.Board;
import main.game.Pattern;
import main.game.Square;

import java.util.ArrayList;

public class DiagonalPattern implements Pattern {
    @Override
    public ArrayList<Square> getMoves(Square current, Board board) {
        ArrayList<Square> moves = new ArrayList<>();

        // Up and to the right
        for (int i = current.x+1; i < Board.FILES; i++){
            int diff = i - current.x;
            int y = current.y + diff;

            if(y >= Board.RANKS) break;

            Square temp = board.getSquare(i, y);
            if(temp.piece == null){
                moves.add(temp);
            } else if (temp.piece.side != current.piece.side) {
                moves.add(temp);
                break;
            }else{
                break;
            }
        }

        // Up and to the left
        for (int i = current.x-1; i >= 0; i--){
            int diff = current.x - i;
            int y = current.y + diff;

            if(y >= Board.RANKS) break;

            Square temp = board.getSquare(i, y);
            if(temp.piece == null){
                moves.add(temp);
            } else if (temp.piece.side != current.piece.side) {
                moves.add(temp);
                break;
            }else{
                break;
            }
        }

        // Down and to the right
        for (int i = current.x+1; i < Board.FILES; i++){
            int diff = i - current.x;
            int y = current.y - diff;

            if(y < 0) break;

            Square temp = board.getSquare(i, y);
            if(temp.piece == null){
                moves.add(temp);
            } else if (temp.piece.side != current.piece.side) {
                moves.add(temp);
                break;
            }else{
                break;
            }
        }

        // Down and to the left
        for (int i = current.x-1; i >= 0; i--){
            int diff = current.x - i;
            int y = current.y - diff;

            if(y < 0) break;

            Square temp = board.getSquare(i, y);
            if(temp.piece == null){
                moves.add(temp);
            } else if (temp.piece.side != current.piece.side) {
                moves.add(temp);
                break;
            }else{
                break;
            }
        }

        return moves;
    }
}
