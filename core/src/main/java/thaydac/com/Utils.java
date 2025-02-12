package thaydac.com;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

import java.util.*;

public class Utils {
    public static final String PREF_NAME = "game_prefs";
    public static final String BOMB_NUMBER = "bombNumber";
    public static final String BOMB_POWER = "bombPower";
    public static final String DECORATOR = "decorator";
    public static final String BOMB_PASS = "bombPass";
    public static final String LEVEL = "level";
    public static final String SCORE = "score";
    public static final String LEFT = "left";

    public static final int MAN_SPEED = 2;

    public static final int EMPTY_TYPE = 0;
    public static final int WALL_TYPE = 1;
    public static final int BRICK_TYPE = 2;

    public static final int ENEMY_TYPE1 = 3;
    public static final int ENEMY_TYPE2 = 4;
    public static final int ENEMY_TYPE3 = 5;
    public static final int ENEMY_TYPE4 = 6;
    public static final int ENEMY_TYPE5 = 7;
    public static final int ENEMY_TYPE6 = 8;
    public static final int ENEMY_TYPE7 = 9;
    public static final int ENEMY_TYPE_FAST = 10;

    public static int[][] buildMap() {
        Random rand = new Random();

        // Mảng tạo khung bản đồ
        int[][] wallArray = new int[][] {
            // 0: trống, 1: tường, 2: gạch, 3: địch
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        // Danh sách số lượng enemy theo level
        Map<Integer, Map<Integer, Integer>> enemyConfig = new HashMap<>();
        enemyConfig.put(1, Map.of(ENEMY_TYPE1, 6)); // Level 1 có 6 enemy1, gọi là loại 3, bởi vì số 0, 1, 2 đã dùng cho ô trống, tường, gạch rồi.
        enemyConfig.put(2, Map.of(ENEMY_TYPE1, 3, ENEMY_TYPE2, 3)); // Level 2 có 3 enemy1 và 3 enemy2
        enemyConfig.put(3, Map.of(ENEMY_TYPE1, 2, ENEMY_TYPE2, 2, ENEMY_TYPE3, 2)); // Level 2 có 3 enemy1 và 3 enemy2
        enemyConfig.put(9, Map.of(ENEMY_TYPE2, 1, ENEMY_TYPE3, 1, ENEMY_TYPE4, 4,ENEMY_TYPE5, 1)); // Level 2 có 3 enemy1 và 3 enemy2
        // ... thêm các level tiếp theo...

        List<int[]> emptyPositions = new ArrayList<>();

        // Tìm tất cả vị trí trống
        for (int i = 3; i < wallArray.length - 1; i++) {
            for (int j = 1; j < wallArray[i].length - 1; j++) {
                if((i == 3 && j == 1) || (i == 3 && j == 2) || (i == 4 && j == 1)){ // 3 ô đầu này để cho Man xuất hiện
                    continue;
                } else if (wallArray[i][j] == 0) {
                    emptyPositions.add(new int[]{i, j});
                }
            }
        }

        // Lấy cấu hình enemy của level hiện tại
        Map<Integer, Integer> levelEnemies = enemyConfig.getOrDefault(GameState.level, new HashMap<>());

        // Đặt enemy theo số lượng quy định
        for (Map.Entry<Integer, Integer> entry : levelEnemies.entrySet()) {
            int enemyType = entry.getKey(); // Loại enemy (1, 2, 3, ...)
            int count = entry.getValue(); // Số lượng cần đặt

            while (count > 0 && !emptyPositions.isEmpty()) {
                int index = rand.nextInt(emptyPositions.size());
                int[] pos = emptyPositions.remove(index);
                wallArray[pos[0]][pos[1]] = enemyType;
                count--;
            }
        }

        // Sau khi đặt enemy, đặt 1/3 số ô trống còn lại thành gạch (2)
        int brickCount = emptyPositions.size() / 3;
        while (brickCount > 0 && !emptyPositions.isEmpty()) {
            int index = rand.nextInt(emptyPositions.size());
            int[] pos = emptyPositions.remove(index);
            wallArray[pos[0]][pos[1]] = BRICK_TYPE; // Đặt giá trị 2 (gạch)
            brickCount--;
        }

        ////

        System.out.println("int[][] wallArray = new int[][] {");
        System.out.println("    // 0: trống, 1: tường, 2: gạch");
        for (int i = 0; i < wallArray.length; i++) {
            System.out.print("    {");
            for (int j = 0; j < wallArray[i].length; j++) {
                System.out.print(wallArray[i][j]);
                if (j < wallArray[i].length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("},");
        }
        System.out.println("};");

        return wallArray;
    }
    public static void saveGame(){
        Preferences preferences = Gdx.app.getPreferences(PREF_NAME);
        preferences.putInteger(LEVEL, GameState.level);
        preferences.putInteger(SCORE, GameState.score);
        preferences.putInteger(LEFT, GameState.left);
        preferences.putInteger(BOMB_NUMBER, GameState.bombNumber);
        preferences.putInteger(BOMB_POWER, GameState.bombPower);
        preferences.putBoolean(DECORATOR, GameState.decorator);

        preferences.flush();
    }
    public static void loadGame(){
        Preferences preferences = Gdx.app.getPreferences(PREF_NAME);
        GameState.level = preferences.getInteger(LEVEL, 1);
        GameState.score = preferences.getInteger(SCORE, 0);
        GameState.left = preferences.getInteger(LEFT, 3);
        GameState.bombNumber = preferences.getInteger(BOMB_NUMBER, 1);
        GameState.bombPower = preferences.getInteger(BOMB_POWER, 1);
        GameState.decorator = preferences.getBoolean(DECORATOR, false);

        System.out.println("Saved Data: " + preferences.get());
    }
}
