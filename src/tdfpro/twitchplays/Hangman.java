package tdfpro.twitchplays;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class Hangman implements Entity {
    private static final TrueTypeFont bigfont = new TrueTypeFont(
            new Font("Consolas", Font.PLAIN, 48), true);


    private int kappaCount;

    private String secret;
    private Set<String> guesses = new HashSet<>();

    private Map<String, Integer> currentRound = new HashMap<>();
    private TimerCounter roundTimer = new TimerCounter(30000);

    public Hangman(String secret, IRCReader irc){
        this.secret = secret.toUpperCase();
        irc.registerListener(s -> s.length() == 1, this::onLetter);
        irc.registerListener(s -> s.contains("Kappa"), this::onKappa);
        guesses.add(" ");
    }

    private void onLetter(String msg) {
        currentRound.compute(msg, (s, prev) -> (prev == null ? 0 : prev) + 1);
    }

    private void onKappa(String s) {
        kappaCount++;
    }

    @Override
    public void render(GameContainer c, Game s, Graphics g) {
        String status = Arrays.stream(secret.split(""))
                .map(letter -> guesses.contains(letter) ? letter : "_")
                .collect(Collectors.joining(" "));
        bigfont.drawString(50, 200, status);
        bigfont.drawString(50, 50, Integer.toString(kappaCount));
    }

    @Override
    public boolean update(GameContainer c, Game s, int delta) {
        if(roundTimer.update(delta)){
            roundTimer.reset(30000);

            currentRound.entrySet().stream()
                    .filter(e -> !guesses.contains(e.getKey()))
                    .max((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue()))
                    .map(Map.Entry::getKey).ifPresent(guess -> guesses.add(guess));

        }
        return false;
    }
}
