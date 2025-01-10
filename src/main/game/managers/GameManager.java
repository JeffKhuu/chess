package main.game.managers;

import main.computer.Computer;
import main.computer.move.Move;
import main.game.Board;
import main.game.Square;
import main.game.gui.Main;
import main.game.pieces.King;
import main.game.pieces.Piece;
import utilities.GameUtilities.COLOR;

import java.lang.reflect.Array;
import java.util.ArrayList;

public enum GameManager {
    INSTANCE; // Create a singleton instance of the manager

    public enum MODE {
        COMPUTER,
        TWO_PLAYER
    };

    public static class WinState {
        public enum WinCondition{
            CHECKMATE,
            STALEMATE
        }

        public COLOR winner;
        public WinCondition state;

        public WinState(COLOR winner, WinCondition state){
            this.winner = winner;
            this.state = state;
        }
    }

    MODE gamemode = MODE.TWO_PLAYER;
    public King lightKing, darkKing;
    public boolean isLightKingInCheck, isDarkKingInCheck;
    public boolean isGameOver;
    public Computer computer;

    COLOR turn = COLOR.LIGHT;
    COLOR user = COLOR.LIGHT;

    public COLOR getTurn(){
        return turn;
    }
    public COLOR getUserSide(){
        return user;
    }

    public MODE getGamemode() {return gamemode;}
    public void setGamemode(MODE gamemode) { this.gamemode = gamemode; }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    /**
     * Start the next turn on the chess board
     */
    public void nextTurn(){
        turn = turn == COLOR.LIGHT ? COLOR.DARK : COLOR.LIGHT;

        if(gamemode == MODE.TWO_PLAYER){ // If there are two players switch the user's colors for the next turn
            user = user == COLOR.LIGHT ? COLOR.DARK : COLOR.LIGHT;
        }

        isLightKingInCheck = lightKing.isAttacked();
        isDarkKingInCheck = darkKing.isAttacked();

        if(isLightKingInCheck || isDarkKingInCheck) Main.playSound("move-check.wav");

        if(gamemode == MODE.COMPUTER && turn == computer.getSide()){
            Move move = computer.getMove();
            computer.playMove(move);
            nextTurn();
        }
    }

    /**
     * Check a given board for checkmate and stalemate
     * @param board Board to check
     * @return Null if no end condition is met, otherwise a valid winstate containing end condition information.
     */
    public WinState checkWinCondition(Board board){
        ArrayList<Square> lightMoves = new ArrayList<>();
        ArrayList<Square> darkMoves = new ArrayList<>();

        for(Piece piece : board.pieces){
            if(piece.side == COLOR.LIGHT) lightMoves.addAll(board.validateMoves(piece, piece.getMoves()));
            else if(piece.side == COLOR.DARK) darkMoves.addAll(board.validateMoves(piece, piece.getMoves()));
        }

        if(lightMoves.size() == 0){
            if(isLightKingInCheck) return new WinState(COLOR.DARK, WinState.WinCondition.CHECKMATE);
            else return new WinState(COLOR.DARK, WinState.WinCondition.STALEMATE);
        }
        else if(darkMoves.size() == 0 ){
            if(isDarkKingInCheck) return new WinState(COLOR.LIGHT, WinState.WinCondition.CHECKMATE);
            else return new WinState(COLOR.LIGHT, WinState.WinCondition.STALEMATE);
        }else {
            return null;
        }
    }

    public void setLightKing(King king){
        lightKing = king;
    }

    public void setDarkKing(King king){
        darkKing = king;
    }
}
