package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.*;

// Balloon
public class Enemy1 extends EnemyActor {


    public Enemy1(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemy1.png");
        setAnimation(texture, 11, 1, 0.3f);
        type = Utils.ENEMY_TYPE1;
        smart = EnemySmart.LOW;
        speed = EnemySpeed.SLOW;
        speedX = speed.getValue();
    }
}
