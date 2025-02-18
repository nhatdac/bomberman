//package thaydac.com.enemies;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import thaydac.com.Master;
//import thaydac.com.MyActor;
//import thaydac.com.Utils;
//
//// Pontan
//public class Enemy8 extends EnemyActor {
//
//    public Enemy8(float x, float y, Stage s) {
//        super(x, y, s);
//        Texture texture = new Texture("enemyfast.png");
//        setAnimation(texture, 11, 1, 0.3f);
//        type = Utils.ENEMY_TYPE_FAST;
//        brickPass = true;
//        speed = 4;
//        speedX = 0;
//    }
//}
package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.*;

// Balloon
public class Enemy8 extends EnemyActor {

    public Enemy8(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemyfast.png");
        setAnimation(texture, 11, 1, 0.3f);
        type = Utils.ENEMY_TYPE5;
        smart = EnemySmart.HIGH;
        brickPass = true;
        speed = 4f;
        speedY = speed;
    }
}
