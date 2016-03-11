package tdfpro.twitchplays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface Entity {
    /**
     * Called after update every tick.
     *
     * @param c
     * @param s
     * @param g
     *            The graphics context.
     */
    void render(GameContainer c, StateBasedGame s, Graphics g);

    /**
     * Called every tick, before rendering.
     *
     * @param c
     * @param s
     * @param delta
     *            Number of milliseconds since last update
     * @return true if the entity should be removed from the next update
     */
    boolean update(GameContainer c, StateBasedGame s, int delta);
}
