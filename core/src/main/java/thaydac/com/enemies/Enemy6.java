package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.EnemySmart;
import thaydac.com.Utils;
import thaydac.com.*;

public class Enemy6 extends EnemyActor {
    public Enemy6(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemy6.png");
        setAnimation(texture, 11, 1, 0.3f);
        type = Utils.ENEMY_TYPE6;
        smart = EnemySmart.NORMAL;
        brickPass = true;
        speed = EnemySpeed.SLOW;
        speedY = speed.getValue();
    }
}
