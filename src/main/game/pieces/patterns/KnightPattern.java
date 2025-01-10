package main.game.pieces.patterns;

import main.game.Board;
import main.game.Pattern;
import main.game.Square;

import java.util.ArrayList;

public class KnightPattern implements Pattern {
    @Override
    public ArrayList<Square> getMoves(Square current, Board board) {
        int[] horizontal = 	{1, 1, -1, -1, 2, 2, -2, -2}; // Two equally sized lists to represent the set of numbers needed to get all valid moves of a knight
        int[] vertical = 	{2, -2, 2, -2, 1, -1, 1, -1};

        ArrayList<Square> moves = new ArrayList<>();

        for(int i = 0; i < horizontal.length; i++){
            // Step through each combination of horizontal and vertical
            int x = horizontal[i], y = vertical[i];
            if(current.x + x >= Board.FILES || current.x + x < 0 || current.y + y >= Board.RANKS || current.y + y < 0) continue; // skip out of bounds moves

            Square temp = board.getSquare(current.x + x, current.y + y);
            if(temp.piece == null){ // Add the moves if the square is empty or has an opposing piece
                moves.add(temp);
            }else if(temp.piece.side != current.piece.side){
                moves.add(temp);
            }
        }

        return moves;
    }
}
