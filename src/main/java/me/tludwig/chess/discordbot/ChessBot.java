package me.tludwig.chess.discordbot;

import me.tludwig.chess.discordbot.chessbot.DCChessGame;
import me.tludwig.chess.discordbot.commands.CommandHandler;
import me.tludwig.chess.game.move.AbstractMove;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class ChessBot {
	private static ChessBot INSTANCE = null;
	private JDA jda;
	public final Map<Long, DCChessGame> runningGames = new HashMap<>();

	public static void start(String token) {
		if (INSTANCE == null)
			INSTANCE = new ChessBot(token);
	}

	public ChessBot(String token) {
		JDABuilder builder = JDABuilder.createDefault(token);

		builder.setStatus(OnlineStatus.IDLE);
		builder.addEventListeners(new CommandHandler());

		builder.addEventListeners(new ListenerAdapter() {
			@Override
			public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
				long channelId = event.getChannel().getIdLong();

				if (!runningGames.containsKey(channelId)) return;

				DCChessGame game = runningGames.get(channelId);
				if (event.getAuthor().getIdLong() != game.idToMove()) return;

				AbstractMove m = AbstractMove.fromSAN(event.getMessage().getContentDisplay(), game.board);

				if (m == null || !m.isLegal(game.board)) {
					event.getChannel().sendMessage("Illegal move!");

					return;
				}

				game.board.apply(m);
				game.sendStatusMessage();
			}
		});

		try {
			jda = builder.build();
			jda.awaitReady();
			jda.getPresence().setStatus(OnlineStatus.ONLINE);
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static ChessBot getBot() {
		return INSTANCE;
	}

	public static JDA getJDA() {
		return INSTANCE.jda;
	}
}
