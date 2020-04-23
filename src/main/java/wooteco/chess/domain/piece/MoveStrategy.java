package wooteco.chess.domain.piece;

import wooteco.chess.domain.position.Position;

public interface MoveStrategy {
    boolean movable(Position source, Position target);
}
