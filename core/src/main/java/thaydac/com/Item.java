package thaydac.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Item extends MyActor{
    int level = 0;
    Item(float x, float y, Stage s) {
        super(x, y, s);
        TextureRegion tRegion = new TextureRegion(new Texture("items.png"));
        tRegion.setRegion(32 * level, 0, 16, 16);
        textureRegion = tRegion;
        setSize(32, 32);
    }
}
