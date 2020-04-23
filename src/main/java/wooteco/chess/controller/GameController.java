package wooteco.chess.controller;

import wooteco.chess.domain.ChessRunner;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.BoardDTO;
import wooteco.chess.dto.PositionDTO;
import wooteco.chess.view.ConsoleOutputView;
import wooteco.chess.view.OutputView;

import java.util.Map;

public abstract class GameController {
    protected final OutputView outputView;

    public GameController() {
        this.outputView = new ConsoleOutputView();
    }

    protected void printBoard(final Map<String, String> board) {
        BoardDTO boardDto = new BoardDTO(board);
        PositionDTO positionDto = new PositionDTO(Position.getPositions());
        this.outputView.printBoard(positionDto.getPositions(), boardDto.get());
    }

    public abstract void execute(ChessRunner chessRunner, String input);
}
