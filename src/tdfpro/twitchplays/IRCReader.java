package tdfpro.twitchplays;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Game;
import org.newdawn.slick.util.Log;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class IRCReader extends PircBot implements Entity {
	/* irc related */
	private static final String IRC_HOSTNAME = "irc.twitch.tv";
	private static final int IRC_PORT = 6667;

	/* Entity related */
	private static final int MAX_MESSAGES = 10, MESSAGE_TOTAL_MAX_LENGTH = 35;
	private static final float CHAT_OFFSET_X = 650;
	private static final float CHAT_OFFSET_Y = 650;
	private static final float STRING_HEIGHT = 15f;

	private final LinkedList<String> chatMessages = new LinkedList<>();

	private Map<Predicate<String>, List<Consumer<IRCMessage>>> listeners = new HashMap<>();

	/**
	 * Creates an IRCReader and connects to a channel based on the credentials
	 * submitted in its parameters
	 *
	 * @param username
	 * @param oauth
	 * @param channel
	 */
	public IRCReader(String username, String oauth, String channel) throws IOException, IrcException {
		// setVerbose(true);
		setName(username);
		connect(IRC_HOSTNAME, IRC_PORT, oauth);
		joinChannel(channel);
	}

	public void registerListener(Predicate<String> pred, Consumer<IRCMessage> listener) {
		listeners.computeIfAbsent(pred, p -> new ArrayList<>());
		listeners.get(pred).add(listener);
	}

	/**
	 * Incoming messages go through here
	 */
	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		// System.out.println(sender + ": " + message);

		listeners.keySet().stream().filter(p -> p.test(message)).flatMap(p -> listeners.get(p).stream())
				.forEach(listener -> listener.accept(new IRCMessage(sender, message)));

		synchronized (chatMessages) {
			StringBuilder sb = new StringBuilder();
			sb.append(sender);
			sb.append(": ");
			int remainingLength = MESSAGE_TOTAL_MAX_LENGTH - sb.toString().length();
			remainingLength = remainingLength < 0 ? 0 : remainingLength;
			
			if (message.length() > remainingLength) {
				sb.append(message.substring(0, remainingLength));
			} else {
				sb.append(message);
			}
			chatMessages.addFirst(sb.toString());
			if (chatMessages.size() > MAX_MESSAGES) {
				chatMessages.removeLast();
			}
		}
	}

	@Override
	public void render(GameContainer c, Game game, Graphics g) {
		synchronized (chatMessages) {
			for (int i = 0; i < chatMessages.size(); i++) {
				String msg = chatMessages.get(i);
				g.drawString(msg, CHAT_OFFSET_X, CHAT_OFFSET_Y - i * STRING_HEIGHT);
			}
		}

	}

	@Override
	public boolean update(GameContainer c, Game game, int delta) {
		return false;
	}

	@Override
	protected void onDisconnect() {
		Log.info("Disconnected.");
	}
}
