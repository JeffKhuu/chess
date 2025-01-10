package main.game;

import java.util.ArrayList;

public interface Pattern {
	ArrayList<Square> getMoves(Square current, Board board);
}
