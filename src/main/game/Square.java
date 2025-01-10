package main.game;

import main.game.pieces.Piece;
import utilities.GameUtilities.COLOR;

public class Square implements Cloneable {
	// File & rank represent the row and column on a physical chess board
	// X & y represent the indices of the square in a 2d board array
	public int file, rank, x, y;
	public COLOR color;
	public Piece piece;
	
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
		this.rank = Board.RANKS - y;
		this.file = x + 1;
		piece = null;
		
		color = ((x+y) % 2 == 0) ? COLOR.LIGHT : COLOR.DARK;
	}
	
	public Square(int x, int y, Piece piece) {
		this.x = x;
		this.y = y;
		this.rank = Board.RANKS - y;
		this.file = x + 1;
		this.piece = piece;
		piece.current = this;
		
		color = ((x+y) % 2 == 0) ? COLOR.LIGHT : COLOR.DARK;
	}

	@Override
	public Square clone() {
		try {
			return (Square) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
