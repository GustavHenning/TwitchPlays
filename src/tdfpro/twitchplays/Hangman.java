package tdfpro.twitchplays;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class Hangman implements Entity {
	private static final TrueTypeFont bigfont = new TrueTypeFont(new Font("Consolas", Font.PLAIN, 48), true);

	private int kappaCount;

	private String secret;
	private Set<String> guesses = new HashSet<>();

	private Map<String, Integer> currentRound = new HashMap<>();
	private TimerCounter roundTimer = new TimerCounter(30000);

	private Image armLeft, armRight, body, head, legs, bg;
	private static final float HANGMAN_X = 250f, HANGMAN_Y = 260f, ARMS_OFFSET_Y = 65f, HANGMAN_SCALE = 0.3f;

	public Hangman(String secret, IRCReader irc) {
		initImages();
		this.secret = secret.toUpperCase();
		irc.registerListener(s -> s.length() == 1, this::onLetter);
		irc.registerListener(s -> s.contains("Kappa"), this::onKappa);
		guesses.add(" ");
	}

	private void initImages() {
		try {
			armLeft = new Image("res/armSprite.png");
			armRight = new Image("res/armSprite.png").getFlippedCopy(true, false);
			body = new Image("res/chestSprite.png");
			head = new Image("res/faceSprite.png");
			legs = new Image("res/pantsSprite.png");
			bg = new Image("res/BBoardSprite.png");
		} catch (SlickException e) {
			System.err.println("Unable to load image elements");
			e.printStackTrace();
		}
	}

	private void onLetter(String msg) {
		currentRound.compute(msg, (s, prev) -> (prev == null ? 0 : prev) + 1);
	}

	private void onKappa(String s) {
		kappaCount++;
	}

	@Override
	public void render(GameContainer c, Game s, Graphics g) {
		bg.draw(0, 0, Main.WIDTH, Main.HEIGHT);
		/* text */
		String status = Arrays.stream(secret.split("")).map(letter -> guesses.contains(letter) ? letter : "_")
				.collect(Collectors.joining(" "));
		bigfont.drawString(120f, 140f, status);
		bigfont.drawString(100f, 60f, Integer.toString(kappaCount));
		/* Hung man MingLee */
		armLeft.draw(HANGMAN_X - 50f, HANGMAN_Y + ARMS_OFFSET_Y, HANGMAN_SCALE);
		armRight.draw(HANGMAN_X + 125f, HANGMAN_Y + ARMS_OFFSET_Y, HANGMAN_SCALE);
		body.draw(HANGMAN_X, HANGMAN_Y + 50f, HANGMAN_SCALE-0.15f);
		legs.draw(HANGMAN_X - 4f, HANGMAN_Y + 200f, HANGMAN_SCALE);
		head.draw(HANGMAN_X + 5f, HANGMAN_Y - 50f, HANGMAN_SCALE-0.05f);
	}

	@Override
	public boolean update(GameContainer c, Game s, int delta) {
		if (roundTimer.update(delta)) {
			roundTimer.reset(30000);

			currentRound.entrySet().stream().filter(e -> !guesses.contains(e.getKey()))
					.max((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).map(Map.Entry::getKey)
					.ifPresent(guess -> guesses.add(guess));

		}
		return false;
	}
}
