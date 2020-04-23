package wooteco.chess.domain.board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;

import java.util.*;

public class BoardScoreTest {
    @DisplayName("같은 파일에 폰이 있을 때 점수 계산")
    @Test
    void pawnStrategyTest() {
        List<Map.Entry<Position, Piece>> sameFilePawns = new ArrayList<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>(Position.of("a3"), new Pawn(PieceType.PAWN, Team.WHITE)),
                new AbstractMap.SimpleEntry<>(Position.of("a4"), new Pawn(PieceType.PAWN, Team.WHITE)),
                new AbstractMap.SimpleEntry<>(Position.of("a5"), new Pawn(PieceType.PAWN, Team.WHITE))
        ));

        BoardScore boardScore = new BoardScore(10d);

        Assertions.assertThat(boardScore.pawnStrategy(sameFilePawns).getBoardScore()).isEqualTo(8.5);
    }
}
