package thaydac.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

// dumpling
public class Enemy2 extends MyActor {
    Animation<TextureRegion> animation;
    Animation<TextureRegion> animationDie;
    float time;
    String direction = "R";
    int speedX = 1;
    int speedY = 0;

    Enemy2(float x, float y, Stage s) {
        super(x, y, s);
        setSize(32, 32);

        int cot = 11;
        int hang = 1;
        Texture texture = new Texture("enemy2.png");
        TextureRegion[][] frameBuff = TextureRegion.split(texture, texture.getWidth() / cot, texture.getHeight() / hang);
        TextureRegion[] frames = new TextureRegion[cot * hang];
        int index = 0;
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < cot; j++) {
                frames[index++] = frameBuff[i][j];
            }
        }

        TextureRegion[] framesAlive = {frames[0], frames[1], frames[2], frames[3], frames[4], frames[5],  frames[6]};

        animation = new Animation<TextureRegion>(0.3f, framesAlive);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        time = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
        textureRegion = animation.getKeyFrame(time);

        // Lấy vị trí nhân vật chính
        float playerX = Master.man.getX();
        float playerY = Master.man.getY();

        // Lấy vị trí của Enemy2
        float enemyX = getX();
        float enemyY = getY();
        float distance = 0;

        // Kiểm tra nếu Enemy2 "nhìn thấy" nhân vật chính
        if(enemyX == playerX){
            distance = Math.abs(enemyY - playerY);
            if (distance < 150 && !isWallBetween(enemyX, enemyY, playerX, playerY, true)){
                speedX = 0;
                if (playerY > enemyY) {
                    speedY = 1;
                } else {
                    speedY = -1;
                }
            }
        } else if (enemyY == playerY) {
            distance = Math.abs(enemyX - playerX);
            if (distance < 150 && !isWallBetween(enemyX, enemyY, playerX, playerY, false)){
                speedY = 0;
                if (playerX > enemyX) {
                    speedX = 1;
                } else {
                    speedX = -1;
                }
            }
        }

        moveBy(speedX, speedY);
        for (MyActor wall: Master.walls) {
            if(wall.getBound().overlaps(getBound())){
                if(speedX == 1){
                    moveBy(-1, 0);
                    speedX = 0;
                    speedY = MathUtils.random(-1, 1);
                    if(speedY == 0){
                        speedX = -1;
                    }
                } else if (speedX == -1){
                    moveBy(1, 0);
                    speedX = 0;
                    speedY = MathUtils.random(-1, 1);
                    if(speedY == 0){
                        speedX = 1;
                    }
                } else if (speedY == 1){
                    moveBy(0, -1);
                    speedY = 0;
                    speedX = MathUtils.random(-1, 1);
                    if(speedX == 0){
                        speedY = -1;
                    }
                } else if (speedY == -1){
                    moveBy(0, 1);
                    speedY = 0;
                    speedX = MathUtils.random(-1, 1);
                    if(speedX == 0){
                        speedY = 1;
                    }
                }
            }
        }
    }

    private boolean isWallBetween(float x1, float y1, float x2, float y2, boolean isVertical) {
        for (MyActor wall : Master.walls) {
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
        return false; // Không có tường chắn
    }
}
