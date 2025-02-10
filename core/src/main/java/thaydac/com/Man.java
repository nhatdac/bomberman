package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Man extends MyActor {
    Animation<TextureRegion> animationLeft;
    Animation<TextureRegion> animationRight;
    Animation<TextureRegion> animationUp;
    Animation<TextureRegion> animationDown;
    Animation<TextureRegion> animationDie;

    float time;
    String direction = "R";

    Sound walking1;
    Sound walking2;
    float timeSound = 0;
    boolean isPlaying = false;
    boolean isAlive = true;

    Man(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("man1.png");
        int cot = 19;
        int hang = 1;
        TextureRegion[][] frameBuff = TextureRegion.split(texture, texture.getWidth() / cot, texture.getHeight() / hang);

        TextureRegion[] frames = new TextureRegion[cot * hang];
        int index = 0;
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < cot; j++) {
                frames[index++] = frameBuff[i][j];
            }
        }

        TextureRegion[] framesLeft = {frames[0], frames[1], frames[2]};
        TextureRegion[] framesDown = {frames[3], frames[4], frames[5]};
        TextureRegion[] framesRight = {frames[6], frames[7], frames[8]};
        TextureRegion[] framesUp = {frames[9], frames[10], frames[11]};
        TextureRegion[] framesDie = {frames[12], frames[13], frames[14], frames[15], frames[16], frames[17], frames[18]};

        animationLeft = new Animation<TextureRegion>(0.1f, framesLeft);
        animationRight = new Animation<TextureRegion>(0.1f, framesRight);
        animationDown = new Animation<TextureRegion>(0.1f, framesDown);
        animationUp = new Animation<TextureRegion>(0.1f, framesUp);
        animationDie = new Animation<TextureRegion>(0.3f, framesDie);

        animationLeft.setPlayMode(Animation.PlayMode.LOOP);
        animationRight.setPlayMode(Animation.PlayMode.LOOP);
        animationUp.setPlayMode(Animation.PlayMode.LOOP);
        animationDown.setPlayMode(Animation.PlayMode.LOOP);

        setSize(32, 32);

        time = 0;
        textureRegion = animationRight.getKeyFrame(time);

        walking1 = Gdx.audio.newSound(Gdx.files.internal("walking1.mp3"));
        walking2 = Gdx.audio.newSound(Gdx.files.internal("walking2.mp3"));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!Master.isFinished) {
            if (isAlive) {
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    direction = "L";
                    moveBy(-2, 0);
                    time += delta;
                    textureRegion = animationLeft.getKeyFrame(time);
                    playSoundWalking(1, delta);
                } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    direction = "R";
                    moveBy(2, 0);
                    time += delta;
                    textureRegion = animationRight.getKeyFrame(time);
                    playSoundWalking(1, delta);
                } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    direction = "D";
                    moveBy(0, -2);
                    time += delta;
                    textureRegion = animationDown.getKeyFrame(time);
                    playSoundWalking(2, delta);
                } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    direction = "U";
                    moveBy(0, 2);
                    time += delta;
                    textureRegion = animationUp.getKeyFrame(time);
                    playSoundWalking(2, delta);
                }
            } else {
                time += delta;
                textureRegion = animationDie.getKeyFrame(time);
            }
        }
    }

    public void playSoundWalking(int mode, float _delta) {
        timeSound += _delta;
        if (timeSound > 0.4f) {
            timeSound = 0f;
            isPlaying = false;
        }
        if (!isPlaying) {
            if (mode == 1) {
                walking1.play(0.2f);
            } else {
                walking2.play(0.2f);
            }
            isPlaying = true;
        }
    }
}
