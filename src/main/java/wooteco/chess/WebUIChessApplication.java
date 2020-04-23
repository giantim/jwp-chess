package wooteco.chess;

import wooteco.chess.controller.WebChessController;

import static spark.Spark.staticFiles;

public class WebUIChessApplication {
    public static void main(String[] args) {
        staticFiles.location("/templates");
        WebChessController webChessController = new WebChessController();
        webChessController.playChess();
    }
}
