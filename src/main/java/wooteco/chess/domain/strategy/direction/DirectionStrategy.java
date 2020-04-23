package wooteco.chess.domain.strategy.direction;

import wooteco.chess.domain.position.Position;

import java.util.List;

public interface DirectionStrategy {
    List<Position> findPath(Position source, Position target);
}