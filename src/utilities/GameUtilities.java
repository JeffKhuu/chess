package utilities;

import java.awt.Color;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Scanner;

import main.game.Board;
import main.game.Square;
import main.game.managers.GameManager.MODE;
import main.game.pieces.*;

public class GameUtilities {
	public enum GAME_STATE {
		SETUP,
		ONGOING,
		END
	}
	
	public enum COLOR {
		LIGHT,
		DARK
	}
	
	public static final String START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 1000;
	
	public static final int SQUARE_WIDTH = WINDOW_WIDTH / Board.FILES;
	public static final int SQUARE_HEIGHT = WINDOW_HEIGHT / Board.RANKS;
	
	public static final Color LIGHT_SQUARE = new Color(238,238,210,255); // Light square color
	public static final Color DARK_SQUARE = new Color(118,150,86,255); // Dark square color

	public static final Color SELECT = new Color(189, 195, 199, 128);

	/**
	 * Print a given chess board to standard output.
	 * @param board A valid chess board.
	 */
	public static void printBoard(Board board) {
		Square[][] squares = board.getBoard();
		System.out.println();
		for(Square[] rank : squares) {
			for(Square square : rank) {
				if(square.piece != null){
					char c = (square.piece.side == COLOR.LIGHT) ? square.piece.getNotationChar() : Character.toUpperCase(square.piece.getNotationChar());
					System.out.print("["+ c + "] ");
				}else{
					System.out.print("[ ] ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * Prompt the user to enter a piece they would like to promote to.
	 * @param original Original pawn to promote
	 * @return The user-specified piece to promote to.
	 */
	public static Piece getPromotedPiece(Piece original){
		System.out.print("What piece would you like to promote to? (b/n/r/q): ");
		Scanner sc = new Scanner(System.in);

		switch (sc.nextLine().toLowerCase()){
			case "q":
				return new Queen(original.side, original.board);
			case "b":
				return new Bishop(original.side, original.board);
			case "n":
				return new Knight(original.side, original.board);
			case "r":
				return new Rook(original.side, original.board);
			default:
				System.out.println("The piece you entered was not valid please try again.");
				return getPromotedPiece(original);
		}
	}

	public static MODE getGamemode(){
		System.out.print("Would you like to play against a computer? (y): ");
		Scanner sc = new Scanner(System.in);

		if ("y".equalsIgnoreCase(sc.nextLine())) {
			return MODE.COMPUTER;
		}
		return MODE.TWO_PLAYER;
	}

}
