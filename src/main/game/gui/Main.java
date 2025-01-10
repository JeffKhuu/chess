package main.game.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.computer.Computer;
import main.game.Board;
import main.game.Square;
import main.game.gui.components.JPiece;
import main.game.gui.components.JSquare;
import main.game.input.InputAdapter;
import main.game.managers.GameManager;
import main.game.pieces.Pawn;
import utilities.GameUtilities;
import utilities.GameUtilities.COLOR;

public class Main {
	/**
	 * Create the GUI and game given a valid chess board
	 * @param board Chess board
	 */
	public static void createAndShowGUI(Board board) {
		JFrame frame = new JFrame();
		JLayeredPane gamePanel = new JLayeredPane();
		JPanel boardPanel = new JPanel();
		JPanel piecePanel = new JPanel();
		
		ImageIcon icon = new ImageIcon("assets/icon.png");
		boardPanel.setLayout(new GridLayout(Board.RANKS, Board.FILES));
		piecePanel.setLayout(new GridLayout(Board.RANKS, Board.FILES));
		
		
		
		piecePanel.setOpaque(false);
		for(int i = 0; i < board.position.length; i++) {
			for(int j = 0; j < board.position[0].length; j++) {
				Square square = board.getSquare(j, i);
				JSquare squareComponent = new JSquare(square);

				boardPanel.add(squareComponent);
				if(square.piece != null) {
					JPiece piece = new JPiece(square.piece);
					squareComponent.pieceComponent = piece;
					piecePanel.add(piece);
				}else {
					JPanel empty = new JPanel();
					empty.setOpaque(false);
					piecePanel.add(empty);
				}
				
			}
		}
		
		gamePanel.setSize(GameUtilities.WINDOW_WIDTH, GameUtilities.WINDOW_HEIGHT);
		boardPanel.setBounds(0, 0, GameUtilities.WINDOW_WIDTH, GameUtilities.WINDOW_HEIGHT);
		piecePanel.setBounds(0, 0, GameUtilities.WINDOW_WIDTH, GameUtilities.WINDOW_HEIGHT);
		
		gamePanel.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
		gamePanel.add(piecePanel, JLayeredPane.MODAL_LAYER);
		
		frame.add(gamePanel);

		GameManager.INSTANCE.setComputer(new Computer(board, COLOR.DARK, gamePanel));
		
		frame.setSize(GameUtilities.WINDOW_WIDTH+15, GameUtilities.WINDOW_HEIGHT+40);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chess");
		frame.setIconImage(icon.getImage());
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		Board board = new Board(GameUtilities.START_POSITION);
		GameManager.INSTANCE.setGamemode(GameUtilities.getGamemode());
		
		SwingUtilities.invokeLater(() -> {
			createAndShowGUI(board); // Create the GUI
		});
		
	}

	public static void playSound(String filename)
	{
		try
		{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("assets/sound/" + filename)));
			clip.start();
		}
		catch (Exception exc)
		{
			exc.printStackTrace(System.out);
		}
	}

}
