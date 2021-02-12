package me.tludwig.chess.discordbot.chessbot;

import me.tludwig.chess.discordbot.ChessBot;
import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.pieces.Alliance;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DCChessGame {
	public final Board board;
	private final long channelId, whitePlayerId, blackPlayerId;
	private long lastMessageId;
	private final String title;

	public DCChessGame(Board board, long channelId, long whitePlayerId, long blackPlayerId) {
		this.board = board;
		this.channelId = channelId;
		this.whitePlayerId = whitePlayerId;
		this.blackPlayerId = blackPlayerId;

		User white = ChessBot.getJDA().retrieveUserById(whitePlayerId).complete();
		User black = ChessBot.getJDA().retrieveUserById(blackPlayerId).complete();
		title = "Chess %s vs. %s".formatted(white.getName(), black.getName());

		TextChannel gameChannel = ChessBot.getJDA().getTextChannelById(channelId);
		Message message = gameChannel.sendMessage(new EmbedBuilder().setTitle(title).build()).mention(white, black).complete();
		lastMessageId = message.getIdLong();

		sendStatusMessage();
	}

	public void sendStatusMessage() {
		BufferedImage image = new BufferedImage(360, 360, BufferedImage.TYPE_INT_ARGB);
		board.draw(image.getGraphics());

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(title);
		builder.setImage("attachment://board.png");
		builder.setColor(board.toMove() == Alliance.WHITE ? Color.WHITE : Color.BLACK);

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			ChessBot.getJDA().getTextChannelById(channelId).deleteMessageById(lastMessageId).complete();
			Message message = ChessBot.getJDA().getTextChannelById(channelId).sendMessage(builder.build()).addFile(baos.toByteArray(), "board.png").complete();
			lastMessageId = message.getIdLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long idToMove() {
		return board.toMove() == Alliance.WHITE ? whitePlayerId : blackPlayerId;
	}
}
