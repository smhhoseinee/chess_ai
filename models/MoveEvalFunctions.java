package models;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;


public class MoveEvalFunctions {

    static int pawnValue = 1;
    static int knightValue = 3;
    static int bishopValue = 3;
    static int rookValue = 5;
    static int queenValue = 9;
    static int kingValue = 200;

    public static int valueOfMovement(Move move, Board board) {

        int moveValueFromVictimPiece = 0;

        int sideEffect;
        int pieceValue = 0;


        Piece attackerPiece = board.getPiece(move.getFrom());
        Piece victimPiece = board.getPiece(move.getTo());

        if (Piece.NONE.equals(victimPiece)) {
            //do nothing caz there is nothing in dest
            moveValueFromVictimPiece = 0;
        } else {
            if (victimPiece.getPieceSide().equals(Side.BLACK)) { //it's black so has positive affect
                sideEffect = 1;
            } else { // else it is white thus it must be negatvie cas our pice is going to be attacked by enemy
                sideEffect = -1;
            }

            if (PieceType.PAWN.equals(victimPiece.getPieceType())) {
                pieceValue = pawnValue;
            } else if (PieceType.BISHOP.equals(victimPiece.getPieceType())) {
                pieceValue = bishopValue;
            } else if (PieceType.KNIGHT.equals(victimPiece.getPieceType())) {
                pieceValue = knightValue;
            } else if (PieceType.QUEEN.equals(victimPiece.getPieceType())) {
                pieceValue = queenValue;
            } else if (PieceType.ROOK.equals(victimPiece.getPieceType())) {
                pieceValue = rookValue;
            } else if (PieceType.KING.equals(victimPiece.getPieceType())) {
                pieceValue = kingValue;
            }

            moveValueFromVictimPiece += (pieceValue * sideEffect);
        }

        return moveValueFromVictimPiece;

    }

}
