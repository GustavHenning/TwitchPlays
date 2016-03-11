package tdfpro.twitchplays;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Sprite implements Entity {
    private Image image;

    private float x;
    private float y;
    private float scale;

    public Sprite(Image image, float x, float y, float scale) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    @Override
    public void render(GameContainer c, Game game, Graphics g) {
        image.draw(x,y, scale);
    }

    @Override
    public boolean update(GameContainer c, Game game, int delta) {
        return false;
    }
}
