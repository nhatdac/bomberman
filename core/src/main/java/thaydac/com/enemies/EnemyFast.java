package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.Master;
import thaydac.com.MyActor;
import thaydac.com.Utils;

// Pontan
public class EnemyFast extends EnemyActor {

    public EnemyFast(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemyfast.png");
        setAnimation(texture, 11, 1, 0.05f);
        type = Utils.ENEMY_TYPE_FAST;
        speed = 3;
        speedX = 0;
    }
}
