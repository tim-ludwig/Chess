package me.tludwig.chess.bot;

import me.tludwig.chess.discordbot.ChessBot;
import me.tludwig.chess.discordbot.chessbot.DCChessGame;
import me.tludwig.chess.game.Board;

public class BotTest {
	public static void main(String[] args) {
		ChessBot.start("");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();

		}

		DCChessGame game = new DCChessGame(new Board(), 254967020695715841L, 238325775290466305L, 213245539930603520L);
		ChessBot.getBot().runningGames.put(254967020695715841L, game);
	}
}
