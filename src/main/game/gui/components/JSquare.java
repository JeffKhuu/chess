package main.game.gui.components;

import java.awt.*;

import javax.swing.*;

import main.game.Square;
import main.game.input.InputAdapter;

import utilities.GameUtilities;
import utilities.GameUtilities.COLOR;

public class JSquare extends JLayeredPane {
	public Square square;
	public JPiece pieceComponent = null;
	
	private final int DOT_RADIUS = 20;
	private boolean drawDot = false;
	
	public JSquare(Square square) {
		this.square = square;
		
		if(square.piece != null) {
			pieceComponent = new JPiece(square.piece);
		}

		repaint();
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
		
		Rectangle box = new Rectangle(GameUtilities.SQUARE_WIDTH, GameUtilities.SQUARE_HEIGHT);
		Color color = square.color == COLOR.DARK ? GameUtilities.DARK_SQUARE : GameUtilities.LIGHT_SQUARE;
		g2D.draw(box);
		g2D.setColor(color);
        g2D.fill(box);
        
        if(drawDot) {
			if(square.piece == null){
				int x = GameUtilities.SQUARE_WIDTH / 2 - DOT_RADIUS;
				int y = GameUtilities.SQUARE_HEIGHT / 2 - DOT_RADIUS;

				g2D.setColor(GameUtilities.SELECT);
				g2D.fillOval(x, y, DOT_RADIUS*2, DOT_RADIUS*2);
			}else{
				int radius = DOT_RADIUS * 3;

				int x = GameUtilities.SQUARE_WIDTH / 2 - radius;
				int y = GameUtilities.SQUARE_HEIGHT / 2 - radius;

				g2D.setColor(GameUtilities.SELECT);
				g2D.fillOval(x, y, radius*2, radius*2);
				g2D.setColor(color);
			}

        }
	}

	/**
	 * Set the visible of the indicator dot to a given boolean
	 * @param bool True if the dot is visible, false otherwise
	 */
	public void setVisibleDot(boolean bool) {
		drawDot = bool;
		paintComponent(getGraphics());
	}

	/**
	 * Toggle the visibility of the indicator dot. Becomes true if false, and false if true.
	 */
	public void toggleVisibleDot() {
		drawDot = !drawDot;
		paintComponent(getGraphics());
	}

	/**
	 * Get a JSquare component given the x and y grid coordinates. Do not use the pixel location x and y positions
	 * @param x Coordinate from the grid (0, 1, ..., 7)
	 * @param y Coordinate from the grid (0, 1, ..., 7)
	 * @return JSquare component
	 */
	public JSquare getSquare(int x, int y){
		return (JSquare) getParent().getComponentAt(x * GameUtilities.SQUARE_WIDTH + (GameUtilities.SQUARE_WIDTH/2), y * GameUtilities.SQUARE_HEIGHT + (GameUtilities.SQUARE_HEIGHT / 2));
	}
}
