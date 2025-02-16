package thaydac.com.enemies;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.EnemySmart;
import thaydac.com.Utils;

// Balloon
public class Enemy8 extends EnemyActor {


    public Enemy8(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemy8.png");
        setAnimation(texture, 11, 1, 0.3f);
        type = Utils.ENEMY_TYPE_FAST;
        smart = EnemySmart.HIGH;
        speed = 4;
        speedX = speed;
    }
}
