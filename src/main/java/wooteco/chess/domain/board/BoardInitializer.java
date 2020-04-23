package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.strategy.initialize.*;

import java.util.*;

public class BoardInitializer {
    private static final List<InitializeStrategy> INITIALIZER;

    static {
        INITIALIZER = new ArrayList<>(Arrays.asList(
                new KingInitializer(),
                new QueenInitializer(),
                new RookInitializer(),
                new KnightInitializer(),
                new BishopInitializer(),
                new PawnInitializer()
        ));
    }

    public static Map<Position, Piece> initializeAll() {
        Map<Position, Piece> board = new HashMap<>();

        for (InitializeStrategy strategy : INITIALIZER) {
            board.putAll(strategy.initialize());
        }

        return board;
    }

    public static Map<Position, Piece> webInitialize(Map<String, String> pieceOnBoards) {
        Map<Position, Piece> board = new HashMap<>();

        for (InitializeStrategy strategy : INITIALIZER) {
            board.putAll(strategy.initialize(pieceOnBoards));
        }

        return board;
    }
}
