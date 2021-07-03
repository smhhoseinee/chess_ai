package models;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class Game {

    private final Player whitePlayer;
    private final Player blackPlayer;
    private final Board board;

    public Game(Player whitePlayer, Player blackPlayer, GameVariation variation) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.board = new Board();
        this.board.loadFromFen(variation.toString());
    }


    public Player play() {
        System.out.println(board);
        NodeEvalFunctions.valueOfPieces(board);

        System.out.println("Get Fen :");
        System.out.println(board.getFen());
        System.out.println();

        int MoveCounter = 0;

        Move whiteMove, blackMove;
        while (true) {
            whiteMove = whitePlayer.play(board.clone());
            if (whiteMove == null || !board.doMove(whiteMove)) {
                System.out.println("number of Moves =" + MoveCounter);
                return blackPlayer;
            } else {
                System.out.println("white move = " + whiteMove);
                MoveCounter++;
            }
            System.out.println(board);
            if (board.isMated()) {
                System.out.println("number of Moves =" + MoveCounter);
                return whitePlayer;
            }
            if (board.isDraw()) {
                System.out.println("number of Moves =" + MoveCounter);
                System.out.println("black stale mate :" + board.isStaleMate());
                return null;
            }
            blackMove = blackPlayer.play(board.clone());
            if (blackMove == null || !board.doMove(blackMove)) {
                System.out.println("number of Moves =" + MoveCounter);
                return whitePlayer;
            } else {
                System.out.println("black move = " + blackMove);
            }
            System.out.println(board);
            if (board.isMated()) {
                System.out.println("number of Moves =" + MoveCounter);
                return blackPlayer;
            }
            if (board.isDraw()) {
                System.out.println("number of Moves =" + MoveCounter);
                System.out.println("white stale mate :" + board.isStaleMate());
                return null;
            }
        }
    }

}
