package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.Master;
import thaydac.com.MyActor;
import thaydac.com.Utils;

// Balloon
public class Enemy3 extends EnemyActor {


    public Enemy3(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemy3.png");
        setAnimation(texture, 11, 1, 0.3f);
        type = Utils.ENEMY_TYPE3;
        speed = 2;
        speedX = speed;
    }
}
