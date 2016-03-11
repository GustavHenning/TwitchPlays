package tdfpro.twitchplays;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Game;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class IRCReader extends PircBot implements Entity {
	/* irc related */
	private static final String IRC_HOSTNAME = "irc.twitch.tv";
	private static final int IRC_PORT = 6667;

	/* Entity related */
	private static final int MAX_MESSAGES = 40;
	private static final float CHAT_OFFSET_X = 600;
	private static final float CHAT_OFFSET_Y = 670;
	private static final float STRING_HEIGHT = 14;
	
	private LinkedList<String> chatMessages = new LinkedList<String>();

	/**
	 * Creates an IRCReader and connects to a channel based on the credentials
	 * submitted in it's parameters
	 * 
	 * @param username
	 * @param oauth
	 * @param channel
	 */
	public IRCReader(String username, String oauth, String channel) {
		try {
			// setVerbose(true);
			setName(username);
			connect(IRC_HOSTNAME, IRC_PORT, oauth);
			joinChannel(channel);
		} catch (IOException | IrcException e) {
			System.err.println("IRCReader: Could not connect to irc: " + IRC_HOSTNAME + ":" + IRC_PORT);
			e.printStackTrace();
		}
	}

	/**
	 * Incoming messages go through here
	 */
	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		 System.out.println(sender + ": " + message);
		chatMessages.addFirst(sender + ": " + message);
		if (chatMessages.size() > MAX_MESSAGES)
			chatMessages.removeLast();
	}

	@Override
	public void render(GameContainer c, Game game, Graphics g) {
		for(int i = 0; i < chatMessages.size(); i++){
			g.drawString(chatMessages.get(i), CHAT_OFFSET_X, CHAT_OFFSET_Y - i*STRING_HEIGHT);
		}
		
	}

	@Override
	public boolean update(GameContainer c, Game game, int delta) {
		return false;
	}
}
