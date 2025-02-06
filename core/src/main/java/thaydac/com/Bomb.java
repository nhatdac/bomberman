package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class Bomb extends MyActor {
    Animation<TextureRegion> animation;
    Animation<TextureRegion> explosionAnimation;
    Animation<TextureRegion> explosionUpAnimation;
    Animation<TextureRegion> explosionDownAnimation;
    Animation<TextureRegion> explosionLeftAnimation;
    Animation<TextureRegion> explosionRightAnimation;

    Animation<TextureRegion> verticalBodyAnimation;
    Animation<TextureRegion> horizontalBodyAnimation;

    float time;
    boolean isFire = false;
    boolean isExploded = false;

    Sound setSound;
    Sound explodeSound;
    Array<Bomb> bombs;
    Array<Explosion> explosions;
    int power = 5;

    Bomb(float x, float y, Stage s, Array<Bomb> _bombs, Array<Explosion> _explosions) {
        super(x, y, s);
        this.bombs = _bombs;
        this.explosions = _explosions;

        int cot = 3;
        int hang = 1;
        Texture texture = new Texture("bomb.png");
        TextureRegion[][] frameBuff = TextureRegion.split(texture, texture.getWidth() / cot, texture.getHeight() / hang);
        TextureRegion[] frames = new TextureRegion[cot * hang];
        int index = 0;
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < cot; j++) {
                frames[index++] = frameBuff[i][j];
            }
        }

        animation = new Animation<TextureRegion>(0.5f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        // Load explosion animations
        explosionAnimation = loadExplosionAnimation("bomb-explosion-center.png");
        explosionUpAnimation = loadExplosionAnimation("bomb-explosion-up.png");
        explosionDownAnimation = loadExplosionAnimation("bomb-explosion-down.png");
        explosionLeftAnimation = loadExplosionAnimation("bomb-explosion-left.png");
        explosionRightAnimation = loadExplosionAnimation("bomb-explosion-right.png");

        verticalBodyAnimation = loadExplosionAnimation("bomb-explosion-body-up.png");
        horizontalBodyAnimation = loadExplosionAnimation("bomb-explosion-body-right.png");

        setSize(32, 32);

        time = 0;
        textureRegion = animation.getKeyFrame(time);

        setSound = Gdx.audio.newSound(Gdx.files.internal("set.mp3"));
        setSound.play(0.3f);
        explodeSound = Gdx.audio.newSound(Gdx.files.internal("explode.mp3"));
    }

    private Animation<TextureRegion> loadExplosionAnimation(String textureFile) {
        int cot = 4;
        int hang = 1;
        Texture texture = new Texture(textureFile);
        TextureRegion[][] frames = TextureRegion.split(texture, texture.getWidth() / cot, texture.getHeight() / hang);
        TextureRegion[] regions = new TextureRegion[cot * hang];
        int index = 0;
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < cot; j++) {
                regions[index++] = frames[i][j];
            }
        }
        return new Animation<TextureRegion>(0.1f, regions);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;

        if (!isExploded && time >= 3) {
            isExploded = true;
        }

        if(!isExploded) {
            for (Explosion explosion : explosions) {
                if (explosion.getBound().overlaps(getBound())) {
                    isExploded = true;
                }
            }
        }

        if (isExploded) {
            time = 0;
            explodeSound.play();
            createExplosions(); // Tạo hiệu ứng nổ lan
            remove();
            bombs.removeValue(this, true);
        } else {
            textureRegion = animation.getKeyFrame(time);
        }
    }

    private void createExplosions() {
        Stage stage = getStage();
        float size = 32; // Kích thước 1 ô

        Explosion explosionCenter = new Explosion(getX(), getY(), stage, explosionAnimation, explosions);
        explosions.add(explosionCenter);

        // Nổ trên
        for (int i = 1; i <= power; i++) {
            float yOffset = getY() + size * i;
            if (isBlocked(getX(), yOffset)) break; // Dừng nếu bị chặn
            Explosion explosion = new Explosion(getX(), yOffset, stage, i == power ? explosionUpAnimation : verticalBodyAnimation, explosions);
            explosions.add(explosion);
        }

        // Nổ dưới
        for (int i = 1; i <= power; i++) {
            float yOffset = getY() - size * i;
            if (isBlocked(getX(), yOffset)) break; // Dừng nếu bị chặn
            Explosion explosion = new Explosion(getX(), yOffset, stage, i == power ? explosionDownAnimation : verticalBodyAnimation, explosions);
            explosions.add(explosion);
        }

        // Nổ trái
        for (int i = 1; i <= power; i++) {
            float xOffset = getX() - size * i;
            if (isBlocked(xOffset, getY())) break; // Dừng nếu bị chặn
            Explosion explosion = new Explosion(xOffset, getY(), stage, i == power ? explosionLeftAnimation : horizontalBodyAnimation, explosions);
            explosions.add(explosion);
        }

        // Nổ phải
        for (int i = 1; i <= power; i++) {
            float xOffset = getX() + size * i;
            if (isBlocked(xOffset, getY())) break; // Dừng nếu bị chặn
            Explosion explosion = new Explosion(xOffset, getY(), stage, i == power ? explosionRightAnimation : horizontalBodyAnimation, explosions);
            explosions.add(explosion);
        }
    }


    private Array<MyActor> getObstacles() {
        // Trả về danh sách các vật cản.
        return Master.walls;
    }


    private boolean isBlocked(float x, float y) {
        // Kiểm tra xem vị trí (x, y) có bị chặn bởi vật cản hay không.
        Array<MyActor> obstacles = getObstacles(); // Lấy danh sách vật cản
        for (MyActor obstacle : obstacles) {
            if (obstacle.getX() == x && obstacle.getY() == y) {
                if(obstacle instanceof Brick){
                    Brick brick = (Brick) obstacle;
                    brick.isFire = true;
                }
                return true; // Có vật cản tại vị trí này
            }
        }
        return false;
    }


//    private void createExplosions() {
//        float explosionSize = 32; // Kích thước hiệu ứng nổ
//        Stage stage = getStage();
//
//        // Nổ trên
//        Explosion explosionUp = new Explosion(getX(), getY() + explosionSize, stage, explosionUpAnimation);
//        stage.addActor(explosionUp);
//
//        // Nổ dưới
//        Explosion explosionDown = new Explosion(getX(), getY() - explosionSize, stage, explosionDownAnimation);
//        stage.addActor(explosionDown);
//
//        // Nổ trái
//        Explosion explosionLeft = new Explosion(getX() - explosionSize, getY(), stage, explosionLeftAnimation);
//        stage.addActor(explosionLeft);
//
//        // Nổ phải
//        Explosion explosionRight = new Explosion(getX() + explosionSize, getY(), stage, explosionRightAnimation);
//        stage.addActor(explosionRight);
//    }
}
