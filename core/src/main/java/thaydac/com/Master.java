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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import thaydac.com.enemies.*;

import java.util.Random;

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
    public static Item itemBonus;
    public static Door door;

    int[][] wallArray;

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

    // vẽ đ thử
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    Rectangle itemRec = new Rectangle();
    Rectangle doorRec = new Rectangle();

    int time = 0;
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
        timing = 300;
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
                    GameState.bombNumber--;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.B) && Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyJustPressed(Input.Keys.V)) {//BAV = BuiAnhVu
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
                    for(MyActor e : enemies){
                        Bomb bomb = new Bomb(e.getX() - 32, e.getY(), stage, bombs, explosions);
                        bombs.add(bomb);
                        Bomb bomb2 = new Bomb(e.getX() + 32, e.getY(), stage, bombs, explosions);
                        bombs.add(bomb2);
                        Bomb bomb3 = new Bomb(e.getX(), e.getY()-32, stage, bombs, explosions);
                        bombs.add(bomb3);
                        Bomb bomb4 = new Bomb(e.getX(), e.getY()+32, stage, bombs, explosions);
                        bombs.add(bomb4);
                        GameState.bombNumber -= 4;
                    }
                    Bomb bomb = new Bomb(doorRec .getX() - 32, doorRec.getY(), stage, bombs, explosions);
                    bombs.add(bomb);
                    GameState.bombNumber--;
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
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
                    for(int i = 0;i < 20;i += 2){
                        Bomb bomb = new Bomb(xMan + i*32 , yMan, stage, bombs, explosions);
                        bombs.add(bomb);
                    }
                    for(int i = 1;i < 21;i += 2){
                        Bomb bomb = new Bomb(xMan + i*32 , yMan-32, stage, bombs, explosions);
                        bombs.add(bomb);
                    }
                    for(int i = 0;i < 20;i += 2){
                        Bomb bomb = new Bomb(xMan + i*32 , yMan-64, stage, bombs, explosions);
                        bombs.add(bomb);
                    }
                    GameState.bombNumber-=30;
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
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
                    int i = 0;
                    int j = 0;
                    for(;i < 6;i += 2){
                        j++;
                        Bomb bomb = new Bomb(xMan + j*32 , yMan - i*32, stage, bombs, explosions);
                        bombs.add(bomb);
                        Bomb bomb2 = new Bomb(xMan + j*32 , yMan - i*32 - 32, stage, bombs, explosions);
                        bombs.add(bomb2);
                    }
                    for(; -2 < i; i-= 2){
                        j++;
                        Bomb bomb = new Bomb(xMan + j*32 , yMan - i*32, stage, bombs, explosions);
                        bombs.add(bomb);
                        Bomb bomb2 = new Bomb(xMan + j*32 , yMan - i*32 - 32, stage, bombs, explosions);
                        bombs.add(bomb2);
                    }
                    j+= 2;
                    i+=2;
                    for(;i < 7;i ++){
                        Bomb bomb = new Bomb(xMan + j*32 , yMan - i*32, stage, bombs, explosions);
                        bombs.add(bomb);

                    }
                    j++;
                    System.out.println(j);
                    for(;j < 15;j ++){
                        Bomb bomb = new Bomb(xMan + j*32 , yMan - i*32, stage, bombs, explosions);
                        bombs.add(bomb);

                    }
                    i--;
                    System.out.println(i);
                    for(; -1 < i; i --){
                        Bomb bomb = new Bomb(xMan + j*32 , yMan - i*32, stage, bombs, explosions);
                        bombs.add(bomb);
                    }
                    GameState.bombNumber-=32;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.B) && GameState.decorator) {
                // kích nổ qủa đầu tiên
                bombs.get(0).isExploded = true;
            }

           if(GameState.mystery = false){

               for (Explosion explosion : explosions) {
                   if (!GameState.flamepass && explosion.getBound().overlaps(man.getBound())) {
//                    man.isAlive = false;
//                    dieSound.play();
//                    break;
                   }
                   for (Bomb b : bombs) {
                       if (explosion.getBound().overlaps(b.getBound())) {
                           b.isExploded = true;
                       }
                   }
               }
               for (MyActor enemy : enemies) {
                   if (enemy.getBound().overlaps(man.getBound())) {
//                    man.isAlive = false;
//                    dieSound.play();
//                    break;
                   }
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

        if(enemies.isEmpty()){
            if(!isEnemiesAllDie) {
                enemiesalldieMusic.play();
                isEnemiesAllDie = true;
            }
            if(GameState.level == 1 || GameState.level == 9) {
                if(itemBonus == null && Utils.isShownGoddess && !Utils.isCollectedItemBonus){
                    itemBonus = new Item(32, 32, ItemType.GODDESS_MASK, stage);
                    System.out.println("item bonus");
                } else {
                    Utils.updatePlayerPosition(new Vector2(man.getX(), man.getY()));
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
            }else if (item.type.equals(ItemType.BOMB_PASS)) {
                GameState.bombPass = true;
            } else if(item.type.equals(ItemType.MYSTERY)){
                time = MathUtils.random(600,1800);
                GameState.mystery = true;
                time -= 1;
                if(time <= 0) {
                    GameState.mystery = false;
                }
            } else if(item.type.equals(ItemType.FLAME_PASS)){

            }
            item.remove();
            item = null;
            collectSound.play();
        }
        if(itemBonus != null && man.getBound().overlaps(itemBonus.getBound())){
            if (itemBonus.type.equals(ItemType.GODDESS_MASK)) {
                GameState.score += 20000;
                Utils.isCollectedItemBonus = true;
            }
            itemBonus.remove();
            itemBonus = null;
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
                    man.moveBy(Utils.MAN_SPEED, 0);
                    float diff = diffirentYCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(0, diff);
                    }
                } else if (man.direction.equalsIgnoreCase("R")) {
                    man.moveBy(-Utils.MAN_SPEED, 0);
                    float diff = diffirentYCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(0, diff);
                    }
                } else if (man.direction.equalsIgnoreCase("U")) {
                    man.moveBy(0, -Utils.MAN_SPEED);
                    float diff = diffirentXCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(diff, 0);
                    }
                } else if (man.direction.equalsIgnoreCase("D")) {
                    man.moveBy(0, Utils.MAN_SPEED);
                    float diff = diffirentXCor(man, wall);
                    if (Math.abs(diff) < 10) {
                        man.moveBy(diff, 0);
                    }
                }
                break;
            }
        }
        if(!GameState.bombPass){
            for (Bomb b : bombs) {
                if (checkCollision(b, man)) {
                    if (man.direction.equalsIgnoreCase("L")) {
                        man.moveBy(Utils.MAN_SPEED, 0);
                        float diff = diffirentYCor(man, b);
                        if (Math.abs(diff) < 10) {
                            man.moveBy(0, diff);
                        }
                    } else if (man.direction.equalsIgnoreCase("R")) {
                        man.moveBy(-Utils.MAN_SPEED, 0);
                        float diff = diffirentYCor(man, b);
                        if (Math.abs(diff) < 10) {
                            man.moveBy(0, diff);
                        }
                    } else if (man.direction.equalsIgnoreCase("U")) {
                        man.moveBy(0, -Utils.MAN_SPEED);
                        float diff = diffirentXCor(man, b);
                        if (Math.abs(diff) < 10) {
                            man.moveBy(diff, 0);
                        }
                    } else if (man.direction.equalsIgnoreCase("D")) {
                        man.moveBy(0, Utils.MAN_SPEED);
                        float diff = diffirentXCor(man, b);
                    if (Math.abs(diff) < 10) {
                            man.moveBy(diff, 0);
                        }
                    }
                    break;
                }
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
                }else if (cell == Utils.ENEMY_TYPE4) {
                    Enemy4 enemy4 = new Enemy4(x, y, stage);
                    enemies.add(enemy4);
                }else if (cell == Utils.ENEMY_TYPE5) {
                    Enemy5 enemy5 = new Enemy5(x, y, stage);
                    enemies.add(enemy5);
                } else if(cell == Utils.ENEMY_TYPE6){
                    Enemy6 enemy6 = new Enemy6(x,y,stage);
                    enemies.add(enemy6);
                } else if (cell == Utils.ENEMY_TYPE7){
                    Enemy7 enemy7 = new Enemy7(x,y,stage);
                    enemies.add(enemy7);
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
