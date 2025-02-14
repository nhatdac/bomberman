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
        speed = 0.5f;
        speedY = speed;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (isAlive) {
            // Lấy vị trí nhân vật chính
            float playerX = Master.man.getX();
            float playerY = Master.man.getY();

            // Lấy vị trí của Enemy5
            float distance = 0;

                // Kiểm tra nếu Enemy5 nhin thay NVC
                    if (getX() == playerX && !isWallBetween(getX(),getY(), Master.man.getX(), Master.man.getY(),true)) {
                        distance = Math.abs(getY() - playerY);
                        if (distance < 1000) {
                            speedX = 0;
                            if (playerY > getY()) {
                                speedY = speed;
                            } else {
                                speedY = -speed;
                            }
                        }
                    } else if (getY() == playerY && !isWallBetween(getX(),getY(), Master.man.getX(), Master.man.getY(),false)) {
                        distance = Math.abs(getX() - playerX);
                        if (distance < 1000) {
                            speedY = 0;
                            if (playerX > getX()) {
                                speedX = speed;
                            } else {
                                speedX = -speed;
                            }
                        }
                    } else {
                        float distanceX = Math.abs(getX() - playerX);
                        float distanceY = Math.abs(getY() - playerY);
                        if (distanceX < distanceY ) {
                            if(!isWallBetween(getX(),getY(), Master.man.getX(), getY(),false) && distanceX > 32){
                                speedY = 0;
                                if (playerX > getX() ) {
                                    speedX = speed;
                                } else {
                                    speedX = -speed;
                                }
                            }
                        }else{
                            if(!isWallBetween(getX(),getY(), getX(), Master.man.getY(),true) && distanceY > 32){
                                speedX = 0;
                                if (playerY > getX()) {
                                    speedY = speed;
                                } else {
                                    speedY = -speed;
                                }
                            }
                        }
                    }
            // Kiểm tra nếu Enemy5 "nhìn thấy" bomb
            for(Bomb b : Master.bombs){
                if (getX() == b.getX()) {
                    distance = Math.abs(getY() - b.getY());
                    if (distance < 32*GameState.bombPower+64 && !isWallBetween(getX(), getY() , b.getX() , b.getY(), true)) {
                        speedX = 0;
                        if (b.getY() < getY()) {
                            speedY = speed;
                        } else {
                            speedY = -speed;
                        }
                    }
                } else if (getY() == b.getY()) {
                    distance = Math.abs(getX() - b.getX());
                    if (distance < 32*GameState.bombPower+64 && !isWallBetween(getX(), getY() , b.getX(), b.getY(), false)) {
                        speedY = 0;
                        if (b.getX() < getX()) {
                            speedX = speed;
                        } else {
                            speedX = -speed;
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
