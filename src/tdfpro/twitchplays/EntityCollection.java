package tdfpro.twitchplays;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class EntityCollection<T extends Entity> implements Entity {

    private ArrayList<T> entities;

    /**
     * Creates a new empty collection.
     */
    public EntityCollection(){
        this(new ArrayList<T>());
    }

    /**
     * Constructs an EntityCollection from a list.
     * NB! The list is not copied, but is used directly.
     *
     * @param initial The initial entities in this collection.
     */
    public EntityCollection(ArrayList<T> initial){
        entities = initial;
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g) {
        entities.forEach(e -> e.render(c, s, g));
    }

    @Override
    public boolean update(GameContainer c, StateBasedGame s, int delta) {
        Iterator<T> it = entities.iterator();
        while (it.hasNext()) {
            if (it.next().update(c, s, delta)) {
                it.remove();
            }
        }
        return false;
    }

    public void add(T e){
        entities.add(e);
    }
}
