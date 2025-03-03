package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Objects;

public class Man extends MyActor {
    Animation<TextureRegion> animationLeft;
    Animation<TextureRegion> animationRight;
    Animation<TextureRegion> animationUp;
    Animation<TextureRegion> animationDown;
    Animation<TextureRegion> animationDie;
    Animation<TextureRegion> animationWin;

    float time;
    Direction direction = Direction.RIGHT;

    Sound walking1;
    Sound walking2;
    float timeSound = 0;
    boolean isPlaying = false;
    boolean isAlive = true;

    Man(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("man1.png");
        Texture textureWin = new Texture("man-win.png");
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

        TextureRegion[][] frameBuffWin = TextureRegion.split(textureWin, textureWin.getWidth() / 3, textureWin.getHeight() / 1);
        TextureRegion[] framesWin = new TextureRegion[3];
        int indexWin = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                framesWin[indexWin++] = frameBuffWin[i][j];
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
        animationWin = new Animation<TextureRegion>(0.3f, framesWin);

        animationLeft.setPlayMode(Animation.PlayMode.LOOP);
        animationRight.setPlayMode(Animation.PlayMode.LOOP);
        animationUp.setPlayMode(Animation.PlayMode.LOOP);
        animationDown.setPlayMode(Animation.PlayMode.LOOP);
        animationWin.setPlayMode(Animation.PlayMode.LOOP);

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
            if (isAlive && GameState.level != Level.WIN.getValue()) {
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    direction = Direction.LEFT;
                    moveBy(-Utils.MAN_SPEED, 0);
                    time += delta;
                    textureRegion = animationLeft.getKeyFrame(time);
                    playSoundWalking(1, delta);
                } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    direction = Direction.RIGHT;
                    moveBy(Utils.MAN_SPEED, 0);
                    time += delta;
                    textureRegion = animationRight.getKeyFrame(time);
                    playSoundWalking(1, delta);
                } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    direction = Direction.DOWN;
                    moveBy(0, -Utils.MAN_SPEED);
                    time += delta;
                    textureRegion = animationDown.getKeyFrame(time);
                    playSoundWalking(2, delta);
                } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    direction = Direction.UP;
                    moveBy(0, Utils.MAN_SPEED);
                    time += delta;
                    textureRegion = animationUp.getKeyFrame(time);
                    playSoundWalking(2, delta);
                }
            } else if (!isAlive){
                time += delta;
                textureRegion = animationDie.getKeyFrame(time);
            }else{
                time += delta;
                textureRegion = animationWin.getKeyFrame(time);
                if(direction.equals(Direction.DOWN) || direction.equals(Direction.UP)){
                    direction = Direction.RIGHT;
                }
                if(getX() == 32){
                    direction = Direction.RIGHT;
                }
                if(getX() == Gdx.graphics.getWidth() - 32){
                    direction = Direction.LEFT;
                }
                if(direction.equals(Direction.RIGHT)){
                    moveBy(2,0);
                    setScaleX(1);
                }
                if(direction.equals(Direction.LEFT)){
                    moveBy(-2,0);
                    setScaleX(-1);
                }

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
