package models;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.*;

public class Agent extends Player {

    private final static int maxDepth = 5;
    public Map<String, Integer> hashFen = new HashMap<>();

    public Agent(Side side) {
        super(side);
    }

    public int eval(Board board) {

        // check transposition table whether this fen has been repeated or not
        if (hashFen.get(board.getFen()) != null) {
//            System.out.println("Fen=" + board.getFen());
            return hashFen.get(board.getFen());
        }

        int evalResult = 0;
        //Eval(s) = w1 f1(s) + w2 f2(s) + … + wnfn(s)

        int w1 = 18;
        int w2 = 2;
        int w3 = 5;
        //f1:
        evalResult += w1 * NodeEvalFunctions.valueOfPieces(board);
        evalResult += w2 * NodeEvalFunctions.numberOfPossibleMoves(board);
        evalResult += w3 * NodeEvalFunctions.queenPlace(board);

        hashFen.put(board.getFen(), evalResult);
        return evalResult;
    }

    public Square getQueenSquare(Side side, Board board) {
        Square result = Square.NONE;
        long piece = board.getBitboard(Piece.make(side, PieceType.KING));
        if (piece != 0L) {
            int sq = Bitboard.bitScanForward(piece);
            return Square.squareAt(sq);
        }


        return result;
    }

    public List<ValuableMove> checkPseudoTransitionTable(Board board, List<ValuableMove> valuableMoves) {

        List<ValuableMove> urgentValuableMoves = new LinkedList<>();

        for (int i = 7; i >= 0; i--) {
            Rank r = Rank.allRanks[i];
            for (int n = 0; n <= 7; n++) {
                File f = File.allFiles[n];
                if (!File.NONE.equals(f) && !Rank.NONE.equals(r)) {
                    Square sq = Square.encode(r, f);
                    Piece piece = board.getPiece(sq);
                    if (Piece.NONE.equals(piece)) {
                        // never mind
                    } else {

//                        if (board.squareAttackedBy(sq,side))


                        if (PieceType.PAWN.equals(piece.getPieceType())) {

                        } else if (PieceType.QUEEN.equals(piece.getPieceType())) {
                            long queenAttacked = board.squareAttackedBy(sq, side.flip());
                            if (queenAttacked != 0L) {
//                                for (ValuableMove vb : valuableMoves) {
                                for (int j = 0; j < valuableMoves.size(); j++) {

                                    if (valuableMoves.get(j).move.getFrom().equals(sq)) {
                                        valuableMoves.get(j).addValue(50);
                                        urgentValuableMoves.add(valuableMoves.get(j));
                                        valuableMoves.remove(valuableMoves.get(j));
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return urgentValuableMoves;
    }

    @Override
    public Move play(Board board) {

        List<Move> moves = board.legalMoves();

        //phase2 : node ordering
        List<ValuableMove> valuableMoves = ValuableMove.convertList(moves, board);
        Collections.sort(valuableMoves);

        //phase4 Transposition table
        List<ValuableMove> urgentValuableMoves = checkPseudoTransitionTable(board, valuableMoves);
        Collections.sort(urgentValuableMoves);

        if (urgentValuableMoves.size() > 0 && urgentValuableMoves.get(0) != null) {
            return urgentValuableMoves.get(0).getMove();
        }

        //phase3 : cut off
        valuableMoves = valuableMoves.subList(0, calculateCutOff(valuableMoves.size(), 1));

        Move bestMove = null;
        int bestMoveValue = Integer.MIN_VALUE;
//        for (Move move : moves) {
        for (ValuableMove valuableMove : valuableMoves) {
//            if (board.doMove(move)) {
            if (board.doMove(valuableMove.move)) {
//                int temp = min(board.clone(), 1);
                int temp = minAlphaBetaPrune(board.clone(), 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if (temp > bestMoveValue) {
//                    bestMove = move;
                    bestMove = valuableMove.move;
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

    public int maxAlphaBetaPrune(Board board, int depth, int alpha, int beta) {
        if (depth == maxDepth) {
            return eval(board);
        }
        List<Move> moves = board.legalMoves();

        //phase2 : node ordering
        List<ValuableMove> valuableMoves = ValuableMove.convertList(moves, board);
        Collections.sort(valuableMoves);

        //phase3 : cut off
        valuableMoves = valuableMoves.subList(0, calculateCutOff(valuableMoves.size(), depth));

        int bestValue = Integer.MIN_VALUE;
//        for (Move move : moves) {
        for (ValuableMove valuableMove : valuableMoves) {
//            if (board.doMove(move)) {
            if (board.doMove(valuableMove.move)) {
                if (board.isMated()) {
                    return Integer.MAX_VALUE;
                }
                bestValue = Integer.max(minAlphaBetaPrune(board.clone(), depth + 1, alpha, beta), bestValue);
//                bestValue = Integer.max(min(board.clone(), depth + 1), bestValue);
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

        //phase4 Transposition table


        List<Move> moves = board.legalMoves();

        //phase2 : node ordering
        List<ValuableMove> valuableMoves = ValuableMove.convertList(moves, board);
        Collections.sort(valuableMoves);
        Collections.reverse(valuableMoves);

        //phase3 : cut off
        valuableMoves = valuableMoves.subList(0, calculateCutOff(valuableMoves.size(), depth));

        int bestValue = Integer.MAX_VALUE;
//        for (Move move : moves) {
        for (ValuableMove valuableMove : valuableMoves) {
//            if (board.doMove(move)) {
            if (board.doMove(valuableMove.move)) {
                if (board.isMated()) {
                    return Integer.MIN_VALUE;
                }
                bestValue = Integer.min(maxAlphaBetaPrune(board.clone(), depth + 1, alpha, beta), bestValue);
//                bestValue = Integer.min(max(board.clone(), depth + 1), bestValue);
                board.undoMove();

                // alpha beta prune
                if (bestValue <= alpha) return bestValue;
                beta = Integer.min(beta, bestValue);
            }
        }
        return bestValue;
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

    public int calculateCutOff(int numberOfMoves, int depth) {
        int CutOff;
        int minCutOff = 15;
        int maxCutOff = 40;
        int alpha = 15; // alph% effect of numberofMoves
        int beta = 20; // beta% effect of depth
        if (numberOfMoves < minCutOff) {
            return numberOfMoves;
        } else {
            CutOff = Math.min(maxCutOff, numberOfMoves) + (alpha * numberOfMoves / 100) - beta * depth;
            return Math.max(CutOff, minCutOff);
        }
    }


}
