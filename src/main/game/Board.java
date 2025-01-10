package main.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import main.game.gui.components.JSquare;
import main.game.managers.GameManager;
import main.game.pieces.Bishop;
import main.game.pieces.King;
import main.game.pieces.Knight;
import main.game.pieces.Pawn;
import main.game.pieces.Piece;
import main.game.pieces.Queen;
import main.game.pieces.Rook;
import utilities.GameUtilities;
import utilities.GameUtilities.COLOR;

public class Board {
	public static final int RANKS = 8;
	public static final int FILES = 8;
	
	public Square[][] position = new Square[RANKS][FILES];
	public ArrayList<Piece> pieces = new ArrayList<>();
	
	public Board(String FEN) {
		Piece[][] map = fenToPieceMap(FEN);

		for(int y = 0; y < RANKS; y++) {
			for(int x = 0; x < FILES; x++) {
				if(map[y][x] != null) {
					Piece piece = map[y][x];

					pieces.add(piece);

					if(piece instanceof King){
						if(piece.side == COLOR.LIGHT) GameManager.INSTANCE.setLightKing((King)piece);
						else if(piece.side == COLOR.DARK) GameManager.INSTANCE.setDarkKing((King) piece);
					}

					position[y][x] = new Square(x, y, piece);
				}else {
					position[y][x] = new Square(x, y);
				}
				
				
			}
		}

	}

	public Board(Square[][] position){
		this.position = position;

		for(Square[] row : position){
			for(Square square : row){
				if(square.piece != null) pieces.add(square.piece);
			}
		}
	}
	
	public Board() {
		for(int y = 0; y < RANKS; y++) {
			for(int x = 0; x < FILES; x++) {
				position[x][y] = new Square(x, y);
			}
		}
	}
	
	
	/**
	 * Get a square using two given indices x and y
	 * @param x Index of the row to get
	 * @param y Index of the column to get
	 * @return Square from the given position
	 */
	public Square getSquare(int x, int y) {
		return position[y][x];
	}
	
	/**
	 * Get a square using a given file and rank if the final given parameter is true
	 * @param file File of the square to get
	 * @param rank Rank of the square to get
	 * @param useFileRank Whether to use the file/rank system or index system
	 * @return Square from the given position
	 */
	public Square getSquare(int rank, int file, boolean useFileRank) {
		return useFileRank ? position[file - 1][RANKS - rank] : getSquare(file, rank);
	}
	
	/**
	 * Get the 2D array representation of a created Chess board
	 * @return 2D array of squares
	 */
	public Square[][] getBoard(){
		return this.position;
	}

	public void notifyPieceMove(){
		for(Piece piece : pieces){
			piece.update();
		}
	}

	public ArrayList<Square> validateMoves(Piece piece, ArrayList<Square> moves){
		ArrayList<Square> valid = new ArrayList<>();

		for(Square square : moves){
			if(GameManager.INSTANCE.getTurn() == COLOR.LIGHT){
				if(!GameManager.INSTANCE.lightKing.isAttacked(getPreview(piece, square))) valid.add(square);
			} else if (GameManager.INSTANCE.getTurn() == COLOR.DARK) {
				if(!GameManager.INSTANCE.darkKing.isAttacked(getPreview(piece, square))) valid.add(square);
			}
		}

		return valid;
	}

	/**
	 * Get a preview of a chess board after a piece moves to a given square
	 * @param piece Piece to move
	 * @param destination Square to move to
	 * @return Preview of a chess board
	 */
	public Board getPreview(Piece piece, Square destination){
		Square[][] copy = new Square[RANKS][FILES];

		for(int y = 0; y < position.length; y++){
			for(int x = 0; x < position[0].length; x++){
				Square clone = position[y][x].clone();
				Piece pieceClone = piece.clone();
				if(piece.current.x == clone.x && piece.current.y == clone.y){
					clone.piece = null;
				}
				else if(destination.x == clone.x && destination.y == clone.y){
					pieceClone.current = clone;
					clone.piece = pieceClone;
				}
				copy[y][x] = clone;
			}
		}

		return new Board(copy);
	}

	/**
	 * Get the king on a board of a given color
	 * @param color Color of the king to get
	 * @return A king of the given color
	 */
	public King getKing(COLOR color){
		for(Piece piece : pieces){
			if(piece instanceof King && piece.side == color) return (King) piece;
		}
		return null;
	}

	/**
	 * Create a 2d array of pieces and nulls given a valid FEN string
	 * @param FEN A valid fen string
	 * @return 2D array of pieces and nulls where empty
	 */
	private Piece[][] fenToPieceMap(String FEN){
		Piece[][] map = new Piece[FILES][RANKS];
		
		String[] rows = FEN.split("/");
		for(int row = 0; row < rows.length; row++) {
			char[] instructions = rows[row].toCharArray();
			int col = 0;
			
			for(char instruction : instructions) {
				if(Character.isDigit(instruction)) {
					col += Character.getNumericValue(instruction);
				}else {
					map[row][col] = getPieceFromInstruction(instruction);
					col++;
				}
			}
		}
		return map;
	}

	/**
	 * Get a piece from a given character instruction
	 * @param instruction A single character representing a piece and its side
	 * @return The piece as a class
	 */
	private Piece getPieceFromInstruction(char instruction) {
		COLOR side = Character.isUpperCase(instruction) ? COLOR.LIGHT : COLOR.DARK;
		switch(instruction) {
			case 'p':
			case 'P':
				return new Pawn(side, this);
			case 'r':
			case 'R':
				return new Rook(side, this);
			case 'n':
			case 'N':
				return new Knight(side, this);
			case 'b':
			case 'B':
				return new Bishop(side, this);
			case 'q':
			case 'Q':
				return new Queen(side, this);
			case 'k':
			case 'K':
			default:
				return new King(side, this);
		}
	}
}
