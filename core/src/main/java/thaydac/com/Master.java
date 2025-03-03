package thaydac.com;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import thaydac.com.enemies.*;

import java.util.*;

public class Master implements Screen {
    StartGame game;
    int timing = 0;
    int count = 0;
    static boolean isFinished;
    boolean isEnemiesAllDie = false;

    public static Stage stage;
    private Background background;
    private Panel panel;
    public static Man man;
    public static Item item;
    public static Item itemBonus;
    public static Door door;

    public static int[][] wallArray;

    public static Array<MyActor> walls;
    public static Array<Brick> briches;
    public static Array<MyActor> enemies;
    public static Array<Bomb> bombs;
    public static Array<Explosion> explosions;

    Music dieSound;
    Music dieMusic;
    Music finishMusic;
    Music backgroundMusic;
    Music timeupMusic;
    Music enemiesalldieMusic;
    Sound collectSound;
    Music winMusic;
    float mysteryTime = 0;
    float waitSpawnEnemy = 61;

    // vẽ để thử
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
        if (man == null) { // lúc mới vào game nó chưa được khơỉ tạp
            man = new Man(32, Gdx.graphics.getHeight() - 32 * 4, stage);
        } else { // khi vào ván mới chỉ cần đặt vị trí và add vào stage, vì stage đã khởi tạo lại
            man.setPosition(32, Gdx.graphics.getHeight() - 32 * 4);
            man.time = 0;
            man.isAlive = true;
            man.textureRegion = man.animationRight.getKeyFrame(timing);
            stage.addActor(man);
        }
        if (GameState.level == Level.WIN.getValue()) {
            man.setPosition(32, 32 * 4);
        }

