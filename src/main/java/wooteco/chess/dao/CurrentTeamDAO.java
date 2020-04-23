package wooteco.chess.dao;

import wooteco.chess.domain.piece.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrentTeamDAO {
    public void addCurrentTeam(ChessBoard chessBoard, CurrentTeam currentTeam) {
        String query = "INSERT INTO currentTeam (team, chessBoardId) VALUES(?, ?)";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, currentTeam.getCurrentTeam());
            pstmt.setInt(2, chessBoard.getChessBoardId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomSQLException(e.getMessage(), e);
        }
    }

    public void updateCurrentTeam(ChessBoard chessBoard, CurrentTeam currentTeam) {
        String query = "UPDATE currentTeam SET team = ? WHERE chessBoardId = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, currentTeam.getCurrentTeam());
            pstmt.setInt(2, chessBoard.getChessBoardId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomSQLException(e.getMessage(), e);
        }
    }

    public void deleteCurrentTeam(ChessBoard chessBoard) {
        String query = "DELETE FROM currentTeam WHERE chessBoardId = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, chessBoard.getChessBoardId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomSQLException(e.getMessage(), e);
        }
    }

    public CurrentTeam findCurrentTeam(ChessBoard chessBoard) {
        String query = "SELECT team FROM currentTeam WHERE chessBoardId = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, chessBoard.getChessBoardId());
            ResultSet rs = pstmt.executeQuery();
            while (!rs.next()) {
                return null;
            }

            CurrentTeam currentTeam = new CurrentTeam(
                    Team.valueOf(rs.getString("team"))
            );
            ConnectionManager.closeResultSet(rs);
            return currentTeam;
        } catch (SQLException e) {
            throw new CustomSQLException(e.getMessage(), e);
        }
    }
}
