package wooteco.chess.domain;

import org.apache.commons.lang3.StringUtils;
import wooteco.chess.dao.PieceOnBoard;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardScore;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.strategy.direction.Direction;
import wooteco.chess.dto.BoardScoreDTO;
import wooteco.chess.dto.TileDTO;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChessRunner {
    private Board board;
    private Team currentTeam;

    public ChessRunner() {
        this.board = new Board();
        this.currentTeam = Team.WHITE;
    }

    public ChessRunner(Map<String, String> pieceOnBoards, String currentTeam) {
        this.board = Board.webBoard(pieceOnBoards);
        this.currentTeam = Team.valueOf(currentTeam);
    }

    public void update(String source, String target) {
        Position sourcePosition = Position.of(source);
        Position targetPosition = Position.of(target);
        Piece sourcePiece = getSourcePiece(sourcePosition);

        checkCorrectTurn(sourcePiece);
        checkUpdateBoard(sourcePosition, targetPosition, sourcePiece);

        updateBoard(sourcePosition, targetPosition);
        changeTeam();
    }

    private Piece getSourcePiece(Position source) {
        Optional<Piece> sourcePiece = this.board.getPiece(source);
        if (!sourcePiece.isPresent()) {
            throw new IllegalArgumentException("비어있는 위치를 선택했습니다.");
        }

        return sourcePiece.get();
    }

    private void checkCorrectTurn(Piece sourcePiece) {
        if (sourcePiece.isEnemy(this.currentTeam)) {
            throw new IllegalArgumentException("현재 차례가 아닙니다.");
        }
    }

    private void checkUpdateBoard(Position sourcePosition, Position targetPosition, Piece sourcePiece) {
        if (isSamePosition(sourcePosition, targetPosition)) {
            throw new IllegalArgumentException("같은 위치로 이동할 수 없습니다.");
        }

        if (!(sourcePiece.movable(sourcePosition, targetPosition))) {
            throw new IllegalArgumentException("선택한 기물이 이동할 수 없는 곳입니다.");
        }

        if (!isEmptyPath(sourcePosition, targetPosition)) {
            throw new IllegalArgumentException("경로 사이에 장애물이 있습니다.");
        }

        if (!isMovableTarget(sourcePiece, targetPosition)) {
            throw new IllegalArgumentException("목적지가 잘못되었습니다.");
        }
    }

    private boolean isSamePosition(final Position sourcePosition, final Position targetPosition) {
        return sourcePosition.equals(targetPosition);
    }

    private boolean isEmptyPath(final Position sourcePosition, final Position targetPosition) {
        Direction direction = Direction.findDirection(sourcePosition, targetPosition);
        List<Position> path = direction.findPath(sourcePosition, targetPosition);

        if (path.isEmpty()) {
            return true;
        }
        return path.stream()
                .allMatch(this.board::isEmpty);
    }

    private boolean isMovableTarget(final Piece sourcePiece, final Position targetPosition) {
        Optional<Piece> targetPiece = this.board.getPiece(targetPosition);
        return targetPiece.map(sourcePiece::isEnemy).orElse(true);
    }

    private void updateBoard(Position sourcePosition, Position targetPosition) {
        this.board.updateBoard(sourcePosition, targetPosition);
    }

    private void changeTeam() {
        this.currentTeam = this.currentTeam.changeTeam();
    }

    public double calculateScore() {
        BoardScore currentTeamScore = this.board.calculateScore(this.currentTeam);
        return currentTeamScore.getBoardScore();
    }

    public List<BoardScoreDTO> calculateScores() {
        List<BoardScoreDTO> scores = new ArrayList<>();
        BoardScoreDTO currentTeam = new BoardScoreDTO(calculateScore(), this.currentTeam.name());
        scores.add(currentTeam);

        BoardScore oppositeTeamScore = this.board.calculateScore(this.currentTeam.changeTeam());
        BoardScoreDTO oppositeTeam = new BoardScoreDTO(oppositeTeamScore.getBoardScore(),
                this.currentTeam.changeTeam().name());
        scores.add(oppositeTeam);

        return Collections.unmodifiableList(scores);
    }

    public boolean isEndChess() {
        return this.board.getWinner().isPresent();
    }

    public String getCurrentTeam() {
        return this.currentTeam.name();
    }

    public String getWinner() {
        Optional<Team> winner = this.board.getWinner();
        return winner.map(Enum::name).orElse(StringUtils.EMPTY);
    }

    public List<TileDTO> entireTileDtos() {
        List<String> positions = Position.getPositions();
        List<Integer> indexes = Position.getPositionsIndex();
        List<TileDTO> tileDTOS = IntStream.range(0, positions.size())
                .mapToObj((index) -> {
                    String position = positions.get(index);
                    int styleIndex = indexes.get(index);
                    if (this.board.contain(position)) {
                        Map.Entry<Position, Piece> entry = this.board.getEntry(position);
                        String pieceImageUrl = entry.getValue().toSymbol() + entry.getValue().teamName().toLowerCase();
                        return new TileDTO(position, pieceImageUrl, styleIndex);
                    }
                    return new TileDTO(position, StringUtils.EMPTY, styleIndex);
                }).collect(Collectors.toList());

        return Collections.unmodifiableList(tileDTOS);
    }

    public List<PieceOnBoard> getPieceOnBoards(int chessBoardId) {
        List<PieceOnBoard> pieces = this.board.getBoard().entrySet().stream()
                .map((entry) -> {
                    Position position = Position.of(entry.getKey().toString());
                    PieceType pieceType = PieceType.of(entry.getValue().toSymbol());
                    Team team = Team.valueOf(entry.getValue().teamName());
                    return new PieceOnBoard(position, pieceType, team, chessBoardId);
                }).collect(Collectors.toList());

        return Collections.unmodifiableList(pieces);
    }
}
