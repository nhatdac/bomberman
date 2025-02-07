package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.lang.reflect.Type;

public class Master implements Screen {
    StartGame game;
    int time = 150;
    int count = 0;
    int score = 0;
    int left = 3;
    int timingChangeLevel;
    static boolean isFinished;

    Stage stage;
    private Background background;
    private Panel panel;
    static Man man;
    static Item item;
    static Door door;
    static int level = 1;

    static Array<MyActor> walls;
    static Array<Brick> briches;
    static Array<MyActor> enemies;
    Array<Bomb> bombs;
    static Array<Explosion> explosions;
    Sound dieSound;
    Sound collectSound;

    // vẽ đ thử
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    Rectangle itemRec = new Rectangle();
    Rectangle doorRec = new Rectangle();

    public Master(StartGame game) {
        this.game = game;
        stage = new Stage();

        walls = new Array<>();
        briches = new Array<>();
        enemies = new Array<>();
        bombs = new Array<>();
        explosions = new Array<>();

        generateMap();
        if(man == null) { // lúc mới vào game nó chưa được khơỉ tạp
            man = new Man(32, Gdx.graphics.getHeight() - 32 * 4, stage);
        } else { // khi vào ván mới chỉ cần đặt vị trí và add vào stage, vì stage đã khởi tạo lại
            man.setPosition(32, Gdx.graphics.getHeight() - 32 * 4);
            stage.addActor(man);
        }

        dieSound = Gdx.audio.newSound(Gdx.files.internal("die.mp3"));
        collectSound = Gdx.audio.newSound(Gdx.files.internal("collect.mp3"));
    }

    @Override
    public void show() {
        isFinished = false;
        timingChangeLevel = 120;
    }

