package tdfpro.twitchplays;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Hangman implements Entity {

    private enum State {
        FINISHED, PLAYING;
    }

    private static final TrueTypeFont bigfont = new TrueTypeFont(
            new Font("Consolas", Font.PLAIN, 48), true);

//    private final IRCReader irc;
    private State gamestate = State.PLAYING;
    private String secret;
    private Set<String> guesses = new HashSet<>();
    private int kappaCount = 0;

    public Hangman(String secret, IRCReader irc){
        this.secret = secret.toUpperCase();
        irc.registerListener(s -> s.length() == 1, this::onLetter);
        irc.registerListener(s -> s.contains("Kappa"), this::onKappa);
        guesses.add(" ");
    }

    private void onLetter(String s) {
        guesses.add(s);
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
        return false;
    }
}
