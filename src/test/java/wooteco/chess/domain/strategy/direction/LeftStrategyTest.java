package wooteco.chess.domain.strategy.direction;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.position.Position;

import java.util.List;

public class LeftStrategyTest {
    @DisplayName("좌측 이동 시 경로 구하기")
    @Test
    void findPathTest() {
        DirectionStrategy directionStrategy = new LeftStrategy();
        Position source = Position.of("g2");
        Position target = Position.of("a2");
        List<Position> path = directionStrategy.findPath(source, target);

        Assertions.assertThat(path).containsExactly(
                Position.of("b2"),
                Position.of("c2"),
                Position.of("d2"),
                Position.of("e2"),
                Position.of("f2")
        );
    }
}
