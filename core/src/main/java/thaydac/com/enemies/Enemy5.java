package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.*;

// Balloon
public class Enemy5 extends EnemyActor {


    public Enemy5(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("enemy5.png");
        setAnimation(texture, 11, 1, 0.3f);
        type = Utils.ENEMY_TYPE5;
        speed = 1f;
        speedY = speed;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (isAlive) {
            // Lấy vị trí nhân vật chính
            float playerX = Master.man.getX();
            float playerY = Master.man.getY();

            // Lấy vị trí của Enemy2
            float enemyX = getX();
            float enemyY = getY();
            float distance = 0;

            // Kiểm tra nếu Enemy5 "nhìn thấy" nhân vật chính
            if (enemyX == playerX) {
                distance = Math.abs(enemyY - playerY);
                if (distance < 1000) {
                    speedX = 0;
                    if (playerY > enemyY) {
                        speedY = speed;
                    } else {
                        speedY = -speed;
                    }
                }
            } else if (enemyY == playerY) {
                distance = Math.abs(enemyX - playerX);
                if (distance < 1000) {
                    speedY = 0;
                    if (playerX > enemyX) {
                        speedX = speed;
                    } else {
                        speedX = -speed;
                    }
                }
            } else {
                float distanceX = Math.abs(enemyX - playerX);
                ;
                float distanceY = Math.abs(enemyY - playerY);
                if (distanceX < distanceY ) {
                    if(!isWallBetween(getX(),getY(), Master.man.getX(), getY(),false)){
                        speedY = 0;
                        if (playerX > enemyX ) {
                            speedX = speed;
                        } else {
                            speedX = -speed;
                        }
                    }
                }else{
                    if(!isWallBetween(getX(),getY(), getX(), Master.man.getY(),true)){
                        speedX = 0;
                        if (playerY > enemyY) {
                            speedY = speed;
                        } else {
                            speedY = -speed;
                        }
                    }
                }
            }
        }
    }
    private boolean isWallBetween(float x1, float y1, float x2, float y2, boolean isVertical) {
        for (MyActor wall : Master.walls) {
            if(wall instanceof Wall){
                float wallX = wall.getX();
                float wallY = wall.getY();

                if (isVertical) { // Kiểm tra theo cột
                    if ((wallX > x1 - 32 && wallX < x1 + 32) && ((wallY > y1 && wallY < y2) || (wallY < y1 && wallY > y2))) {
                        return true; // Có tường chắn
                    }
                } else { // Kiểm tra theo hàng
                    if ((wallY > y1 - 32 && wallY < y1 + 32) && ((wallX > x1 && wallX < x2) || (wallX < x1 && wallX > x2))) {
                        return true; // Có tường chắn
                    }
                }
            }
        }
        return false; // Không có tường chắn
    }
}
