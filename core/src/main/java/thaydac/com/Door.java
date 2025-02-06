package thaydac.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Door extends MyActor{
    Door(float x, float y, Stage s) {
        super(x, y, s);
        TextureRegion tRegion = new TextureRegion(new Texture("door.png"));
        textureRegion = tRegion;
        setSize(32, 32);
    }
}
