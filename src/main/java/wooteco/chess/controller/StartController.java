package wooteco.chess.controller;

import wooteco.chess.domain.ChessRunner;

public class StartController extends GameController {
    public StartController() {
        super();
    }

    @Override
    public void execute(ChessRunner chessRunner, String input) {
        printBoard(chessRunner.getBoardEntities());
    }
}
