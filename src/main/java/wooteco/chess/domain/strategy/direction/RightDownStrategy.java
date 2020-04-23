package wooteco.chess.domain.strategy.direction;

import wooteco.chess.domain.position.Position;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class RightDownStrategy implements DirectionStrategy {

    @Override
    public List<Position> findPath(final Position source, final Position target) {
        List<Position> path = IntStream.rangeClosed(source.getFile() + 1, target.getFile() - 1)
                .mapToObj(index -> Position.of(index, target.getRank() + (target.getFile() - index)))
                .collect(Collectors.toList());

        return Collections.unmodifiableList(path);
    }
}
