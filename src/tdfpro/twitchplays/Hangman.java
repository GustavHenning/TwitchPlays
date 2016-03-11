package tdfpro.twitchplays;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.awt.*;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Hangman implements Entity {
	private static final TrueTypeFont bigfont = new TrueTypeFont(new Font("Consolas", Font.PLAIN, 48), true);
	private static final TrueTypeFont smallfont = new TrueTypeFont(new Font("Consolas", Font.PLAIN, 28), true);

    public static final int ROUND_TIME_MS = 15000;

	private static final Comparator<Map.Entry<String, Integer>> sortingcomparator = (e1, e2) -> {
		int diff = -Integer.compare(e1.getValue(), e2.getValue());
		return diff == 0 ? e1.getKey().compareTo(e2.getKey()) : diff;
	};
	private static final Predicate<String> isalpha = Pattern.compile("[A-Za-z]").asPredicate();

	private enum GameState {
		PLAYING, WIN, LOSS;
	}

	private int kappaCount;

	private GameState state = GameState.PLAYING;

	private String secret;
	private Set<String> guesses = new HashSet<>();

	private Map<String, Integer> currentRound = new HashMap<>();
	private TimerCounter roundTimer = new TimerCounter(ROUND_TIME_MS);
	private Random r = new Random();

//	private Image armLeft, armRight, body, head, legs, bg;
	private List<Sprite> man;
	private Image bg;
	private static final float HANGMAN_X = 250f, HANGMAN_Y = 260f, ARMS_OFFSET_Y = 65f, HANGMAN_SCALE = 0.3f;



	public Hangman(String secret, IRCReader irc) {
		initImages();
		this.secret = secret == null ? nextWord() : secret.toUpperCase();
		irc.registerListener(isalpha, this::onLetter);
		irc.registerListener(s -> s.contains("Kappa"), this::onKappa);
		guesses.add(" ");
	}

	private void initImages() {
		try {
			Image armLeft = new Image("res/armSprite.png");
			Image armRight = armLeft.getFlippedCopy(true, false);
			Image body = new Image("res/chestSprite.png");
			Image head = new Image("res/faceSprite.png");
			Image legs = new Image("res/pantsSprite.png");

			man = new ArrayList<>();
			man.add(new Sprite(head, HANGMAN_X + 5f, HANGMAN_Y - 50f, HANGMAN_SCALE-0.05f));
			man.add(new Sprite(body, HANGMAN_X, HANGMAN_Y + 50f, HANGMAN_SCALE-0.15f));
			man.add(new Sprite(armLeft, HANGMAN_X - 50f, HANGMAN_Y + ARMS_OFFSET_Y, HANGMAN_SCALE));
			man.add(new Sprite(armRight, HANGMAN_X + 125f, HANGMAN_Y + ARMS_OFFSET_Y, HANGMAN_SCALE));
			man.add(new Sprite(legs, HANGMAN_X - 4f, HANGMAN_Y + 200f, HANGMAN_SCALE));

			bg = new Image("res/BBoardSprite.png");
		} catch (SlickException e) {
			System.err.println("Unable to load image elements");
			e.printStackTrace();
		}
	}

	private void onLetter(String msg) {
		currentRound.compute(msg.toUpperCase(), (s, prev) -> (prev == null ? 0 : prev) + 1);
	}

	private void onKappa(String s) {
		kappaCount++;
	}

	private int numWrongGuesses(){
		return (int) guesses.stream()
				.filter(isalpha)
				.filter(s -> !secret.contains(s))
				.count();
	}
	
	private String nextWord(){
		File f = new File("res/wordsEn.txt");

		long next = r.nextInt((int) (f.length() - 20)); /* last word not longer */
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			raf.seek(next);
			raf.readLine();
			String nextWord = raf.readLine();
			raf.close();
			return nextWord.toUpperCase();
		} catch (IOException e) {
			System.err.println("Could not find word list");
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	public void render(GameContainer c, Game s, Graphics g) {
		bg.draw(0, 0, Main.WIDTH, Main.HEIGHT);
		/* text */
		String status = Arrays.stream(secret.split("")).map(letter -> guesses.contains(letter) ? letter : "_")
				.collect(Collectors.joining(" "));

		bigfont.drawString(120f, 140f, status);

		bigfont.drawString(100f, 60f, Integer.toString(kappaCount));

        String wrongGuesses = guesses.stream()
                .filter(gu -> !secret.contains(gu)).sorted()
                .collect(Collectors.joining(" "));
        smallfont.drawString(120f, 100f, wrongGuesses);

        drawRightAligned(bigfont, 920, 60,  "T-" + Integer.toString(roundTimer.getMillis()/ 1000));
        List<String> curGuesses = currentRound.entrySet().stream()
                .filter(e -> !guesses.contains(e.getKey()))
                .sorted(sortingcomparator)
                .limit(5)
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.toList());

        for(int i = 0; i < curGuesses.size(); i++){
            drawRightAligned(smallfont, 920, 105 + 25 * i, curGuesses.get(i));
        }

		/* Hung man MingLee */
		man.stream().limit(numWrongGuesses()).forEach(spr -> spr.render(c, s, g));

		g.drawString("To play: type a single letter in the chat!", 100, 645);

		if (state == GameState.WIN){
			bigfont.drawString(500, 100, "WIN!");
		} else if (state == GameState.LOSS) {
			bigfont.drawString(500, 100, "LOSS!");
		}

	}

	@Override
	public boolean update(GameContainer c, Game s, int delta) {
		if(state == GameState.PLAYING){
			if (roundTimer.update(delta)) {
				roundTimer.reset(ROUND_TIME_MS);

				List<Map.Entry<String, Integer>> allGuesses = currentRound.entrySet().stream()
						.filter(e -> !guesses.contains(e.getKey()))
						.sorted(sortingcomparator)
						.collect(Collectors.toList());
				if (!allGuesses.isEmpty()){
					int max = allGuesses.get(0).getValue();

					List<String> guessCandidates = allGuesses.stream()
							.filter(str -> str.getValue() == max)
							.map(Map.Entry::getKey)
							.collect(Collectors.toList());
					String guess = guessCandidates.get((int) (Math.random() * guessCandidates.size()));

					guesses.add(guess);
					currentRound.clear();

					if (!guesses.isEmpty() && Arrays.stream(secret.split(""))
							.allMatch(l -> guesses.contains(l))){
						// win
						state = GameState.WIN;
						roundTimer.reset(2 * ROUND_TIME_MS);
					} else if (numWrongGuesses() == man.size()){
						// loss
						state = GameState.LOSS;
						roundTimer.reset(2 * ROUND_TIME_MS);
					}
				}
			}
		} else {
			if (roundTimer.update(delta)) {
				guesses.clear();
				guesses.add(" ");
				currentRound.clear();
				state = GameState.PLAYING;
				secret = nextWord();
			}
		}

		return false;
	}
    public static void drawRightAligned(org.newdawn.slick.Font font, float x, float y, String msg){
        int offset = font.getWidth(msg);
        font.drawString(x - offset, y, msg);
    }
}
