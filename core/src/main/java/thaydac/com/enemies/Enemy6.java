package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.*;

// Balloon
public class Enemy6 extends EnemyActor {

    public Enemy6(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemy5.png");
        setAnimation(texture, 11, 1, 0.3f);
        type = Utils.ENEMY_TYPE5;
        smart = EnemySmart.NORMAL;
        brickPass = true;
        speed = EnemySpeed.SLOW;
        speedY = speed.getValue();
    }
}
