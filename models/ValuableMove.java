package models;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.LinkedList;
import java.util.List;

public class ValuableMove implements Comparable<ValuableMove> {

    Move move;
    int value;

    public ValuableMove(Move move, Board board) {
        this.move = move;
        value = MoveEvalFunctions.valueOfMovement(move, board);
    }

    public static List<ValuableMove> convertList(List<Move> moves, Board board) {
        List<ValuableMove> valuableMoves = new LinkedList<>();
        for (Move move : moves) {
            ValuableMove vm = new ValuableMove(move, board);
            valuableMoves.add(vm);
        }
        return valuableMoves;

    }

    public void addValue(int sum){
        value += sum;
    }

    @Override
    public int compareTo(ValuableMove valuableMove) {
        return valuableMove.value - this.value;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
