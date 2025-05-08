package thaydac.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Map;

public class Brick extends MyActor {

    private static final Texture texture = new Texture("brich.png");
    private static final Map<Integer, ItemType> levelItemMap = new HashMap<>();

    static {
        // Khởi tạo map chỉ một lần
        int[] bombPowerLevels = {1, 7, 11, 12, 27, 38};
        int[] bombNumberLevels = {2, 6, 15, 17, 19, 23, 28, 32};
        int[] detonatorLevels = {3, 8, 13, 20, 22, 24, 29, 33, 37, 41, 44, 48};
        int[] speedLevels = {4};
        int[] bombPassLevels = {5, 9, 14, 18, 21, 25, 35, 43, 47};
        int[] wallPassLevels = {10, 16, 39, 42, 46};
        int[] mysteryLevels = {26, 34, 40, 45, 50};
        int[] flamePassLevels = {30, 36, 49};

        for (int lvl : bombPowerLevels) levelItemMap.put(lvl, ItemType.BOMB_POWER);
        for (int lvl : bombNumberLevels) levelItemMap.put(lvl, ItemType.BOMB_NUMBER);
        for (int lvl : detonatorLevels) levelItemMap.put(lvl, ItemType.DETONATOR);
        for (int lvl : speedLevels) levelItemMap.put(lvl, ItemType.SPEED);
        for (int lvl : bombPassLevels) levelItemMap.put(lvl, ItemType.BOMB_PASS);
        for (int lvl : wallPassLevels) levelItemMap.put(lvl, ItemType.WALLPASS);
        for (int lvl : mysteryLevels) levelItemMap.put(lvl, ItemType.MYSTERY);
        for (int lvl : flamePassLevels) levelItemMap.put(lvl, ItemType.FLAME_PASS);
    }

    private final Animation<TextureRegion> animation;
    private float time;
    private boolean isFire = false;
    private boolean hasItem = false;
    private boolean hasDoor = false;

    public Brick(float x, float y, Stage s) {
        super(x, y, s);

        int columns = 7;
        int rows = 1;

        TextureRegion[][] frameBuff = TextureRegion.split(texture, texture.getWidth() / columns, texture.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[columns * rows];

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames[index++] = frameBuff[i][j];
            }
        }

        animation = new Animation<>(0.1f, frames);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        setSize(32, 32);
        time = 0;
        textureRegion = animation.getKeyFrame(time);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (isFire) {
            time += delta;
            textureRegion = animation.getKeyFrame(time);
        }

        if (animation.isAnimationFinished(time)) {
            setFire(false);

            if (hasItem) {
                ItemType itemType = levelItemMap.get(GameState.level);
                if (itemType != null) {
                    Master.item = new Item(getX(), getY(), itemType, getStage());
                }
            } else if (hasDoor) {
                Master.door = new Door(getX(), getY(), getStage());
            }

            remove();
            Master.walls.removeValue(this, true);
            Master.briches.removeValue(this, true);
        }
    }

    public void setFire(boolean fire) {
        this.isFire = fire;
    }

    public void setHasItem(boolean hasItem) {
        this.hasItem = hasItem;
    }

    public void setHasDoor(boolean hasDoor) {
        this.hasDoor = hasDoor;
    }
}
