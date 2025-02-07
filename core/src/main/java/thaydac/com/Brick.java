package thaydac.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Brick extends MyActor{

    Animation<TextureRegion> animation;
    float time;
    boolean isFire = false;
    boolean hasItem = false;
    boolean hasDoor = false;

    Brick(float x, float y, Stage s) {
        super(x, y, s);

        int cot = 7;
        int hang = 1;

        Texture texture = new Texture("brich.png");
        TextureRegion[][] frameBuff = TextureRegion.split(texture, texture.getWidth()/cot, texture.getHeight()/hang);

        TextureRegion[] frames = new TextureRegion[cot*hang];
        int index = 0;
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < cot; j++) {
                frames[index++] = frameBuff[i][j];
            }
        }

        animation = new Animation<TextureRegion>(0.1f, frames);

        animation.setPlayMode(Animation.PlayMode.NORMAL);

        setSize(32, 32);

        time = 0;
        textureRegion = animation.getKeyFrame(time);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(isFire){
            time += delta;
            textureRegion = animation.getKeyFrame(time);
        }
        if(animation.isAnimationFinished(time)){
            isFire = false;
            if(hasItem){
                if(Master.level == 1){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_NUMBER, getStage());
                } else if(Master.level == 2){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_POWER, getStage());
                }

            } else if(hasDoor){
                Master.door = new Door(getX(), getY(), getStage());
            }
            remove();
            Master.walls.removeValue(this, true);
        }
    }
}
