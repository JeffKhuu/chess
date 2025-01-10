package main.game.pieces;

import main.game.Board;
import main.game.Pattern;
import main.game.Square;
import main.game.pieces.patterns.KnightPattern;
import utilities.GameUtilities.COLOR;

import java.util.ArrayList;

public class Knight extends Piece {

	public Knight(COLOR side, Board board) {
		super(side, board);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Square> getMoves() {
		return new KnightPattern().getMoves(current, board);
	}

	@Override
	public char getNotationChar() {
		return 'n';
	}

}
