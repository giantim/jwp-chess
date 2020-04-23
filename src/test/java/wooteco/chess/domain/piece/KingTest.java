package wooteco.chess.domain.piece;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.position.Position;

public class KingTest {
    @DisplayName("킹이 이동 가능한 곳인지 검사")
    @Test
    void movableTest() {
        Piece king = new King(PieceType.KING, Team.BLACK);
        Position source = Position.of("d4");
        Position movableTarget = Position.of("d5");
        Position nonMovableTarget = Position.of("c7");

        Assertions.assertThat(king.movable(source, movableTarget)).isTrue();
        Assertions.assertThat(king.movable(source, nonMovableTarget)).isFalse();
    }
}
