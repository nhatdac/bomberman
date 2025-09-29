package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Man extends MyActor {
    public Animation<TextureRegion> animationLeft, animationRight, animationUp, animationDown, animationDie, animationWin;
    private float time, timeSound;
    private Direction direction = Direction.RIGHT;
    private boolean isPlaying = false;
    private boolean isAlive = true;
    private final Sound walking1, walking2;

    public Man(float x, float y, Stage s) {
        super(x, y, s);
        Texture texture = new Texture("man1.png");
        Texture textureWin = new Texture("man-win.png");

        TextureRegion[] frames = extractFrames(texture, 19, 1);
        TextureRegion[] framesWin = extractFrames(textureWin, 3, 1);

        animationLeft = createAnimation(0.1f, frames[0], frames[1], frames[2]);
        animationDown = createAnimation(0.1f, frames[3], frames[4], frames[5]);
        animationRight = createAnimation(0.1f, frames[6], frames[7], frames[8]);
        animationUp = createAnimation(0.1f, frames[9], frames[10], frames[11]);
        animationDie = createAnimation(0.3f, frames[12], frames[13], frames[14], frames[15], frames[16], frames[17], frames[18]);
        animationWin = createAnimation(0.3f, framesWin);

        setSize(32, 32);
        textureRegion = animationRight.getKeyFrame(0);

        walking1 = Gdx.audio.newSound(Gdx.files.internal("walking1.mp3"));
        walking2 = Gdx.audio.newSound(Gdx.files.internal("walking2.mp3"));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (Master.isFinished) return;

        if (isAlive && GameState.level != Level.WIN.getValue()) {
            handleInput(delta);
        } else {
            time += delta;
            textureRegion = isAlive ? animationWin.getKeyFrame(time) : animationDie.getKeyFrame(time);
            if (isAlive) handleVictoryWalk();
        }
    }

    private void handleInput(float delta) {
        boolean moved = false;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction = Direction.LEFT;
            moveBy(-Utils.MAN_SPEED, 0);
            textureRegion = animationLeft.getKeyFrame(time += delta);
            playSoundWalking(1, delta);
            moved = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = Direction.RIGHT;
            moveBy(Utils.MAN_SPEED, 0);
            textureRegion = animationRight.getKeyFrame(time += delta);
            playSoundWalking(1, delta);
            moved = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction = Direction.DOWN;
            moveBy(0, -Utils.MAN_SPEED);
            textureRegion = animationDown.getKeyFrame(time += delta);
            playSoundWalking(2, delta);
            moved = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction = Direction.UP;
            moveBy(0, Utils.MAN_SPEED);
            textureRegion = animationUp.getKeyFrame(time += delta);
            playSoundWalking(2, delta);
            moved = true;
        }

        if (!moved) time = 0;
    }

    private void handleVictoryWalk() {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            direction = Direction.RIGHT;
        }

        float x = getX();
        float screenWidth = Gdx.graphics.getWidth();

        if (x <= 32) direction = Direction.RIGHT;
        else if (x >= screenWidth - 32) direction = Direction.LEFT;

        float moveAmount = direction == Direction.RIGHT ? 0.5f : -0.5f;
        moveBy(moveAmount, 0);
        setScaleX(direction == Direction.RIGHT ? 1 : -1);
    }

    private void playSoundWalking(int mode, float delta) {
        timeSound += delta;
        if (timeSound > 0.4f) {
            timeSound = 0;
            isPlaying = false;
        }

        if (!isPlaying) {
            (mode == 1 ? walking1 : walking2).play(0.2f);
            isPlaying = true;
        }
    }

    private Animation<TextureRegion> createAnimation(float frameDuration, TextureRegion... regions) {
        Animation<TextureRegion> anim = new Animation<>(frameDuration, regions);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        return anim;
    }

    private TextureRegion[] extractFrames(Texture texture, int cols, int rows) {
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / cols, texture.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                frames[index++] = tmp[i][j];
        return frames;
    }
    public void setTime(float time){
        this.time = time;
    }

    public void setAlive(boolean isAlive){
        this.isAlive = isAlive;
    }
    public boolean getAlive(){
        return isAlive;
    }
    public Direction getDirection(){
        return direction;
    }
}
