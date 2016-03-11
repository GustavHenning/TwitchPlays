package tdfpro.twitchplays;

import org.newdawn.slick.*;

import java.io.IOException;

public class Main extends BasicGame {

    private IRCReader irc;
    private Config config;

	public Main() {
		super("Twitch Plays Hangman - by the TDF Team");
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer agc = new AppGameContainer(new Main());
		agc.setDisplayMode(1024, 720, false);
        agc.setAlwaysRender(true);
		agc.start();
	}

	@Override
	public void init(GameContainer gameContainer) throws SlickException {
        try {
            config = Config.loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
            gameContainer.exit();
        }

        irc = new IRCReader(config.username, config.oauthtoken, config.listenchannel);
	}

	@Override
	public void update(GameContainer gameContainer, int i) throws SlickException {
        if (gameContainer.getInput().isKeyPressed(Input.KEY_ESCAPE)){
            irc.disconnect();
            irc.dispose();
            gameContainer.exit();
        }
	}

	@Override
	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		graphics.drawString("Hello, world", 100, 100);
	}
}