    @Override
    public void render(float v) {
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        if (man.isAlive && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            int xMan = Math.round(man.getX() / 32) * 32; // làm tròn tọa độ x để chuẩn bị đặt bom cho chuẩn
            int yMan = Math.round(man.getY() / 32) * 32;
            boolean positionOK = true;
            for (Bomb b : bombs) {
                if (b.getX() == xMan && b.getY() == yMan) {
                    positionOK = false;
                    break;
                }
            }
            if (positionOK && man.bombNumber > 0) {
                Bomb bomb = new Bomb(xMan, yMan, stage, bombs, explosions);
                bombs.add(bomb);
                walls.add(bomb);
                man.bombNumber--;
            }
        }

        if (man.isAlive) {
            for (Explosion explosion : explosions) {
                if (explosion.getBound().overlaps(man.getBound())) {
                    man.isAlive = false;
                    man.time = 0;
                    dieSound.play();
                    break;
                }
            }
            for (MyActor enemy : enemies) {
                if (enemy.getBound().overlaps(man.getBound())) {
                    man.isAlive = false;
                    man.time = 0;
                    dieSound.play();
                    break;
                }
            }
        }

        stage.act();
        collisionWall();
        collectItems();
        collisionDoor();
        man.toFront();
        stage.draw();

        count++;
        if (count % 60 == 0) {
            time--;
        }
        game.batch.begin();
        game.font.draw(game.batch, "TIME: " + time, 32, Gdx.graphics.getHeight() - 16);
        game.font.draw(game.batch, score < 10 ? "0" + score : "" + score, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 16);
        game.font.draw(game.batch, "LEFT: " + left, Gdx.graphics.getWidth() - 128, Gdx.graphics.getHeight() - 16);
        game.batch.end();

        if (isFinished){
            timingChangeLevel--;
            if(timingChangeLevel < 1){
                level++;
                game.setScreen(new StageScreen(game));
            }
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(itemRec.getX(), itemRec.getY(), itemRec.width, itemRec.height);
        shapeRenderer.rect(doorRec.getX(), doorRec.getY(), doorRec.width, doorRec.height);
        shapeRenderer.end();
    }

    public void collectItems() {
        if (item != null && man.getBound().overlaps(item.getBound())) {
            if (item.type.equals(ItemType.BOMB_NUMBER)) {
                man.bombNumber++;
            } else if (item.type.equals(ItemType.BOMB_POWER)) {
                man.bombPower++;
            }
            item.remove();
            item = null;
            collectSound.play();
        }
    }

    public void collisionDoor(){
        if (door != null
            && !isFinished
            && enemies.isEmpty()
            && man.getX() == door.getX()
            && man.getY() == door.getY()) { // lúc vào cửa trùng hoàn toàn vị trí cho đẹp
            isFinished = true;
            collectSound.play();
        }
    }

    public void collisionWall() {
        for (MyActor wall : walls) {
            if (checkCollision(wall, man)) {
                if (man.direction.equalsIgnoreCase("L")) {
                    man.moveBy(2, 0);
                    float diff = diffirentYCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(0, diff);
                    }
                } else if (man.direction.equalsIgnoreCase("R")) {
                    man.moveBy(-2, 0);
                    float diff = diffirentYCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(0, diff);
                    }
                } else if (man.direction.equalsIgnoreCase("U")) {
                    man.moveBy(0, -2);
                    float diff = diffirentXCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(diff, 0);
                    }
                } else if (man.direction.equalsIgnoreCase("D")) {
                    man.moveBy(0, 2);
                    float diff = diffirentXCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(diff, 0);
                    }
                }
                break;
            }
        }
    }

    public float diffirentYCor(MyActor _man, MyActor _wall) {
        float diff;
        if (_man.getY() > _wall.getY()) {
            diff = _wall.getY() + _wall.getHeight() - _man.getY();
        } else {
            diff = -1 * (_man.getY() + _man.getHeight() - _wall.getY());
        }

        return diff;
    }

    public float diffirentXCor(MyActor _man, MyActor _wall) {
        float diff;
        if (_man.getX() > _wall.getX()) {
            diff = _wall.getX() + _wall.getWidth() - _man.getX();
        } else {
            diff = -1 * (_man.getX() + _man.getWidth() - _wall.getX());
        }

        return diff;
    }

    public void generateMap() {
        background = new Background(0, 0, stage);
        panel = new Panel(0, Gdx.graphics.getHeight() - 64, stage);

        int[][] wallArray = Utils.buildMap();
        int enemy1Number = 5;
        int enemy2Number = 3;

        int tileSize = 32; // Kích thước mỗi ô
        for (int row = 0; row < wallArray.length; row++) {
            for (int col = 0; col < wallArray[row].length; col++) {
                int cell = wallArray[row][col];
                int x = col * tileSize;
                int y = (wallArray.length - 1 - row) * tileSize; // Lật trục y
                if (cell == 1) {
                    // Tạo tường
                    walls.add(new Wall(x, y, stage));
                } else if (cell == 2) {
                    Brick brick = new Brick(x, y, stage);
                    // Tạo gạch
                    briches.add(brick);
                    // cho cả gạch vào tuờng để kiểm tra va chạm dễ hơn
                    walls.add(brick);
                } else if (cell == 3) {
                    if (enemy1Number > 0) {
                        Enemy1 enemy1 = new Enemy1(x, y, stage);
                        // Tạo enemy
                        enemies.add(enemy1);
                        enemy1Number--;
                    }
                } else if (cell == 4) {
                    if(level > 1){
                        if (enemy2Number > 0) {
                            Enemy2 enemy2 = new Enemy2(x, y, stage);
                            // Tạo enemy
                            enemies.add(enemy2);
                            enemy2Number--;
                        }
                    }
                }
            }
        }
        int itemPosition = MathUtils.random(0, briches.size - 1);
        int doorPosition = MathUtils.random(0, briches.size - 1);

        while (itemPosition == doorPosition) {
            doorPosition = MathUtils.random(0, briches.size - 1);
        }
        briches.get(itemPosition).hasItem = true;
        briches.get(doorPosition).hasDoor = true;
        itemRec = briches.get(itemPosition).getBound();
        doorRec = briches.get(doorPosition).getBound();
    }


    // _actor1: wall, _actor2: man
    boolean checkCollision(MyActor _actor1, MyActor _actor2) {
        if (_actor1 instanceof Bomb && ((Bomb) _actor1).isJustPlaced) {
            return false;
        }
        return _actor1.getBound().overlaps(_actor2.getBound());
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
