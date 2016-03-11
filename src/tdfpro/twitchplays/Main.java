package tdfpro.twitchplays;

import org.newdawn.slick.*;

public class Main extends BasicGame {
	public Main() {
		super("Twitch Plays Hangman - by the TDF Team");
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer agc = new AppGameContainer(new Main());
		agc.setDisplayMode(1024, 720, false);
		agc.start();
	}

	@Override
	public void init(GameContainer gameContainer) throws SlickException {

	}

	@Override
	public void update(GameContainer gameContainer, int i) throws SlickException {

	}

	@Override
	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		graphics.drawString("Hello, world", 100, 100);
	}
}
