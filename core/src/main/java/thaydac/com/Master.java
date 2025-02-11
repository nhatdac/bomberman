package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import thaydac.com.enemies.Enemy1;
import thaydac.com.enemies.Enemy2;
import thaydac.com.enemies.Enemy3;
import thaydac.com.enemies.EnemyFast;

public class Master implements Screen {
    StartGame game;
    int timing = 0;
    int count = 0;
    static boolean isFinished;
    boolean isEnemiesAllDie = false;

    Stage stage;
    private Background background;
    private Panel panel;
    public static Man man;
    public static Item item;
    public static Door door;

    int[][] wallArray;

    public static Array<MyActor> walls;
    public static Array<Brick> briches;
    public static Array<MyActor> enemies;
    public Array<Bomb> bombs;
    public static Array<Explosion> explosions;
    Music dieSound;
    Music dieMusic;
    Music finishMusic;
    Music backgroundMusic;
    Music timeupMusic;
    Music enemiesalldieMusic;
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
            man.time = 0;
            man.isAlive = true;
            man.textureRegion = man.animationRight.getKeyFrame(timing);
            stage.addActor(man);
        }

        dieSound = Gdx.audio.newMusic(Gdx.files.internal("die.mp3"));
        dieMusic = Gdx.audio.newMusic(Gdx.files.internal("dieMusic.mp3"));
        finishMusic = Gdx.audio.newMusic(Gdx.files.internal("finishMusic.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        timeupMusic = Gdx.audio.newMusic(Gdx.files.internal("timeup.mp3"));
        enemiesalldieMusic = Gdx.audio.newMusic(Gdx.files.internal("enemiesalldie.mp3"));
        collectSound = Gdx.audio.newSound(Gdx.files.internal("collect.mp3"));

        backgroundMusic.play();

        dieSound.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                dieMusic.play();
            }
        });

        dieMusic.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                game.setScreen(new StageScreen(game));
            }
        });

        finishMusic.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                GameState.level++;
                Utils.saveGame();
                game.setScreen(new StageScreen(game));
            }
        });

        timeupMusic.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                spawnEnemyFast();
            }
        });
    }

    @Override
    public void show() {
        isFinished = false;
        timing = 150;
        System.out.println("" + GameState.score);
    }

    @Override
    public void render(float v) {
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        if(man.getX() > Gdx.graphics.getWidth()/2 && man.getX() < 31*32 - Gdx.graphics.getWidth()/2){
            stage.getCamera().position.x = man.getX();
        }

        if (man.isAlive) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                int xMan = Math.round(man.getX() / 32) * 32; // làm tròn tọa độ x để chuẩn bị đặt bom cho chuẩn
                int yMan = Math.round(man.getY() / 32) * 32;
                boolean positionOK = true;
                for (Bomb b : bombs) {
                    if (b.getX() == xMan && b.getY() == yMan) {
                        positionOK = false;
                        break;
                    }
                }
                if (positionOK && GameState.bombNumber > 0) {
                    Bomb bomb = new Bomb(xMan, yMan, stage, bombs, explosions);
                    bombs.add(bomb);
                    walls.add(bomb);
                    GameState.bombNumber--;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.B) && GameState.decorator) {
                // kích nổ qủa đầu tiên
                bombs.get(0).isExploded = true;
            }

            for (Explosion explosion : explosions) {
                if (explosion.getBound().overlaps(man.getBound())) {
                    man.isAlive = false;
                    dieSound.play();
                    break;
                }
            }
            for (MyActor enemy : enemies) {
                if (enemy.getBound().overlaps(man.getBound())) {
                    man.isAlive = false;
                    dieSound.play();
                    break;
                }
            }
            count++;
            if (count % 60 == 0) {
                if(timing > 0) {
                    timing--;
                    if (timing == 0) {
                        timeupMusic.play();
                    }
                }
            }
            if(!man.isAlive){
                GameState.left--;
                GameState.decorator = false;
                man.time = 0;
            }
        }

        if(!isEnemiesAllDie && enemies.isEmpty()){
            enemiesalldieMusic.play();
            isEnemiesAllDie = true;
        }


        stage.act();
        collisionWall();
        collectItems();
        collisionDoor();
        man.toFront();
        stage.draw();

        game.batch.begin();
        game.font.draw(game.batch, "TIME: " + timing, 32, Gdx.graphics.getHeight() - 16);
        game.font.draw(game.batch, GameState.score < 10 ? "0" + GameState.score : "" + GameState.score, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 16);
        game.font.draw(game.batch, "LEFT: " + GameState.left, Gdx.graphics.getWidth() - 128, Gdx.graphics.getHeight() - 16);
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(itemRec.getX(), itemRec.getY(), itemRec.width, itemRec.height);
        shapeRenderer.rect(doorRec.getX(), doorRec.getY(), doorRec.width, doorRec.height);
        shapeRenderer.end();
    }

    public void collectItems() {
        if (item != null && man.getBound().overlaps(item.getBound())) {
            if (item.type.equals(ItemType.BOMB_NUMBER)) {
                GameState.bombNumber++;
            } else if (item.type.equals(ItemType.BOMB_POWER)) {
                GameState.bombPower++;
            } else if (item.type.equals(ItemType.DETONATOR)) {
                GameState.decorator = true;
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
            finishMusic.play();
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

        wallArray = Utils.buildMap();

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
                } else if (cell == Utils.ENEMY_TYPE1) {
                    // Tạo enemy
                    Enemy1 enemy1 = new Enemy1(x, y, stage);
                    // thêm vào danh sách các enemies
                    enemies.add(enemy1);
                } else if (cell == Utils.ENEMY_TYPE2) {
                    Enemy2 enemy2 = new Enemy2(x, y, stage);
                    enemies.add(enemy2);
                } else if (cell == Utils.ENEMY_TYPE3) {
                    Enemy3 enemy3 = new Enemy3(x, y, stage);
                    enemies.add(enemy3);
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

    public void spawnEnemyFast() {
        int enemyFastNumber = 15;

        int tileSize = 32; // Kích thước mỗi ô
        for (int row = 3; row < wallArray.length; row++) {
            for (int col = 0; col < wallArray[row].length; col++) {
                int cell = wallArray[row][col];
                int x = col * tileSize;
                int y = (wallArray.length - 1 - row) * tileSize; // Lật trục y
                if (cell == 0) {
                    if (enemyFastNumber > 0 && MathUtils.random(1, 3) == 1) {
                        EnemyFast enemyFast = new EnemyFast(x, y, stage);
                        // Tạo enemy fast
                        enemies.add(enemyFast);
                        enemyFastNumber--;
                    }
                }
            }
        }
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
        backgroundMusic.stop();
    }

    @Override
    public void dispose() {

    }
}
