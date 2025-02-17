package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.*;

import java.util.Random;

// Oneal
public class Enemy2 extends EnemyActor {

    public Enemy2(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemy2.png");
        setAnimation(texture, 11, 1, 0.3f);
        type = Utils.ENEMY_TYPE2;
        smart = EnemySmart.NORMAL;

        speed = EnemySpeed.NORMAL;
        speedX = speed.getValue();
    }
}
