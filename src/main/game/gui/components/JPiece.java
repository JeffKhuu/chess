package main.game.gui.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.game.Board;
import main.game.Square;
import main.game.gui.Main;
import main.game.input.InputAdapter;
import main.game.managers.GameManager;
import main.game.pieces.King;
import main.game.pieces.Pawn;
import main.game.pieces.Piece;
import main.game.pieces.Queen;
import utilities.GameUtilities;
import utilities.GameUtilities.COLOR;

public class JPiece extends JPanel {
	private BufferedImage image;
	public Piece piece;
	public COLOR color;
	private final int offsetX = -1;
	private final int offsetY = -5;
	private InputAdapter input = new InputAdapter(this);
	private GameManager gameManager = GameManager.INSTANCE;
	
	public JPiece(Piece piece) {
		this.piece = piece;
		this.color = piece.side;
		
		setBounds(0, 0, GameUtilities.SQUARE_WIDTH, GameUtilities.SQUARE_HEIGHT);
		
		try {
			String colorStr = color == COLOR.LIGHT ? "w" : "b";
			String pieceStr = Character.toString(piece.getNotationChar());
			image = ImageIO.read(new File("assets/" + colorStr + pieceStr + ".png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		setOpaque(false);

		// Add input for each piece component
        addMouseListener(input);
        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
				if(gameManager.getTurn() != piece.side) return;
				if(gameManager.isGameOver) return;

                setLocation(e.getX() - input.draggedAtX + getLocation().x,
                        e.getY() - input.draggedAtY + getLocation().y);
            }
            
        });
	}

	/**
	 * Move the JPiece component to a given JSquare component. Resolves all logic with board/piece representations
	 * @param destination The component square the JPiece will move to.
	 */
	public void move(JSquare destination){
		Square temp = piece.current; // Create a temporary square of the pieces last square
		temp.piece = null;
		piece.current = destination.square; // Set the pieces current square to the destination square

		destination.square.piece = piece; // Set the destination square piece to the moved piece
		destination.pieceComponent = JPiece.this;

		piece.hasMoved = true;
		if(piece instanceof Pawn) ((Pawn) piece).justMoved = true;
		setLocation(destination.getLocation());
		Main.playSound("move-self.wav");
	}

	/**
	 * Remove this JPiece component from the board. Resolves all logic with board/piece representations.
	 */
	public void destroy(){
		JPanel panel = (JPanel) this.getParent();
		JPanel pieces = (JPanel)panel.getParent().getComponents()[0];
		JPanel board = (JPanel)panel.getParent().getComponents()[1];

		JSquare current = (JSquare) board.getComponentAt(this.getLocation());

		piece.board.pieces.remove(piece);

		piece.current.piece = null;
		current.square.piece = null;
		current.pieceComponent = null;



		Main.playSound("capture.wav");
		pieces.remove(this);
	}

	/**
	 * Replace this JPiece component with a given piece. Used for promotions
	 * @param promoted Piece to promote to
	 */
	public void replace(Piece promoted){
		promoted.current = piece.current;
		this.piece = promoted;
		updateImage();
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image.getScaledInstance(GameUtilities.SQUARE_WIDTH, GameUtilities.SQUARE_HEIGHT, ABORT), offsetX, offsetY, this);   
    }

	/**
	 * Update the image of the JPiece component
	 */
	private void updateImage(){
		try {
			String colorStr = color == COLOR.LIGHT ? "w" : "b";
			String pieceStr = Character.toString(piece.getNotationChar());
			image = ImageIO.read(new File("assets/" + colorStr + pieceStr + ".png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
    
    
}
