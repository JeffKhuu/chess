package main.game.pieces;

import main.game.Board;
import main.game.Pattern;
import main.game.Square;
import main.game.pieces.patterns.DiagonalPattern;
import utilities.GameUtilities.COLOR;

import java.util.ArrayList;

public class Bishop extends Piece {

	public Bishop(COLOR side, Board board) {
		super(side, board);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Square> getMoves() {
		return new DiagonalPattern().getMoves(this.current, this.board);
	}

	@Override
	public char getNotationChar() {
		return 'b';
	}

}
