package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.Master;
import thaydac.com.MyActor;

// Balloon
public class Enemy5 extends EnemyActor {


    public Enemy5(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemy1.png");
        setAnimation(texture, 11, 1, 0.3f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(isAlive) {
            moveBy(speedX, speedY);
            for (MyActor wall : Master.walls) {
                if (wall.getBound().overlaps(getBound())) {
                    if (speedX == 1) {
                        moveBy(-1, 0);
                        speedX = 0;
                        speedY = MathUtils.random(-1, 1);
                        if (speedY == 0) {
                            speedX = -1;
                        }
                    } else if (speedX == -1) {
                        moveBy(1, 0);
                        speedX = 0;
                        speedY = MathUtils.random(-1, 1);
                        if (speedY == 0) {
                            speedX = 1;
                        }
                    } else if (speedY == 1) {
                        moveBy(0, -1);
                        speedY = 0;
                        speedX = MathUtils.random(-1, 1);
                        if (speedX == 0) {
                            speedY = -1;
                        }
                    } else if (speedY == -1) {
                        moveBy(0, 1);
                        speedY = 0;
                        speedX = MathUtils.random(-1, 1);
                        if (speedX == 0) {
                            speedY = 1;
                        }
                    }
                }
            }
        }
    }
}
