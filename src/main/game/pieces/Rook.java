package main.game.pieces;

import java.util.ArrayList;

import main.game.Board;
import main.game.Pattern;
import main.game.Square;
import main.game.pieces.patterns.AxialPattern;
import utilities.GameUtilities.COLOR;

public class Rook extends Piece {	
	public Rook(COLOR side, Board board) {
		super(side, board);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Square> getMoves() {
		return new AxialPattern().getMoves(this.current, this.board);
	}

	@Override
	public char getNotationChar() {
		return 'r';
	}

}
