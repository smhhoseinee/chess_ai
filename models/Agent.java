package models;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.List;
import java.util.Random;

public class Agent extends Player {

    private final static int maxDepth = 3;

    public Agent(Side side) {
        super(side);
    }

    public int eval(Board board) {

        //Eval(s) = w1 f1(s) + w2 f2(s) + … + wnfn(s)

        return 0;
    }

    @Override
    public Move play(Board board) {
        List<Move> moves = board.legalMoves();
        Move bestMove = null;
        int bestMoveValue = Integer.MIN_VALUE;
        for (Move move : moves) {
            if (board.doMove(move)) {
//                int temp = min(board.clone(), 1);
                int temp = minAlphaBetaPrune(board.clone(), 1, Integer.MIN_VALUE ,Integer.MAX_VALUE );
                if (temp > bestMoveValue) {
                    bestMove = move;
                    bestMoveValue = temp;
                }
                board.undoMove();
            }
        }
        if (bestMove == null) {
            Random random = new Random();
            return moves.get(random.nextInt(moves.size()));
        }
        return bestMove;
    }

    public int max(Board board, int depth) {
        if (depth == maxDepth) {
            return eval(board);
        }
        List<Move> moves = board.legalMoves();
        int bestValue = Integer.MIN_VALUE;
        for (Move move : moves) {
            if (board.doMove(move)) {
                if (board.isMated()) {
                    return Integer.MAX_VALUE;
                }
                bestValue = Integer.max(min(board.clone(), depth + 1), bestValue);
                board.undoMove();
            }
        }
        return bestValue;
    }

    public int min(Board board, int depth) {
        if (depth == maxDepth) {
            return eval(board);
        }
        List<Move> moves = board.legalMoves();
        int bestValue = Integer.MAX_VALUE;
        for (Move move : moves) {
            if (board.doMove(move)) {
                if (board.isMated()) {
                    return Integer.MIN_VALUE;
                }
                bestValue = Integer.min(max(board.clone(), depth + 1), bestValue);
                board.undoMove();
            }
        }
        return bestValue;
    }


    public int maxAlphaBetaPrune(Board board, int depth, int alpha, int beta) {
        if (depth == maxDepth) {
            return eval(board);
        }
        List<Move> moves = board.legalMoves();
        int bestValue = Integer.MIN_VALUE;
        for (Move move : moves) {
            if (board.doMove(move)) {
                if (board.isMated()) {
                    return Integer.MAX_VALUE;
                }
                bestValue = Integer.max(minAlphaBetaPrune(board.clone(), depth + 1, alpha, beta), bestValue);
                board.undoMove();

                // alpha beta prune
                if (bestValue >= beta) return bestValue;
                alpha = Integer.max(alpha, bestValue);


            }
        }
        return bestValue;
    }

    public int minAlphaBetaPrune(Board board, int depth, int alpha, int beta) {
        if (depth == maxDepth) {
            return eval(board);
        }
        List<Move> moves = board.legalMoves();
        int bestValue = Integer.MAX_VALUE;
        for (Move move : moves) {
            if (board.doMove(move)) {
                if (board.isMated()) {
                    return Integer.MIN_VALUE;
                }
                bestValue = Integer.min(maxAlphaBetaPrune(board.clone(), depth + 1, alpha, beta), bestValue);
                board.undoMove();

                // alpha beta prune
                if (bestValue <= alpha) return bestValue;
                beta = Integer.min(beta, bestValue);
            }
        }
        return bestValue;
    }


}
