package main.game.pieces;

import java.util.ArrayList;

import main.game.Board;
import main.game.Pattern;
import main.game.Square;
import utilities.GameUtilities.COLOR;

public class Pawn extends Piece {

	public Pawn(COLOR side, Board board) {
		super(side, board);
	}
	public boolean justMoved; // True if the pawn has just advanced two squares forward last turn

	@Override
	public ArrayList<Square> getMoves() {
		ArrayList<Square> moves = new ArrayList<>();

		int forward = (side == COLOR.LIGHT) ? -1 : 1;

		// TODO: Promotion
		if((current.y + forward) >= Board.RANKS || (current.y + forward) < 0) return moves; // Return no moves if the square ahead is off the board

		Square ahead = board.getSquare(current.x, current.y + forward);
		if(ahead.piece == null) moves.add(ahead); // Find the square ahead, if its empty it is a valid move

		if(!hasMoved){ // Check if the pawn can advance two squares ahead
			if(ahead.piece == null){
				Square twoAhead = board.getSquare(current.x, current.y + (2*forward));
				if(twoAhead.piece == null) moves.add(twoAhead);
			}
		}

		if(current.x + 1 < Board.FILES){ // Check the diagonal towards the right for a piece
			Square right = board.getSquare(current.x + 1, current.y + forward);
			if(right.piece != null && right.piece.side != side) moves.add(right);

			Square enPassant = board.getSquare(current.x + 1, current.y); // Check for en passant
			if(enPassant.piece instanceof Pawn && ((Pawn) enPassant.piece).justMoved){
				moves.add(right);
			}
		}

		if(current.x - 1 >= 0){ // Check the diagonal towards the left for a piece
			Square left = board.getSquare(current.x - 1, current.y + forward);
			if(left.piece != null && left.piece.side != side) moves.add(left);

			Square enPassant = board.getSquare(current.x - 1, current.y); // Check for en passant
			if(enPassant.piece instanceof Pawn && ((Pawn) enPassant.piece).justMoved){
				moves.add(left);
			}
		}

		return moves;
	}

	@Override
	public void update(){
		if(justMoved) justMoved = false;
	}

	@Override
	public char getNotationChar() {
		return 'p';
	}

}
