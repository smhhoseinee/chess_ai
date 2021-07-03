package models;

import com.github.bhlangonijr.chesslib.*;


public class NodeEvalFunctions {

    static int pawnValue = 1;
    static int knightValue = 3;
    static int bishopValue = 3;
    static int rookValue = 5;
    static int queenValue = 9;
    static int kingValue = 500;

    public static int valueOfPieces(Board board) {

//        System.out.println("WHITE_QUEEN :"+board.getBitboard(Piece.WHITE_QUEEN));
//        System.out.println("BLACK_QUEEN :"+board.getBitboard(Piece.BLACK_QUEEN));
//        System.out.println("BLACK_KNIGHT :"+board.getBitboard(Piece.BLACK_KNIGHT));
//        getBitboard(Piece.WHITE_QUEEN);


        int piecesRelativeValue = 0;


        for (int i = 7; i >= 0; i--) {
            Rank r = Rank.allRanks[i];
            for (int n = 0; n <= 7; n++) {
                int sideEffect;
                int pieceValue = 0;

                File f = File.allFiles[n];
                if (!File.NONE.equals(f) && !Rank.NONE.equals(r)) {
                    Square sq = Square.encode(r, f);
                    Piece piece = board.getPiece(sq);

                    if (Piece.NONE.equals(piece)) {
                        //do nothing
                    } else {
                        if (piece.getPieceSide().equals(Side.BLACK)) { //it's black so has negative affect
                            sideEffect = -1;
                        } else { // else it is white thus it must be positive
                            sideEffect = 1;
                        }

//                        if (piece.getPieceType().equals(PieceType.PAWN))
                        if (PieceType.PAWN.equals(piece.getPieceType())) {
                            pieceValue = pawnValue;
                        } else if (PieceType.BISHOP.equals(piece.getPieceType())) {
                            pieceValue = bishopValue;
                        } else if (PieceType.KNIGHT.equals(piece.getPieceType())) {
                            pieceValue = knightValue;
                        } else if (PieceType.QUEEN.equals(piece.getPieceType())) {
                            pieceValue = queenValue;
                        } else if (PieceType.ROOK.equals(piece.getPieceType())) {
                            pieceValue = rookValue;
                        } else if (PieceType.KING.equals(piece.getPieceType())) {
                            pieceValue = kingValue;
                        }

                        piecesRelativeValue += (pieceValue * sideEffect);
                    }

                }
            }
        }
        return piecesRelativeValue;
    }

    public static int numberOfPossibleMoves(Board board) {
        return board.legalMoves().size();
    }

    public static int queenPlace(Board board) {


        int piecesRelativeValue = 0;


        for (int i = 7; i >= 4; i--) {
            Rank r = Rank.allRanks[i];
            for (int n = 0; n <= 7; n++) {
                int sideEffect;
                int pieceValue = 0;

                File f = File.allFiles[n];
                if (!File.NONE.equals(f) && !Rank.NONE.equals(r)) {
                    Square sq = Square.encode(r, f);
                    Piece piece = board.getPiece(sq);

                    if (Piece.NONE.equals(piece)) {
                        //do nothing
                    } else {
                        if (piece.getPieceSide().equals(Side.BLACK)) { //it's black so has negative affect
                            sideEffect = -1;
                        } else { // else it is white thus it must be positive
                            sideEffect = 1;
                        }

//                        if (piece.getPieceType().equals(PieceType.PAWN))
                        if (PieceType.QUEEN.equals(piece.getPieceType())) {
                            pieceValue = queenValue;
                        }

                        piecesRelativeValue += (i * pieceValue * sideEffect);
                    }

                }
            }
        }
        return piecesRelativeValue;
    }

}
