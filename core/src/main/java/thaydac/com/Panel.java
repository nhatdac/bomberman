package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Panel extends MyActor{
    Panel(float x, float y, Stage s) {
        super(x, y, s);
        // lấy khoảng giữa của viên đá để làm phần panel
        textureRegion = new TextureRegion(new Texture("wall.png"), 5, 5, 5, 5);
        setSize(31*32, 64);
    }
}
