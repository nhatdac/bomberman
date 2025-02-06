package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

// Balloon
public class Enemy1 extends MyActor {
    Animation<TextureRegion> animation;
    Animation<TextureRegion> animationDie;
    float time;
    String direction = "R";
    int speedX = 1;
    int speedY = 0;

    Enemy1(float x, float y, Stage s) {
        super(x, y, s);
        setSize(32, 32);

        int cot = 11;
        int hang = 1;
        Texture texture = new Texture("enemy1.png");
        TextureRegion[][] frameBuff = TextureRegion.split(texture, texture.getWidth() / cot, texture.getHeight() / hang);
        TextureRegion[] frames = new TextureRegion[cot * hang];
        int index = 0;
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < cot; j++) {
                frames[index++] = frameBuff[i][j];
            }
        }

        TextureRegion[] framesAlive = {frames[0], frames[1], frames[2], frames[3], frames[4], frames[5]};

        animation = new Animation<TextureRegion>(0.3f, framesAlive);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        time = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
        textureRegion = animation.getKeyFrame(time);
        moveBy(speedX, speedY);
        for (MyActor wall: Master.walls) {
            if(wall.getBound().overlaps(getBound())){
                if(speedX == 1){
                    moveBy(-1, 0);
                    speedX = 0;
                    speedY = MathUtils.random(0, 1) == 0 ? 0 : 1;
                    if(speedY == 0){
                        speedX = -1;
                    }
                } else if (speedX == -1){
                    moveBy(1, 0);
                    speedX = 0;
                    speedY = MathUtils.random(0, 1) == 0 ? 0 : 1;
                    if(speedY == 0){
                        speedX = 1;
                    }
                } else if (speedY == 1){
                    moveBy(0, -1);
                    speedY = 0;
                    speedX = MathUtils.random(0, 1) == 0 ? 0 : 1;
                    if(speedX == 0){
                        speedY = -1;
                    }
                } else if (speedY == -1){
                    moveBy(0, 1);
                    speedY = 0;
                    speedX = MathUtils.random(0, 1) == 0 ? 0 : 1;
                    if(speedX == 0){
                        speedY = 1;
                    }
                }
            }
        }
    }
}
