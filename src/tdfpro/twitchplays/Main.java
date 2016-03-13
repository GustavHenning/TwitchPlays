package tdfpro.twitchplays;

import org.jibble.pircbot.IrcException;
import org.newdawn.slick.*;

import java.io.IOException;

public class Main extends BasicGame {
	
    public static final int WIDTH = 1024, HEIGHT = 720;

    private IRCReader irc;
    private Config config;
    private Hangman hangman;

    public Main() {
        super("Twitch Plays Hangman - by the TDF Team");
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer agc = new AppGameContainer(new Main());
        agc.setDisplayMode(WIDTH, HEIGHT, false);
        agc.setAlwaysRender(true);
        agc.start();
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        try {
            config = Config.loadConfig();
            irc = new IRCReader(config.username, config.oauthtoken, config.listenchannel);
        } catch (IOException | IrcException e) {
            e.printStackTrace();
            gameContainer.exit();
        }

        hangman = new Hangman(null, irc);
    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            irc.disconnect();
            irc.dispose();
            gc.exit();
        }
        hangman.update(gc, this, delta);
    }

    @Override
    public void render(GameContainer c, Graphics g) throws SlickException {
        hangman.render(c, null, g);
        irc.render(c, this, g);
    }
}
