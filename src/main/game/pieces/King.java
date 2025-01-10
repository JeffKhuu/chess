package main.game.pieces;

import main.game.Board;
import main.game.Pattern;
import main.game.Square;
import main.game.pieces.patterns.AdjacentPattern;
import main.game.pieces.patterns.AxialPattern;
import main.game.pieces.patterns.DiagonalPattern;
import main.game.pieces.patterns.KnightPattern;
import utilities.GameUtilities;
import utilities.GameUtilities.COLOR;

import java.util.ArrayList;

public class King extends Piece {

	public King(COLOR side, Board board) {
		super(side, board);
	}

	@Override
	public ArrayList<Square> getMoves() {
		ArrayList<Square> moves = new ArrayList<>();

		for(int i = this.current.x - 1; i <= this.current.x + 1; i++){
			for(int j = this.current.y - 1; j <= this.current.y + 1; j++){ // Find all adjacent squares
				if(i == current.x && j == current.y) continue;
				if(i < 0 || i >= Board.FILES || j < 0 || j >= Board.RANKS) continue;

				Square temp = board.getSquare(i, j);
				
				if(isAttacked(board.getPreview(this, temp))) continue;

				if(temp.piece == null) moves.add(temp); // Add empty squares and squares occupied by opposing pieces as valid moves
				else if(temp.piece.side != current.piece.side) moves.add(temp);
			}
		}

		// Check for castling
		if(!hasMoved){
			// Check if the two squares to the right are open (Short Castle)
			if(current.x + 1 >= Board.FILES || current.x + 2 >= Board.FILES || current.x - 1 < 0 || current.x - 2 < 0 || current.x - 3 < 0) return moves;
			if(board.getSquare(current.x + 1, current.y).piece == null && board.getSquare(current.x + 2, current.y).piece == null){
				Piece rook = board.getSquare(current.x + 3, current.y).piece;
				if(rook instanceof Rook && !rook.hasMoved){
					moves.add(board.getSquare(current.x + 2, current.y));
				}

			// Check if the three squares to the left are open (Long Castle)
			} else if (board.getSquare(current.x - 1, current.y).piece == null && board.getSquare(current.x - 2, current.y).piece == null && board.getSquare(current.x - 3, current.y) == null) {
				Piece rook = board.getSquare(current.x - 4, current.y).piece;
				if(rook instanceof Rook && !rook.hasMoved){
					moves.add(board.getSquare(current.x - 3, current.y));
				}
			}
		}

		return moves;
	}

	public boolean isAttacked(){
		return isAttacked(board);
	}

	/**
	 * Check if the king is attacked by any piece given a preview of the board where the king is potentially attacked
	 * @param preview Board to preview
	 * @return True if the king is attacked, false otherwise
	 */
	public boolean isAttacked(Board preview){
		King king = preview.getKing(side);

		if(king == null) return false;

		// Check pawn attack
		int forward = (side == COLOR.LIGHT) ? -1 : 1;

		if(king.current.x - 1 >= 0 && king.current.y + forward >= 0 && king.current.y + forward < Board.RANKS){
			Square leftPotential = preview.getSquare(king.current.x - 1, king.current.y + forward);
			if(leftPotential.piece instanceof Pawn && leftPotential.piece.side != side) return true;
		}

		if (king.current.x + 1 < Board.FILES && king.current.y + forward >= 0 && king.current.y + forward < Board.RANKS){
			Square rightPotential = preview.getSquare(king.current.x + 1, king.current.y + forward);
			if(rightPotential.piece instanceof Pawn && rightPotential.piece.side != side) return true;
		}

		// Check knight attack
		Pattern knightPattern = new KnightPattern();
		for(Square potential : knightPattern.getMoves(king.current, preview)){
			if(potential.piece instanceof Knight && potential.piece.side != side) return true;
		}

		// Check rook attack & queen horizontal
		Pattern rookPattern = new AxialPattern();
		for(Square potential : rookPattern.getMoves(king.current, preview)){
			if((potential.piece instanceof Rook || potential.piece instanceof Queen) && potential.piece.side != side) return true;
		}

		// Check bishop attack & queen vertical
		Pattern bishopPattern = new DiagonalPattern();
		for(Square potential : bishopPattern.getMoves(king.current, preview)){
			if((potential.piece instanceof Bishop || potential.piece instanceof Queen) && potential.piece.side != side) return true;
		}
		
		// Check king attacks
		for(Square potential : new AdjacentPattern().getMoves(king.current, preview)) {
//			for(int i = potential.x - 1; i <= potential.x + 1; i++){
//				for(int j = potential.y - 1; j <= potential.y + 1; j++){ // Find all adjacent squares
//					
//					if(i == potential.x && j == potential.y) continue;
//					if(i < 0 || i >= Board.FILES || j < 0 || j >= Board.RANKS) continue;
//
//					Square surrounding = board.getSquare(i, j);
//					if(surrounding.piece instanceof King && surrounding.piece.side != side) return true;
//				}
//			}
			if(potential.piece instanceof King && potential.piece.side != side) return true;
//			
		}
		return false;
	}

	@Override
	public char getNotationChar() {
		return 'k';
	}

}