        dieSound = Gdx.audio.newMusic(Gdx.files.internal("die.mp3"));
        dieMusic = Gdx.audio.newMusic(Gdx.files.internal("dieMusic.mp3"));
        finishMusic = Gdx.audio.newMusic(Gdx.files.internal("finishMusic.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        timeupMusic = Gdx.audio.newMusic(Gdx.files.internal("timeup.mp3"));
        enemiesalldieMusic = Gdx.audio.newMusic(Gdx.files.internal("enemiesalldie.mp3"));
        collectSound = Gdx.audio.newSound(Gdx.files.internal("collect.mp3"));
        winMusic = Gdx.audio.newMusic(Gdx.files.internal("win.mp3"));

        dieSound.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                dieMusic.play();
            }
        });

        finishMusic.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                GameState.level++;
                GameState.left++;

                if (GameState.level == 6) {
                    GameState.level = Level.A.getValue();
                } else if (GameState.level == 11) {
                    GameState.level = Level.B.getValue();
                } else if (GameState.level == 16) {
                    GameState.level = Level.C.getValue();
                } else if (GameState.level == 21) {
                    GameState.level = Level.D.getValue();
                } else if (GameState.level == 26) {
                    GameState.level = Level.E.getValue();
                } else if (GameState.level == 31) {
                    GameState.level = Level.F.getValue();
                } else if ((GameState.level == 36)) {
                    GameState.level = Level.G.getValue();
                } else if ((GameState.level == 40)) {
                    GameState.level = Level.H.getValue();
                } else if ((GameState.level == 45)) {
                    GameState.level = Level.I.getValue();
                } else if ((GameState.level == 50)) {
                    GameState.level = Level.J.getValue();
                }
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
        if (!isBonusSatge()) {
            timing = 300;
        } else {
            timing = 30;
        }
        if(GameState.level == Level.WIN.getValue()){
            winMusic.play();
        } else {
            backgroundMusic.play();
        }
    }

    @Override
    public void render(float v) {
        mysteryTime = mysteryTime - 1;
        waitSpawnEnemy += 1;
        if (mysteryTime <= 0) {
            GameState.mystery = false;
        }
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        ScreenUtils.clear(Color.BLACK);

        if (GameState.level != Level.WIN.getValue()) {
            if (man.getX() > Gdx.graphics.getWidth() / 2 && man.getX() < 31 * 32 - Gdx.graphics.getWidth() / 2) {
                stage.getCamera().position.x = man.getX();
            }

            if (man.isAlive) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    int xMan = Math.round(man.getX() / 32) * 32; // làm tròn tọa độ x để chuẩn bị đặt bom cho chuẩn
                    int yMan = Math.round(man.getY() / 32) * 32;

                    if (isPositionFree(xMan, yMan) && GameState.bombNumber > 0) {
                        Bomb bomb = new Bomb(xMan, yMan, stage, bombs, explosions, true);
                        bombs.add(bomb);
                        walls.add(bomb);
                        GameState.bombNumber--;
                    }
                }
                if (Gdx.input.isKeyPressed(Input.Keys.B) && Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyJustPressed(Input.Keys.V)) {//BAV = BuiAnhVu
                    skillBAV();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                    skillN();
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                    skillM();
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.B) && GameState.decorator) {
                    // kích nổ qủa đầu tiên
                    if (!bombs.isEmpty()) {
                        bombs.get(0).isExploded = true;
                    }
                }

                for (Explosion explosion : explosions) {
                    if (explosion.getBound().overlaps(man.getBound())
                        && !isBonusSatge()
                        && !GameState.mystery
                        && !GameState.flamepass
                    ) {
                        replay();
                        break;
                    }

                    if (item != null && explosion.getBound().overlaps(item.getBound())) {
                        item.remove();
                    }
                    if (GameState.enemyInDoor && door != null && explosion.getBound().overlaps(door.getBound())) {
                        waitSpawnEnemy = 0;
                        GameState.enemyInDoor = false;
                    }
                }
                if (waitSpawnEnemy == 60) {
                    spawnEnemyInDoor();
                }
                for (MyActor enemy : enemies) {
                    if (enemy.getBound().overlaps(man.getBound())
                        && !isBonusSatge()
                        && !GameState.mystery
                    ) {
                        replay();
                        break;
                    }
                }
                count++;
                if (count % 60 == 0) {
                    if (timing > 0) {
                        timing--;
                        if (timing == 0) {
                            for (int i = 0; i < bombs.size; i++) {
                                bombs.get(i).isExploded = true;
                            }
                            // Ánh xạ giá trị level cũ sang level mới
                            Map<Integer, Integer> levelMap = Map.of(
                                Level.A.getValue(), 6,
                                Level.B.getValue(), 11,
                                Level.C.getValue(), 16,
                                Level.D.getValue(), 21,
                                Level.E.getValue(), 26,
                                Level.F.getValue(), 31,
                                Level.G.getValue(), 36,
                                Level.H.getValue(), 40,
                                Level.I.getValue(), 45,
                                Level.J.getValue(), 50
                            );

                            if (!levelMap.containsKey(GameState.level)) {
                                timeupMusic.play();
                            } else {
                                GameState.level = levelMap.getOrDefault(GameState.level, GameState.level);
                                Utils.saveGame();
                                game.setScreen(new StageScreen(game));
                            }
                        }
                    }
                }
            }

            if (enemies.isEmpty()) {
                if (!isEnemiesAllDie) {
                    enemiesalldieMusic.play();
                    isEnemiesAllDie = true;
                }
                if (GameState.level == 1 || GameState.level == 7 || GameState.level == 9
                        || GameState.level == 15 || GameState.level == 17
                        || GameState.level == 23 || GameState.level == 25
                        || GameState.level == 31 || GameState.level == 33
                        || GameState.level == 39 || GameState.level == 41
                        || GameState.level == 47 || GameState.level == 49) {
                    if (itemBonus == null && Utils.isShownGoddess && !Utils.isCollectedItemBonus) {
                        itemBonus = new Item(32, 32, ItemType.GODDESS_MASK, stage);
                        System.out.println("item bonus");
                    } else {
                        Utils.updatePlayerPosition(new Vector2(man.getX(), man.getY()));
                    }
                }
            }
        }

        stage.act();
        collisionWall();
        collectItems();
        collisionDoor();
        man.toFront();
        stage.draw();

        game.batch.begin();
        if (GameState.level == Level.WIN.getValue()) {
            game.font.draw(game.batch, "CONGRATULATIONS", Gdx.graphics.getWidth() / 2f - 15 * 7.5f, 450);
            game.font.draw(game.batch, "YOU HAVE SUCCEEDED IN", Gdx.graphics.getWidth() / 2f - 15 * 10.5f, 390);
            game.font.draw(game.batch, "HELPING BOMBERMAN TO BECOME", Gdx.graphics.getWidth() / 2f - 15 * 13.5f, 350);
            game.font.draw(game.batch, "A HUMAN BEING", Gdx.graphics.getWidth() / 2f - 15 * 13.5f, 310);
            game.font.draw(game.batch, "MAY BE YOU CAN RECOGNIZE HIM", Gdx.graphics.getWidth() / 2f - 15 * 10.5f, 270);
            game.font.draw(game.batch, "IN ANOTHER HUDSON SOFT GAME", Gdx.graphics.getWidth() / 2f - 15 * 13.5f, 230);
            game.font.draw(game.batch, "GOOD BYE", Gdx.graphics.getWidth() / 2f - 15 * 4f, 190);

        } else {
            game.font.draw(game.batch, "TIME: " + timing, 32, Gdx.graphics.getHeight() - 16);
            game.font.draw(game.batch, GameState.score < 10 ? "0" + GameState.score : "" + GameState.score, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 16);
            game.font.draw(game.batch, "LEFT: " + GameState.left, Gdx.graphics.getWidth() - 128, Gdx.graphics.getHeight() - 16);
        }
        game.batch.end();

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //shapeRenderer.setColor(Color.RED);
        //shapeRenderer.rect(itemRec.getX(), itemRec.getY(), itemRec.width, itemRec.height);
        //shapeRenderer.rect(doorRec.getX(), doorRec.getY(), doorRec.width, doorRec.height);
        //shapeRenderer.end();
    }

    public void collectItems() {
        if (item != null && man.getBound().overlaps(item.getBound())) {
            if (item.type.equals(ItemType.WALLPASS)) {
                GameState.wallPass = true;
                System.out.println("Collected wall pass");
            } else if (item.type.equals(ItemType.BOMB_NUMBER)) {
                GameState.bombNumber++;
            } else if (item.type.equals(ItemType.BOMB_POWER)) {
                GameState.bombPower++;
            } else if (item.type.equals(ItemType.DETONATOR)) {
                GameState.decorator = true;
            } else if (item.type.equals(ItemType.BOMB_PASS)) {
                GameState.bombPass = true;
            } else if (item.type.equals(ItemType.SPEED)) {
                Utils.MAN_SPEED = 4;
            } else if (item.type.equals(ItemType.FLAME_PASS)) {
                GameState.flamepass = true;
            } else if (item.type.equals(ItemType.MYSTERY)) {
                GameState.mystery = true;
                mysteryTime = MathUtils.random(10, 30) * 60;
            }
            item.remove();
            item = null;
            collectSound.play();
        }
        if (itemBonus != null && man.getBound().overlaps(itemBonus.getBound())) {
            if (itemBonus.type.equals(ItemType.GODDESS_MASK)) {
                GameState.score += 20000;
                Utils.isCollectedItemBonus = true;
            }
            itemBonus.remove();
            itemBonus = null;
            collectSound.play();
        }
    }

    public void collisionDoor() {
        if (!isFinished && enemies.isEmpty()) {
            if (door != null && man.getX() == door.getX() && man.getY() == door.getY()) { // lúc vào cửa trùng hoàn toàn vị trí cho đẹp
                isFinished = true;
                finishMusic.play();
            }
        }
        if (enemies.isEmpty()) {
            GameState.bonusTarget = true;
        }
    }

    public void collisionWall() {
        for (MyActor wall : walls) {
            if (checkCollision(wall, man)) {
                if (man.direction.equals(Direction.LEFT)) {
                    man.moveBy(Utils.MAN_SPEED, 0);
                    float diff = diffirentYCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(0, diff);
                    }
                } else if (man.direction.equals(Direction.RIGHT)) {
                    man.moveBy(-Utils.MAN_SPEED, 0);
                    float diff = diffirentYCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(0, diff);
                    }
                } else if (man.direction.equals(Direction.UP)) {
                    man.moveBy(0, -Utils.MAN_SPEED);
                    float diff = diffirentXCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(diff, 0);
                    }
                } else if (man.direction.equals(Direction.DOWN)) {
                    man.moveBy(0, Utils.MAN_SPEED);
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
        if (GameState.level != Level.WIN.getValue()) {
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
                    } else if ((cell == 2) && !isBonusSatge()) {
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
                    } else if (cell == Utils.ENEMY_TYPE4) {
                        Enemy4 enemy4 = new Enemy4(x, y, stage);
                        enemies.add(enemy4);
                    } else if (cell == Utils.ENEMY_TYPE5) {
                        Enemy5 enemy5 = new Enemy5(x, y, stage);
                        enemies.add(enemy5);
                    } else if (cell == Utils.ENEMY_TYPE6) {
                        Enemy6 enemy6 = new Enemy6(x, y, stage);
                        enemies.add(enemy6);
                    } else if (cell == Utils.ENEMY_TYPE7) {
                        Enemy7 enemy7 = new Enemy7(x, y, stage);
                        enemies.add(enemy7);
                    } else if (cell == Utils.ENEMY_TYPE_FAST) {
                        EnemyFast enemyFast = new EnemyFast(x, y, stage);
                        enemies.add(enemyFast);
                    }
                }
            }
        } else {
            for (int i = 0; i < 25; i++) {
                Brick brick = new Brick(32 * i, 32 * 3, stage);
                briches.add(brick);
                walls.add(brick);
            }
        }
        if (isBonusSatge()) {
            Brick brick = new Brick(-32, 0, stage);
            briches.add(brick);
            walls.add(brick);
            Brick brick2 = new Brick(-32, 0, stage);
            briches.add(brick2);
            walls.add(brick2);
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
        // Vào mỗi màn chơi thì item, door null để không bị nhầm lẫn
        item = null;
        itemBonus = null;
        door = null;
    }

    // _actor1: wall, _actor2: man
    boolean checkCollision(MyActor _actor1, MyActor _actor2) {
        if (_actor1 instanceof Bomb && (GameState.bombPass || ((Bomb) _actor1).isJustPlaced)) {
            return false;
        }
        if (_actor1 instanceof Brick && GameState.wallPass) {
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

    boolean isBonusSatge(){
        return GameState.level == Level.A.getValue()
            || GameState.level == Level.B.getValue()
            || GameState.level == Level.C.getValue()
            || GameState.level == Level.D.getValue()
            || GameState.level == Level.E.getValue()
            || GameState.level == Level.F.getValue()
            || GameState.level == Level.G.getValue()
            || GameState.level == Level.H.getValue()
            || GameState.level == Level.I.getValue()
            || GameState.level == Level.J.getValue();
    }

    public void replay(){
        man.isAlive = false;
        man.time = 0;
        dieSound.play();
        GameState.left--;
        GameState.decorator = false;
        GameState.wallPass = false;
        GameState.mystery = false;
        GameState.flamepass = false;
        GameState.bombPass = false;
        Utils.MAN_SPEED = 2;
        Utils.saveGame();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new StageScreen(game));
            }
        }, 4);
    }

    public void spawnEnemyInDoor() {
        int nextLevel = GameState.level + 1;
        int countEnemy = 0;
        Map<Integer, Integer> levelEnemies = Utils.enemyConfig.getOrDefault(nextLevel, new HashMap<>());

        // Chuyển `entrySet()` thành danh sách và sắp xếp giảm dần theo key
        List<Map.Entry<Integer, Integer>> sortedEntries = new ArrayList<>(levelEnemies.entrySet());
        sortedEntries.sort((e1, e2) -> Integer.compare(e2.getKey(), e1.getKey())); // Sắp xếp giảm dần

        for (Map.Entry<Integer, Integer> entry : sortedEntries) {
            if (countEnemy > 3) break;
            int enemyType = entry.getKey();
            int count = entry.getValue();

            while (countEnemy < 4 && count > 0) {
                switch (enemyType) {
                    case Utils.ENEMY_TYPE1 -> enemies.add(new Enemy1(door.getX(), door.getY(), stage));
                    case Utils.ENEMY_TYPE2 -> enemies.add(new Enemy2(door.getX(), door.getY(), stage));
                    case Utils.ENEMY_TYPE3 -> enemies.add(new Enemy3(door.getX(), door.getY(), stage));
                    case Utils.ENEMY_TYPE4 -> enemies.add(new Enemy4(door.getX(), door.getY(), stage));
                    case Utils.ENEMY_TYPE5 -> enemies.add(new Enemy5(door.getX(), door.getY(), stage));
                    case Utils.ENEMY_TYPE6 -> enemies.add(new Enemy6(door.getX(), door.getY(), stage));
                    case Utils.ENEMY_TYPE7 -> enemies.add(new Enemy7(door.getX(), door.getY(), stage));
                    case Utils.ENEMY_TYPE_FAST -> enemies.add(new EnemyFast(door.getX(), door.getY(), stage));
                }
                count--;
                countEnemy++;
            }
        }
    }

    // Kiểm tra vị trí có bom chưa
    private boolean isPositionFree(int x, int y) {
        for (Bomb b : bombs) {
            if (b.getX() == x && b.getY() == y) {
                return false;
            }
        }
        if(GameState.wallPass){
            for (Brick b : briches) {
                if (b.getX() == x && b.getY() == y) {
                    return false;
                }
            }
        }
        return true;
    }

    private void skillBAV(){
        for (MyActor e : enemies) {
            Bomb bomb = new Bomb(e.getX() - 32, e.getY(), stage, bombs, explosions, false);
            bombs.add(bomb);
            Bomb bomb2 = new Bomb(e.getX() + 32, e.getY(), stage, bombs, explosions, false);
            bombs.add(bomb2);
            Bomb bomb3 = new Bomb(e.getX(), e.getY() - 32, stage, bombs, explosions, false);
            bombs.add(bomb3);
            Bomb bomb4 = new Bomb(e.getX(), e.getY() + 32, stage, bombs, explosions, false);
            bombs.add(bomb4);
            GameState.bombNumber -= 4;
        }
        Bomb bomb = new Bomb(doorRec.getX() - 32, doorRec.getY(), stage, bombs, explosions, false);
        bombs.add(bomb);
        GameState.bombNumber--;
    }

    private void skillN(){
        int xMan = Math.round(man.getX() / 32) * 32; // làm tròn tọa độ x để chuẩn bị đặt bom cho chuẩn
        int yMan = Math.round(man.getY() / 32) * 32;

        for (int i = 0; i < 20; i += 2) {
            Bomb bomb = new Bomb(xMan + i * 32, yMan, stage, bombs, explosions, false);
            bombs.add(bomb);
            GameState.bombNumber--;
        }
        for (int i = 1; i < 21; i += 2) {
            Bomb bomb = new Bomb(xMan + i * 32, yMan - 32, stage, bombs, explosions, false);
            bombs.add(bomb);
            GameState.bombNumber--;
        }
        for (int i = 0; i < 20; i += 2) {
            Bomb bomb = new Bomb(xMan + i * 32, yMan - 64, stage, bombs, explosions, false);
            bombs.add(bomb);
            GameState.bombNumber--;
        }
    }

    private void skillM(){
        int xMan = Math.round(man.getX() / 32) * 32; // làm tròn tọa độ x để chuẩn bị đặt bom cho chuẩn
        int yMan = Math.round(man.getY() / 32) * 32;

        int i = 0;
        int j = 0;
        for (; i < 6; i += 2) {
            j++;
            Bomb bomb = new Bomb(xMan + j * 32, yMan - i * 32, stage, bombs, explosions, false);
            bombs.add(bomb);
            GameState.bombNumber--;
            Bomb bomb2 = new Bomb(xMan + j * 32, yMan - i * 32 - 32, stage, bombs, explosions, false);
            bombs.add(bomb2);
            GameState.bombNumber--;
        }
        for (; -2 < i; i -= 2) {
            j++;
            Bomb bomb = new Bomb(xMan + j * 32, yMan - i * 32, stage, bombs, explosions, false);
            bombs.add(bomb);
            GameState.bombNumber--;
            Bomb bomb2 = new Bomb(xMan + j * 32, yMan - i * 32 - 32, stage, bombs, explosions, false);
            bombs.add(bomb2);
            GameState.bombNumber--;
        }
        j += 2;
        i += 2;
        for (; i < 7; i++) {
            Bomb bomb = new Bomb(xMan + j * 32, yMan - i * 32, stage, bombs, explosions, false);
            bombs.add(bomb);
            GameState.bombNumber--;
        }
        j++;
        for (; j < 15; j++) {
            Bomb bomb = new Bomb(xMan + j * 32, yMan - i * 32, stage, bombs, explosions, false);
            bombs.add(bomb);
            GameState.bombNumber--;
        }
        i--;
        for (; -1 < i; i--) {
            Bomb bomb = new Bomb(xMan + j * 32, yMan - i * 32, stage, bombs, explosions, false);
            bombs.add(bomb);
            GameState.bombNumber--;
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
