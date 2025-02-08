package thaydac.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

// Balloon
public class EnemyFast extends EnemyActor {

    EnemyFast(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemyfast.png");
        setAnimation(texture, 11, 1, 0.05f);
        speedX = 3;
        speedY = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(isAlive) {
            moveBy(speedX, speedY);
            for (MyActor wall : Master.walls) {
                if (wall.getBound().overlaps(getBound())) {
                    if (speedX == 3) {
                        moveBy(-3, 0);
                        speedX = 0;
                        speedY = MathUtils.random(-1, 1)*3;
                        if (speedY == 0) {
                            speedX = -3;
                        }
                    } else if (speedX == -3) {
                        moveBy(3, 0);
                        speedX = 0;
                        speedY = MathUtils.random(-1, 1)*3;
                        if (speedY == 0) {
                            speedX = 3;
                        }
                    } else if (speedY == 3) {
                        moveBy(0, -3);
                        speedY = 0;
                        speedX = MathUtils.random(-1, 1)*3;
                        if (speedX == 0) {
                            speedY = -3;
                        }
                    } else if (speedY == -3) {
                        moveBy(0, 3);
                        speedY = 0;
                        speedX = MathUtils.random(-1, 1)*3;
                        if (speedX == 0) {
                            speedY = 3;
                        }
                    }
                }
            }
        }
    }
}
