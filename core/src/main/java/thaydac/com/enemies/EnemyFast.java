package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.*;
import thaydac.com.enemies.EnemyActor;

// Balloon
public class EnemyFast extends EnemyActor {
    public EnemyFast(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemyfast.png");
        setAnimation(texture, 11, 1, 0.3f);
        type = Utils.ENEMY_TYPE_FAST;
        smart = EnemySmart.HIGH;
        brickPass = true;
        speed = EnemySpeed.FAST;
        speedY = speed.getValue();
    }
}

