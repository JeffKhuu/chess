package main.computer.move;

import main.game.Square;
import main.game.pieces.Piece;

public class Move {
    public Piece piece;
    public Square destination;

    public Move(Piece piece, Square destination){
        this.piece = piece;
        this.destination = destination;
    }
}
