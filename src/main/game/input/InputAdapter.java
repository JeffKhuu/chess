package main.game.input;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.*;

import main.game.Board;
import main.game.Square;
import main.game.gui.Main;
import main.game.gui.components.JPiece;
import main.game.gui.components.JSquare;
import main.game.managers.GameManager;
import main.game.pieces.King;
import main.game.pieces.Pawn;
import main.game.pieces.Piece;
import utilities.GameUtilities;

public class InputAdapter extends MouseAdapter {
	JPiece component;
	GameManager gameManager = GameManager.INSTANCE;
	public volatile int draggedAtX, draggedAtY;

	public InputAdapter(JPiece piece){
		component = piece;
	}

	@Override
	public void mousePressed(MouseEvent e){
		Piece piece = component.piece;
		if(gameManager.getTurn() != piece.side) return;
		if(gameManager.isGameOver) return;

		draggedAtX = e.getX();
		draggedAtY = e.getY();
		JPanel panel = (JPanel)e.getComponent().getParent();
		JPanel squares = (JPanel) panel.getParent().getComponents()[1];
		JPanel pieces = (JPanel) panel.getParent().getComponents()[0];



		ArrayList<Square> moves = piece.board.validateMoves(piece, piece.getMoves());

		piece.showPossibleMoves(squares, moves);
		pieces.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Piece piece = component.piece;

		if(gameManager.getTurn() != piece.side) return;
		if(gameManager.isGameOver) return;

		JPanel panel = (JPanel)e.getComponent().getParent();
		Point pt = e.getLocationOnScreen(); // Convert location from screen to point on the frame
		SwingUtilities.convertPointFromScreen(pt, panel);
		JSquare destination = (JSquare)panel.getParent().getComponents()[1].getComponentAt(pt);

		// Hide Preview Dots
		JPanel board = (JPanel)panel.getParent().getComponents()[1];
		JPanel pieces = (JPanel)panel.getParent().getComponents()[0];
		for(Component component : board.getComponents()) {
			JSquare sq = (JSquare)component;
			sq.setVisibleDot(false);
			pieces.repaint();
		}
		
		JSquare sq = (JSquare) board.getComponents()[0];
		if(pt.x >= GameUtilities.WINDOW_WIDTH || pt.x <= 0 || pt.y >= GameUtilities.WINDOW_HEIGHT || pt.y <= 0) {
			component.setLocation(sq.getSquare(piece.current.x, piece.current.y).getLocation()); // Reset the location
			return;
		}

		ArrayList<Square> moves = piece.board.validateMoves(piece ,piece.getMoves());

		if(moves.contains(destination.square)){
			// Properly move the piece in board logic
			if(piece instanceof King){ // Handle Castling
				if(destination.square.x - piece.current.x >= 2){ // Check if the king moves two squares (check if its castling)
					JSquare rookSquare = (JSquare)board.getComponentAt(pt.x + GameUtilities.SQUARE_WIDTH, pt.y);
					JSquare castledSquare = (JSquare) board.getComponentAt(pt.x - GameUtilities.SQUARE_WIDTH, pt.y);

					rookSquare.pieceComponent.move(castledSquare);
				}
			}

			if(piece instanceof Pawn){
				if(Math.abs(destination.square.y - piece.current.y) == 2) { // Check if the pawn has moved two spaces forward
					((Pawn) piece).justMoved = true; // Set this flag to true to be able to check for en passant
				}

				if(destination.square.x - piece.current.x != 0){ // Check if the pawn has captured something (en passant or diagonally)
					if(destination.square.piece == null){ // The move is en passant
						int backward = piece.side == GameUtilities.COLOR.LIGHT ? 1 : -1;

						JSquare enPassant = (JSquare)board.getComponentAt(pt.x, pt.y + (GameUtilities.SQUARE_HEIGHT * backward));
						enPassant.pieceComponent.destroy();

					}

				}

				if(destination.square.y == 0 || destination.square.y == 7){ // Promote the pawn
					Piece promoted = GameUtilities.getPromotedPiece(piece);

					component.replace(promoted);
				}
			}

			if(destination.square.piece != null && destination.pieceComponent != null){
				destination.pieceComponent.destroy();
			}

			piece.board.notifyPieceMove(); // Update data on all pieces when a piece has moved. IMPORTANT that the notification happens prior to the move
			component.move(destination);
			GameManager.INSTANCE.nextTurn();

			GameManager.WinState winState = gameManager.checkWinCondition(piece.board);
			if(winState != null) { // End the game
				gameManager.isGameOver = true;
				if(winState.state == GameManager.WinState.WinCondition.CHECKMATE){
					System.out.println("The game is over! " + winState.winner + " won through " + winState.state);
				}else if(winState.state == GameManager.WinState.WinCondition.STALEMATE){
					System.out.println("The game drew via stalemate!");
				}

			}

		}else{
			component.setLocation(destination.getSquare(piece.current.x, piece.current.y).getLocation()); // Reset the location
		}
	}
}
