package wooteco.chess.controller;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.dao.ChessBoard;
import wooteco.chess.dao.CustomSQLException;
import wooteco.chess.dao.Player;
import wooteco.chess.dto.MoveResultDTO;
import wooteco.chess.dto.TeamDTO;
import wooteco.chess.dto.TileDTO;
import wooteco.chess.service.SparkChessService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class SparkChessController {
    private SparkChessService sparkChessService;

    public void playChess() {
        this.sparkChessService = new SparkChessService();

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            return render(model, "index.html");
        });

        post("/name", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            return render(model, "name.html");
        });

        post("/load", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            try {
                List<Player> players = this.sparkChessService.players();

                model.put("gameData", players);

                return render(model, "table.html");
            } catch (CustomSQLException e) {
                model.put("errMessage", e.getMessage());
                return render(model, "error.html");
            }
        });

        post("/newGame", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String whitePlayer = req.queryParams("white-player");
            String blackPlayer = req.queryParams("black-player");

            try {
                Player player = new Player(whitePlayer, blackPlayer);
                this.sparkChessService.newGame(player);
                List<TileDTO> tileDtos = this.sparkChessService.getTiles();
                TeamDTO teamDto = this.sparkChessService.getCurrentTeam();
                ChessBoard chessBoard = this.sparkChessService.getRecentChessBoard();

                model.put("tiles", tileDtos);
                model.put("currentTeam", teamDto);
                model.put("player", player);
                model.put("chessBoard", chessBoard);

                return render(model, "game.html");
            } catch (CustomSQLException e) {
                model.put("errMessage", e.getMessage());
                return render(model, "error.html");
            }
        });

        post("/continueGame", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            int chessBoardId = Integer.parseInt(req.queryParams("chess-board-id"));

            try {
                this.sparkChessService.continueGame(chessBoardId);
                List<TileDTO> tileDtos = this.sparkChessService.getTiles();
                TeamDTO teamDto = this.sparkChessService.getCurrentTeam();
                Player player = this.sparkChessService.getPlayer(chessBoardId);
                ChessBoard chessBoard = new ChessBoard(chessBoardId);

                model.put("tiles", tileDtos);
                model.put("currentTeam", teamDto);
                model.put("player", player);
                model.put("chessBoard", chessBoard);

                return render(model, "game.html");
            } catch (CustomSQLException e) {
                model.put("errMessage", e.getMessage());
                return render(model, "error.html");
            }
        });

        post("/move", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            int chessBoardId = Integer.parseInt(req.queryParams("chess-board-id"));
            String source = req.queryParams("source");
            String target = req.queryParams("target");

            try {
                MoveResultDTO moveResultDto = this.sparkChessService.move(chessBoardId, source, target);
                List<TileDTO> tileDtos = this.sparkChessService.getTiles();
                TeamDTO teamDto = this.sparkChessService.getCurrentTeam();
                Player player = this.sparkChessService.getPlayer(chessBoardId);
                ChessBoard chessBoard = new ChessBoard(chessBoardId);

                model.put("tiles", tileDtos);
                model.put("currentTeam", teamDto);
                model.put("message", moveResultDto.getMessage());
                model.put("style", moveResultDto.getStyle());
                model.put("player", player);
                model.put("chessBoard", chessBoard);

                if (this.sparkChessService.isEndGame()) {
                    this.sparkChessService.deleteChessGame(chessBoardId);
                    return render(model, "end.html");
                }
                return render(model, "game.html");
            } catch (CustomSQLException e) {
                model.put("errorMessage", e.getMessage());
                return render(model, "error.html");
            }
        });

        post("/status", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            int chessBoardId = Integer.parseInt(req.queryParams("chess-board-id-status"));

            try {
                List<TileDTO> tileDtos = this.sparkChessService.getTiles();
                TeamDTO teamDto = this.sparkChessService.getCurrentTeam();
                String message = this.sparkChessService.getScores();
                Player player = this.sparkChessService.getPlayer(chessBoardId);
                ChessBoard chessBoard = new ChessBoard(chessBoardId);

                model.put("tiles", tileDtos);
                model.put("currentTeam", teamDto);
                model.put("message", message);
                model.put("player", player);
                model.put("chessBoard", chessBoard);

                return render(model, "game.html");
            } catch (CustomSQLException e) {
                model.put("errorMessage", e.getMessage());
                return render(model, "error.html");
            }
        });

        post("/end", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            int chessBoardId = Integer.parseInt(req.queryParams("chess-board-id-end"));

            try {
                this.sparkChessService.deleteChessGame(chessBoardId);
                String message = this.sparkChessService.getScores();

                model.put("message", message);

                return render(model, "end.html");
            } catch (CustomSQLException e) {
                model.put("errorMessage", e.getMessage());
                return render(model, "error.html");
            }
        });
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
