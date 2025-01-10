package main.game.pieces;

import java.util.ArrayList;

import javax.swing.JPanel;

import main.game.Board;
import main.game.Square;
import main.game.gui.components.JSquare;
import utilities.GameUtilities;
import utilities.GameUtilities.COLOR;

public abstract class Piece implements Cloneable{
	public COLOR side;
	public boolean hasMoved = false;
	
	public Board board;
	public Square current;
	
	public Piece(COLOR side, Board board) {
		this.side = side;
		this.board = board;
	}
	
	public abstract ArrayList<Square> getMoves();
	public abstract char getNotationChar();

	public void update(){}

	/**
	 * Show all the visible moves using a dot indicator from each square.
	 * @param board JPanel component of the board and squares
	 * @param moves ArrayList of all valid moves as squares
	 */
	public void showPossibleMoves(JPanel board, ArrayList<Square> moves) {
		if(moves != null) {
			for(Square square : moves) {
				int x = square.x * GameUtilities.SQUARE_WIDTH + (GameUtilities.SQUARE_WIDTH / 2);
				int y = square.y * GameUtilities.SQUARE_HEIGHT + (GameUtilities.SQUARE_HEIGHT / 2);
				
				JSquare component = (JSquare)board.getComponentAt(x, y);
				component.toggleVisibleDot();
			}
		}
	}

	@Override
	public Piece clone() {
		try {
			return (Piece) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
