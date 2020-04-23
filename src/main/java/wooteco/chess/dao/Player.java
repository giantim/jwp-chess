package wooteco.chess.dao;

import java.util.Objects;

public class Player {
    private String whitePlayer;
    private String blackPlayer;
    private int chessBoardId;

    public Player(String whitePlayer, String blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public void setChessBoardId(int chessBoardId) {
        this.chessBoardId = chessBoardId;
    }

    public String getWhitePlayer() {
        return this.whitePlayer;
    }

    public String getBlackPlayer() {
        return this.blackPlayer;
    }

    public int getChessBoardId() {
        return this.chessBoardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return Objects.equals(this.whitePlayer, player.whitePlayer)
                && Objects.equals(this.blackPlayer, player.blackPlayer)
                && Objects.equals(this.chessBoardId, player.chessBoardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.whitePlayer, this.blackPlayer, this.chessBoardId);
    }
}
