package wooteco.chess.domain.strategy.direction;

import wooteco.chess.domain.position.Position;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class LeftStrategy implements DirectionStrategy {
    @Override
    public List<Position> findPath(final Position source, final Position target) {
        List<Position> path = IntStream.range(target.getFile() + 1, source.getFile())
                .mapToObj(index -> Position.of(index, source.getRank()))
                .collect(Collectors.toList());

        return Collections.unmodifiableList(path);
    }
}