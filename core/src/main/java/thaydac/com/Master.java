package thaydac.com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Master extends ApplicationAdapter {
    private SpriteBatch batch;
    OrthographicCamera camera;
    BitmapFont font;
    int time = 150;
    int count = 0;
    int score = 0;
    int left = 3;

    Stage stage;
    private Background background;
    private Panel panel;
    static Man man;
    static Item item;

    static Array<MyActor> walls = new Array<>();
    static Array<Brick> briches = new Array<>();
    Array<MyActor> enemies = new Array<>();
    Array<Bomb> bombs = new Array<>();
    Array<Explosion> explosions = new Array<>();
    Sound dieSound;
    Sound collectSound;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        stage = new Stage();

      //  buildMap();
        generateMap();

        man = new Man(32, Gdx.graphics.getHeight() - 32*4, stage);

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Lonely Cake.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 32;
        fontParameter.color = Color.WHITE;
        font = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();

        dieSound = Gdx.audio.newSound(Gdx.files.internal("die.mp3"));
        collectSound = Gdx.audio.newSound(Gdx.files.internal("collect.mp3"));
    }

    @Override
    public void render() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            int xMan = Math.round(man.getX()/32)*32; // làm tròn tọa độ x để chuẩn bị đặt bom cho chuẩn
            int yMan = Math.round(man.getY()/32)*32;
            boolean positionOK = true;
            for (Bomb b: bombs) {
                if(b.getX() == xMan && b.getY() == yMan){
                    positionOK = false;
                    break;
                }
            }
            if(positionOK && man.bombNumber > 0){
                Bomb bomb = new Bomb(xMan,yMan, stage, bombs, explosions);
                bombs.add(bomb);
                man.bombNumber--;
            }
        }

        if(!man.isDie){
            for (Explosion explosion: explosions) {
                if(explosion.getBound().overlaps(man.getBound())){
                    man.isDie = true;
                    man.time = 0;
                    dieSound.play();
                    break;
                }
            }
            for (MyActor enemy: enemies) {
                if(enemy.getBound().overlaps(man.getBound())){
                    man.isDie = true;
                    man.time = 0;
                    dieSound.play();
                    break;
                }
            }
        }

        stage.act();
        collisionWall();
        collectItems();
        man.setZIndex(stage.getActors().size - 1);
        stage.draw();

        count ++;
        if(count%60 == 0){
            time--;
        }
        batch.begin();
        font.draw(batch, "TIME: " + time, 32, Gdx.graphics.getHeight() - 16);
        font.draw(batch, score < 10 ? "0" + score: "" + score, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() - 16);
        font.draw(batch, "LEFT: " + left, Gdx.graphics.getWidth() - 128, Gdx.graphics.getHeight() - 16);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void collectItems(){
        if(item != null && man.getBound().overlaps(item.getBound())){
            if(item.type.equals(ItemType.BOMB_NUMBER)){
                man.bombNumber++;
            } else if (item.type.equals(ItemType.BOMB_POWER)) {
                man.bombPower++;
            }
            item.remove();
            item = null;
            collectSound.play();
        }
    }

    public void collisionWall(){
        for (MyActor wall: walls) {
           if(checkCollision(wall, man)){
                if(man.direction.equalsIgnoreCase("L")){
                    man.moveBy(2, 0);
                    float diff = diffirentYCor(man, wall);
                    if(Math.abs(diff) < 10){
                        man.moveBy(0, diff);
                    }
                } else if(man.direction.equalsIgnoreCase("R")){
                    man.moveBy(-2, 0);
                    float diff = diffirentYCor(man, wall);
                    if(Math.abs(diff) < 10){
                        man.moveBy(0, diff);
                    }
                } else if(man.direction.equalsIgnoreCase("U")){
                    man.moveBy(0, -2);
                    float diff = diffirentXCor(man, wall);
                    if(Math.abs(diff) < 10){
                        man.moveBy(diff, 0);
                    }
                } else if(man.direction.equalsIgnoreCase("D")){
                    man.moveBy(0, 2);
                    float diff = diffirentXCor(man, wall);
                    if(Math.abs(diff) < 10){
                        man.moveBy(diff, 0);
                    }
                }
                break;
            }
        }
    }

    public float diffirentYCor(MyActor _man, MyActor _wall){
        float diff;
        if(_man.getY() > _wall.getY()){
            diff = _wall.getY() + _wall.getHeight() - _man.getY();
        } else {
            diff = -1 * (_man.getY() + _man.getHeight() - _wall.getY());
        }

        return  diff;
    }

    public float diffirentXCor(MyActor _man, MyActor _wall){
        float diff;
        if(_man.getX() > _wall.getX()){
            diff = _wall.getX() + _wall.getWidth() - _man.getX();
        } else {
            diff = -1 * (_man.getX() + _man.getWidth() - _wall.getX());
        }

        return  diff;
    }

    public void buildMap(){
        background = new Background(0, 0, stage);
        panel = new Panel(0, Gdx.graphics.getHeight() - 32, stage);

        int xWall = 0;
        int yWall = 0;
        for(int i = 0; i < 31; i++){
            Wall wall = new Wall(xWall, yWall, stage);
            walls.add(wall);
            xWall += 32;
        }
        xWall = 0;
        yWall = Gdx.graphics.getHeight() - 32*2;
        for(int i = 0; i < 31; i++){
            Wall wall = new Wall(xWall, yWall, stage);
            walls.add(wall);
            xWall += 32;
        }
        xWall = 0;
        yWall = 32;
        for(int i = 0; i < 13; i++){
            Wall wall = new Wall(xWall, yWall, stage);
            walls.add(wall);
            yWall += 32;
        }
        xWall = Gdx.graphics.getWidth() - 32;
        yWall = 32;
        for(int i = 0; i < 13; i++){
            Wall wall = new Wall(xWall, yWall, stage);
            walls.add(wall);
            yWall += 32;
        }
        xWall = 32 * 2;
        yWall = 32 * 2;
        for (int k = 0; k < 15; k++) {
            for (int i = 0; i < 6; i++) {
                Wall wall = new Wall(xWall, yWall, stage);
                walls.add(wall);
                yWall += 32 * 2;
            }
            xWall += 32*2;
            yWall -= 6*32*2;
        }

        Brick brick = new Brick(32, 32, stage);
        briches.add(brick);
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
                    if(enemy1Number > 0){
                        Enemy1 enemy1 = new Enemy1(x, y, stage);
                        // Tạo enemy
                        enemies.add(enemy1);
                        enemy1Number--;
                    }
                } else if (cell == 4) {
                    if(enemy2Number > 0){
                        Enemy2 enemy2 = new Enemy2(x, y, stage);
                        // Tạo enemy
                        enemies.add(enemy2);
                        enemy2Number--;
                    }
                }
            }
        }
        int itemPosition = MathUtils.random(0, briches.size - 1);
        int doorPosition = MathUtils.random(0, briches.size - 1);

        while (itemPosition == doorPosition){
            doorPosition = MathUtils.random(0, briches.size - 1);
        }
        briches.get(itemPosition).hasItem = true;
        briches.get(doorPosition).hasDoor = true;
    }


    boolean checkCollision(MyActor _actor1, MyActor _actor2){
        return _actor1.getBound().overlaps(_actor2.getBound());
    }
}
