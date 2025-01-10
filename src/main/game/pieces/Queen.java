package main.game.pieces;

import main.game.Board;
import main.game.Pattern;
import main.game.Square;
import main.game.pieces.patterns.AxialPattern;
import main.game.pieces.patterns.DiagonalPattern;
import utilities.GameUtilities.COLOR;

import java.util.ArrayList;

public class Queen extends Piece {

	public Queen(COLOR side, Board board) {
		super(side, board);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Square> getMoves() {
		ArrayList<Square> moves = new ArrayList<>();

		moves.addAll(new AxialPattern().getMoves(this.current, this.board)); // Add horizontal and vertical moves
		moves.addAll(new DiagonalPattern().getMoves(this.current, this.board)); // Add diagonal moves

		return moves;
	}

	@Override
	public char getNotationChar() {
		// TODO Auto-generated method stub
		return 'q';
	}

}
