package thaydac.com.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thaydac.com.*;

import java.util.Random;

public class EnemyActor extends MyActor {
    Animation<TextureRegion> animation;
    Animation<TextureRegion> animationDie;
    float time;
    String direction = "R";
    float speed = 0;
    float speedX = 0;
    float speedY = 0;
    boolean isAlive = true;

    int type = Utils.ENEMY_TYPE1; // mặc địch là loại 1, con bóng bay
    EnemySmart smart = EnemySmart.NORMAL; // mặc định là mức độ thông minh bình thường
    boolean brickPass = false;

    boolean speedUp = false;

    public EnemyActor(float x, float y, Stage s) {
        super(x, y, s);
        setSize(32, 32);
    }

    public void setAnimation(Texture texture, int cot, int hang, float frameDuration) {

        TextureRegion[][] frameBuff = TextureRegion.split(texture, texture.getWidth() / cot, texture.getHeight() / hang);
        TextureRegion[] frames = new TextureRegion[cot * hang];
        int index = 0;
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < cot; j++) {
                frames[index++] = frameBuff[i][j];
            }
        }

        TextureRegion[] framesAlive = {frames[0], frames[1], frames[2], frames[3], frames[4], frames[5], frames[6]};
        TextureRegion[] framesDie = {frames[7], frames[8], frames[9], frames[10]};

        animation = new Animation<TextureRegion>(frameDuration, framesAlive);
        animationDie = new Animation<TextureRegion>(frameDuration, framesDie);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        time = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
        if(isAlive) {
            moveBy(speedX, speedY);
                for (MyActor wall : Master.walls) {
                    if (!(wall instanceof Brick && brickPass) && wall.getBound().overlaps(getBound())) {
                        if (speedX == speed) {
                            moveBy(-speed, 0);
                            speedX = 0;
                            speedY = MathUtils.random(-1, 1) * speed;
                            if (speedY == 0) {
                                speedX = -speed;
                            }
                        } else if (speedX == -speed) {
                            moveBy(speed, 0);
                            speedX = 0;
                            speedY = MathUtils.random(-1, 1) * speed;
                            if (speedY == 0) {
                                speedX = speed;
                            }
                        } else if (speedY == speed) {
                            moveBy(0, -speed);
                            speedY = 0;
                            speedX = MathUtils.random(-1, 1) * speed;
                            if (speedX == 0) {
                                speedY = -speed;
                            }
                        } else if (speedY == -speed) {
                            moveBy(0, speed);
                            speedY = 0;
                            speedX = MathUtils.random(-1, 1) * speed;
                            if (speedX == 0) {
                                speedY = speed;
                            }
                        }
                        if(brickPass && wall instanceof Brick){
                            checkAndUpdateZIndex(this, wall);
                        }
                    }
                }
            for (Bomb b : Master.bombs) {
                if (b.getBound().overlaps(getBound())) {
                    if (speedX == speed) {
                        moveBy(-speed, 0);
                        speedX = 0;
                        speedY = MathUtils.random(-1, 1) * speed;
                        if (speedY == 0) {
                            speedX = -speed;
                        }
                    } else if (speedX == -speed) {
                        moveBy(speed, 0);
                        speedX = 0;
                        speedY = MathUtils.random(-1, 1) * speed;
                        if (speedY == 0) {
                            speedX = speed;
                        }
                    } else if (speedY == speed) {
                        moveBy(0, -speed);
                        speedY = 0;
                        speedX = MathUtils.random(-1, 1) * speed;
                        if (speedX == 0) {
                            speedY = -speed;
                        }
                    } else if (speedY == -speed) {
                        moveBy(0, speed);
                        speedY = 0;
                        speedX = MathUtils.random(-1, 1) * speed;
                        if (speedX == 0) {
                            speedY = speed;
                        }
                    }
                }
            }

            switch (smart) {
                case NORMAL -> {
                    // Lấy vị trí nhân vật chính
                    float playerX = Master.man.getX();
                    float playerY = Master.man.getY();

                    // Lấy vị trí của Enemy
                    float enemyX = getX();
                    float enemyY = getY();
                    float distance = 0;

                    // Kiểm tra nếu Enemy2 "nhìn thấy" nhân vật chính
                    if (enemyX == playerX) {
                        distance = Math.abs(enemyY - playerY);
                        if (distance < 150 && !isWallBetween(enemyX, enemyY, playerX, playerY, true)) {
                            if(!speedUp && MathUtils.randomBoolean()){
                                speed *=2;
                                speedUp = true;
                            }
                            speedX = 0;
                            if (playerY > enemyY) {
                                speedY = speed;
                            } else {
                                speedY = -speed;
                            }
                        }
                    } else if (enemyY == playerY) {
                        distance = Math.abs(enemyX - playerX);
                        if (distance < 150 && !isWallBetween(enemyX, enemyY, playerX, playerY, false)) {
                            if(!speedUp && MathUtils.randomBoolean()){
                                speed *=2;
                                speedUp = true;
                            }
                            speedY = 0;
                            if (playerX > enemyX) {
                                speedX = speed;
                            } else {
                                speedX = -speed;
                            }
                        }
                    } else {
                        if(speedUp){
                            speed /= 2;
                            speedUp = false;
                        }
                    }
                    // Đặt lại speedX, speedY cho đúng để không bị trôi lúc chạm tường
                    if(speedX != 0){
                        speedX = (speedX/Math.abs(speedX))*speed;
                    } else if (speedY != 0) {
                        speedY = (speedY/Math.abs(speedY))*speed;
                    }
                }
                case HIGH -> {
                    // Lấy vị trí nhân vật chính
                    float playerX = Master.man.getX();
                    float playerY = Master.man.getY();

                    // Lấy vị trí của Enemy5
                    float distance = 0;

                    // Kiểm tra nếu Enemy5 nhin thay NVC
                    if (getX() == playerX && !isWallBetween(getX(), getY(), Master.man.getX(), Master.man.getY(), true)) {
                        distance = Math.abs(getY() - playerY);
                        if (distance < 1000) {
                            speedX = 0;
                            if (playerY > getY()) {
                                speedY = speed;
                            } else {
                                speedY = -speed;
                            }
                        }
                    } else if (getY() == playerY && !isWallBetween(getX(), getY(), Master.man.getX(), Master.man.getY(), false)) {
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
                        if (distanceX < distanceY) {
                            if (!isWallBetween(getX(), getY(), Master.man.getX(), getY(), false) && distanceX > 32) {
                                speedY = 0;
                                if (playerX > getX()) {
                                    speedX = speed;
                                } else {
                                    speedX = -speed;
                                }
                            }
                        } else {
                            if (!isWallBetween(getX(), getY(), getX(), Master.man.getY(), true) && distanceY > 32) {
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
                    for (Bomb b : Master.bombs) {
                        if (getX() == b.getX()) {
                            distance = Math.abs(getY() - b.getY());
                            if (distance < 32 * GameState.bombPower + 64 && !isWallBetween(getX(), getY(), b.getX(), b.getY(), true)) {
                                speedX = 0;
                                if (b.getY() < getY()) {
                                    speedY = speed;
                                } else {
                                    speedY = -speed;
                                }
                            }
                        } else if (getY() == b.getY()) {
                            distance = Math.abs(getX() - b.getX());
                            if (distance < 32 * GameState.bombPower + 64 && !isWallBetween(getX(), getY(), b.getX(), b.getY(), false)) {
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
                default -> {
                    // chỉ di chuyển mặc định
                }
            }

            textureRegion = animation.getKeyFrame(time);
            for (Explosion explosion : Master.explosions) {
                if (getBound().overlaps(explosion.getBound())) {
                    isAlive = false;
                    time = 0;
                    break;
                }
            }
        } else {
            textureRegion = animationDie.getKeyFrame(time);
            if (animationDie.isAnimationFinished(time)) {
                int score = switch (type) {
                    case Utils.ENEMY_TYPE1 -> 100;
                    case Utils.ENEMY_TYPE2 -> 200;
                    case Utils.ENEMY_TYPE3 -> 400;
                    case Utils.ENEMY_TYPE4 -> 800;
                    case Utils.ENEMY_TYPE5 -> 1000;
                    case Utils.ENEMY_TYPE6 -> 2000;
                    case Utils.ENEMY_TYPE7 -> 4000;
                    case Utils.ENEMY_TYPE_FAST -> 8000;
                    default -> 100;
                };
                FloatingScore scoreEffect = new FloatingScore(getX(), getY(), score);
                getStage().addActor(scoreEffect);
                GameState.score += score;
                remove();
                Master.enemies.removeValue(this, true);
            }
        }

        if (Master.item != null && getBound().overlaps(Master.item.getBound())) {
            checkAndUpdateZIndex(this, Master.item);
        }
        if (Master.door != null && getBound().overlaps(Master.door.getBound())) {
            checkAndUpdateZIndex(this, Master.door);
        }
    }

    private boolean isWallBetween(float x1, float y1, float x2, float y2, boolean isVertical) {
        for (MyActor wall : Master.walls) {
            if (!brickPass || (brickPass && wall instanceof Wall)) {
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
