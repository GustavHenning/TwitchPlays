package tdfpro.twitchplays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Game;

public interface Entity {
    /**
     * Called after update every tick.
     *
     * @param c
     * @param game
     * @param g
     *            The graphics context.
     */
    void render(GameContainer c, Game game, Graphics g);

    /**
     * Called every tick, before rendering.
     *
     * @param c
     * @param game
     * @param delta
     *            Number of milliseconds since last update
     * @return true if the entity should be removed from the next update
     */
    boolean update(GameContainer c, Game game, int delta);
}
