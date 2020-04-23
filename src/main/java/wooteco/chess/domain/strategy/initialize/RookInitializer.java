package wooteco.chess.domain.strategy.initialize;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class RookInitializer implements InitializeStrategy {

    private static final String ROOK_SYMBOL = "r";

    private enum InitialRook {
        BLACK_LEFT(Position.of("a8"), new Rook(PieceType.ROOK, Team.BLACK)),
        BLACK_RIGHT(Position.of("h8"), new Rook(PieceType.ROOK, Team.BLACK)),
        WHITE_LEFT(Position.of("a1"), new Rook(PieceType.ROOK, Team.WHITE)),
        WHITE_RIGHT(Position.of("h1"), new Rook(PieceType.ROOK, Team.WHITE));

        private final Position position;
        private final Piece piece;

        InitialRook(Position position, Piece piece) {
            this.position = position;
            this.piece = piece;
        }

        public static Map<Position, Piece> initializeRooks() {
            Map<Position, Piece> rooks = Arrays.stream(values())
                    .collect(Collectors.toMap(entry -> entry.position, entry -> entry.piece,
                            (e1, e2) -> e1, HashMap::new));
            return Collections.unmodifiableMap(rooks);
        }
    }

    @Override
    public Map<Position, Piece> initialize() {
        return InitialRook.initializeRooks();
    }

    @Override
    public Map<Position, Piece> initialize(Map<String, String> pieceOnBoards) {
        Map<String, String> rooks = pieceOnBoards.entrySet().stream()
                .filter(entry -> entry.getValue().substring(0, 1).toLowerCase().equals(ROOK_SYMBOL))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        Map<Position, Piece> board = rooks.entrySet().stream()
                .collect(Collectors.toMap(entry -> Position.convert(entry.getKey()),
                        entry -> initializeRook(entry.getValue()),
                        (e1, e2) -> e1, HashMap::new));

        return Collections.unmodifiableMap(board);
    }

    private Piece initializeRook(String key) {
        String pieceTypeSymbol = key.substring(0, 1);
        String teamName = key.substring(1).toUpperCase();
        PieceType pieceType = PieceType.of(pieceTypeSymbol);
        Team team = Team.valueOf(teamName);
        return new Rook(pieceType, team);
    }
}