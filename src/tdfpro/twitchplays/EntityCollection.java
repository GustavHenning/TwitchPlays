package tdfpro.twitchplays;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityCollection<T extends Entity> implements Entity {

    private ArrayList<T> entities;

    /**
     * Creates a new empty collection.
     */
    public EntityCollection() {
        this(new ArrayList<T>());
    }

    /**
     * Constructs an EntityCollection from a list.
     * NB! The list is not copied, but is used directly.
     *
     * @param initial The initial entities in this collection.
     */
    public EntityCollection(ArrayList<T> initial) {
        entities = initial;
    }

    @Override
    public void render(GameContainer c, Game game, Graphics g) {
        entities.forEach(e -> e.render(c, game, g));
    }

    @Override
    public boolean update(GameContainer c, Game game, int delta) {
        Iterator<T> it = entities.iterator();
        while (it.hasNext()) {
            if (it.next().update(c, game, delta)) {
                it.remove();
            }
        }
        return false;
    }

    public void add(T e) {
        entities.add(e);
    }
}
