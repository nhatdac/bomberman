package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Wall extends MyActor{
    Wall(float x, float y, Stage s) {
        super(x, y, s);
        TextureRegion tRegion = new TextureRegion(new Texture("wall.png"));
        textureRegion = tRegion;
        setSize(32, 32);
    }
}
