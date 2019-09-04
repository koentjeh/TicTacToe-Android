package com.koen.tictactoe.controller;

import android.util.Log;

import com.koen.tictactoe.controller.computer.IComputerController;
import com.koen.tictactoe.controller.computer.ImpossibleComputerController;
import com.koen.tictactoe.controller.computer.NormalComputerController;
import com.koen.tictactoe.model.Board;
import com.koen.tictactoe.model.Move;

public class GameController {
    private static final String TAG = "GameController";

    public enum Difficulties        { NONE, NORMAL, IMPOSSIBLE }
    public enum Figures             { BLANK, X, O }

    private Board                   board = new Board();
    private IComputerController     computer;
    private Figures                 turn;
    private int                     turnCount = 0;

    public GameController(Difficulties difficulty, Figures figure) {
        this.configureComputer(difficulty, figure);
        this.turn = figure;
    }

    public Figures getTurn() { return (this.turn != Figures.X) ? Figures.X : Figures.O; }

    public Move makeMove(int y, int x) {
        Move move = new Move(y, x, this.turn);
        board.updateBoard(move);
        move = isEndingMove(move);
        logMove(move);
        return move;
    }

    public Move makeMoveComputer() {
        Move move = computer.makeMove(board.getBoard());
        board.updateBoard(move);
        move = isEndingMove(move);
        logMove(move);
        return move;
    }

    private Move isEndingMove(Move move) {
        if (board.isWinningMove(move)) {
            move.setEndingMove("Player " + this.turn + " Has Won The Game");
        } else if (turnCount >= 9) {
            move.setEndingMove("The Game Ended In A Tie");
        } else nextTurn();
        return move;
    }

    private void nextTurn() {
        turnCount++;
        this.turn = (this.turn != Figures.X) ? Figures.X : Figures.O;
    }

    //--------------------------//
    //          COMPUTER        //
    //--------------------------//

    // Return IF Method Below Has Been Initialized
    public boolean isComputerConfigured() { return this.computer != null; }

    // Set Computer Difficulty And Figure
    private void configureComputer(Difficulties difficulty, Figures figure) {
        // Select Other Figure As Player Figure
        Figures f = (figure == Figures.X) ? Figures.O : Figures.X;

        switch (difficulty) {
            case NONE:
                this.computer = null;
                break;
            case NORMAL:
                this.computer = new NormalComputerController(f);
                break;
            case IMPOSSIBLE:
                this.computer = new ImpossibleComputerController(f);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + difficulty);
        }
    }



    private void logMove(Move move) {
        Log.i(TAG, "Move " + move.getFigure() + " = " + move.getY() + ":" + move.getX() + " | IsEndingMove?:" + move.isEndingMove() + " - " + move.getEndingMessage());
    }
}