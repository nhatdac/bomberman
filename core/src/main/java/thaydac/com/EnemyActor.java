package thaydac.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class EnemyActor extends MyActor{
    Animation<TextureRegion> animation;
    Animation<TextureRegion> animationDie;
    float time;
    String direction = "R";
    int speedX = 1;
    int speedY = 0;
    boolean isAlive = true;

    EnemyActor(float x, float y, Stage s) {
        super(x, y, s);
        setSize(32, 32);
    }

    public void setAnimation(Texture texture, int cot, int hang, float frameDuration){

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
            if(animationDie.isAnimationFinished(time)){
                remove();
                Master.enemies.removeValue(this, true);
            }
        }

        if(Master.item != null && getBound().overlaps(Master.item.getBound())){
            checkAndUpdateZIndex(this, Master.item);
        }
        if(Master.door != null && getBound().overlaps(Master.door.getBound())){
            checkAndUpdateZIndex(this, Master.door);
        }
    }
}
