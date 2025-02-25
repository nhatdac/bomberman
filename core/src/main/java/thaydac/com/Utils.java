package thaydac.com;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import thaydac.com.enemies.EnemyActor;

import java.util.*;

public class Utils {
    public static final String PREF_NAME = "game_prefs";
    public static final String BOMB_NUMBER = "bombNumber";
    public static final String BOMB_POWER = "bombPower";
    public static final String DECORATOR = "decorator";
    public static final String LEVEL = "level";
    public static final String SCORE = "score";
    public static final String LEFT = "left";
    public static final String WALLPASS = "wallpass";

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

    public static boolean isShownGoddess = false;
    public static boolean isShownBonusTarget = false;
    public static boolean isCollectedItemBonus = false;

    private static List<Vector2> boundaryPositions = new ArrayList<>();
    private static int visitedIndex = -1; // Chỉ mục đã đi qua
    private static boolean tracking = false; // Đánh dấu bắt đầu tính toán vòng
    private static Vector2 lastPosition = null; // Lưu vị trí trước đó

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
        enemyConfig.put(7, Map.of(ENEMY_TYPE2, 2, ENEMY_TYPE3, 3, ENEMY_TYPE5, 2));
        enemyConfig.put(9, Map.of(ENEMY_TYPE2, 1, ENEMY_TYPE3, 1, ENEMY_TYPE4, 4,ENEMY_TYPE5, 1)); // Level 2 có 3 enemy1 và 3 enemy2
        enemyConfig.put(10, Map.of(ENEMY_TYPE2,1,ENEMY_TYPE3,1,ENEMY_TYPE4,1,ENEMY_TYPE5,3,ENEMY_TYPE6,1));
        enemyConfig.put(11, Map.of(ENEMY_TYPE2,1,ENEMY_TYPE3,2,ENEMY_TYPE4,3,ENEMY_TYPE5,1,ENEMY_TYPE6,1));
        enemyConfig.put(12, Map.of(ENEMY_TYPE2,1,ENEMY_TYPE3,1,ENEMY_TYPE4,1,ENEMY_TYPE5,4,ENEMY_TYPE6,1));
        enemyConfig.put(13, Map.of(ENEMY_TYPE3, 3, ENEMY_TYPE4, 3, ENEMY_TYPE5, 3));
        enemyConfig.put(14, Map.of(ENEMY_TYPE6, 7, ENEMY_TYPE7, 1));
        enemyConfig.put(15, Map.of(ENEMY_TYPE3,1,ENEMY_TYPE4,3,ENEMY_TYPE5,3,ENEMY_TYPE7,1));
        enemyConfig.put(16, Map.of(ENEMY_TYPE4, 3, ENEMY_TYPE5, 4, ENEMY_TYPE7, 1));
        enemyConfig.put(17, Map.of(ENEMY_TYPE3, 5, ENEMY_TYPE5, 2, ENEMY_TYPE7, 1));
        enemyConfig.put(18, Map.of(ENEMY_TYPE1, 3, ENEMY_TYPE2, 3, ENEMY_TYPE7, 2));
        enemyConfig.put(19, Map.of(ENEMY_TYPE1, 1, ENEMY_TYPE2, 1, ENEMY_TYPE3, 3,ENEMY_TYPE6, 1,ENEMY_TYPE7, 2));
        enemyConfig.put(20, Map.of(ENEMY_TYPE2, 1, ENEMY_TYPE3, 1, ENEMY_TYPE4, 1,ENEMY_TYPE5, 2,ENEMY_TYPE6, 1,ENEMY_TYPE7, 2));
        enemyConfig.put(21, Map.of(ENEMY_TYPE5, 4, ENEMY_TYPE6, 3, ENEMY_TYPE7,2));
        enemyConfig.put(22, Map.of(ENEMY_TYPE3, 4, ENEMY_TYPE4, 3, ENEMY_TYPE5, 1,ENEMY_TYPE7, 1));
        enemyConfig.put(23, Map.of(ENEMY_TYPE3, 2, ENEMY_TYPE4, 2, ENEMY_TYPE5, 2,ENEMY_TYPE6, 2,ENEMY_TYPE7, 1));
        enemyConfig.put(24,Map.of(ENEMY_TYPE3, 1, ENEMY_TYPE4, 1,ENEMY_TYPE5,4,ENEMY_TYPE6,2,ENEMY_TYPE7,1));
        enemyConfig.put(25,Map.of(ENEMY_TYPE2,2 ,ENEMY_TYPE3,1,ENEMY_TYPE4,1,ENEMY_TYPE5,2,ENEMY_TYPE6,2,ENEMY_TYPE7,1));
        enemyConfig.put(26,Map.of(ENEMY_TYPE1,1,ENEMY_TYPE2,1,ENEMY_TYPE3,1,ENEMY_TYPE4,1,ENEMY_TYPE5,2,ENEMY_TYPE6,1,ENEMY_TYPE7,1));
        enemyConfig.put(27,Map.of(ENEMY_TYPE1,1,ENEMY_TYPE2,1,ENEMY_TYPE5,5,ENEMY_TYPE6,1,ENEMY_TYPE7,1));
        enemyConfig.put(28,Map.of(ENEMY_TYPE2,1,ENEMY_TYPE3,3,ENEMY_TYPE4,3,ENEMY_TYPE5,1,ENEMY_TYPE7,1));
        enemyConfig.put(29,Map.of(ENEMY_TYPE5,2,ENEMY_TYPE6,5,ENEMY_TYPE7,2));
        enemyConfig.put(30,Map.of(ENEMY_TYPE3,3,ENEMY_TYPE4,2,ENEMY_TYPE5,1,ENEMY_TYPE6,2,ENEMY_TYPE7,1));
        enemyConfig.put(31, Map.of(ENEMY_TYPE2, 2,ENEMY_TYPE3,2,ENEMY_TYPE4,2,ENEMY_TYPE5,2,ENEMY_TYPE6,2));
        enemyConfig.put(32, Map.of(ENEMY_TYPE2, 1, ENEMY_TYPE3, 1,ENEMY_TYPE4,3,ENEMY_TYPE5,4,ENEMY_TYPE7,1));
        enemyConfig.put(33, Map.of(ENEMY_TYPE3, 3, ENEMY_TYPE4, 2, ENEMY_TYPE5, 2,ENEMY_TYPE6,1,ENEMY_TYPE7,2));
        enemyConfig.put(34, Map.of(ENEMY_TYPE3, 2, ENEMY_TYPE4, 3, ENEMY_TYPE5, 3,ENEMY_TYPE6,0,ENEMY_TYPE7,2));
        enemyConfig.put(35, Map.of(ENEMY_TYPE3, 2, ENEMY_TYPE4, 1, ENEMY_TYPE5, 3,ENEMY_TYPE6,1,ENEMY_TYPE7,2));
        enemyConfig.put(36, Map.of(ENEMY_TYPE3, 2, ENEMY_TYPE4, 2, ENEMY_TYPE5, 3,ENEMY_TYPE6,0,ENEMY_TYPE7,3));
        enemyConfig.put(37, Map.of(ENEMY_TYPE3, 2, ENEMY_TYPE4, 1, ENEMY_TYPE5, 3,ENEMY_TYPE6,1,ENEMY_TYPE7,3));
        enemyConfig.put(100,Map.of(ENEMY_TYPE1,125));
        enemyConfig.put(101,Map.of(ENEMY_TYPE2, 125));
        enemyConfig.put(103, Map.of(ENEMY_TYPE4,200));
        enemyConfig.put(102, Map.of(ENEMY_TYPE3,200));
        enemyConfig.put(105,Map.of(ENEMY_TYPE6,100));
        enemyConfig.put(106,Map.of(ENEMY_TYPE5,100));
        // ... thêm các level tiếp theo...

        enemyConfig.put(108, Map.of(ENEMY_TYPE1, 20000));
        enemyConfig.put(109, Map.of(ENEMY_TYPE1, 20000));
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
        GameState.wallPass = preferences.getBoolean(WALLPASS, false);

        System.out.println("Saved Data: " + preferences.get());
    }
    // Tạo danh sách các vị trí trên biên theo vòng kim đồng hồ
    public static List<Vector2> getClockwiseBoundary(Vector2 playerPos) {
        if(boundaryPositions.isEmpty()) {
            int count = 0;

            // Hàng trên (trái -> phải)
            for (int x = 32; x <= 31 * 32 - 32 * 2; x += MAN_SPEED) {
                Vector2 v = new Vector2(x, 17 * 32 - 4 * 32);
                boundaryPositions.add(v);
                count ++;
            }
            // Cột phải (trên -> dưới)
            for (int y = 17 * 32 - 4 * 32 - MAN_SPEED; y >= 32; y -= MAN_SPEED) {
                Vector2 v= new Vector2(31 * 32 - 32 * 2, y);
                boundaryPositions.add(v);
                count ++;
            }
            // Hàng dưới (phải -> trái)
            for (int x = 31 * 32 - 2 * 32 - MAN_SPEED; x >= 32; x -= MAN_SPEED) {
                boundaryPositions.add(new Vector2(x, 32));
                count ++;
            }
            // Cột trái (dưới -> trên)
            for (int y = 32 * 1 + MAN_SPEED; y <= 17 * 32 - 4 * 32 - MAN_SPEED; y += MAN_SPEED) {
                boundaryPositions.add(new Vector2(32, y));
                count ++;
            }
        }

        // Tìm vị trí của player trong danh sách
        int index = boundaryPositions.indexOf(playerPos);

        // Nếu tìm thấy, đưa vị trí đó lên đầu danh sách
        if (index != -1) {
            Collections.rotate(boundaryPositions, -index);
        }

        return boundaryPositions;
    }
    // Cập nhật vị trí nhân vật và kiểm tra vòng đi
    public static void updatePlayerPosition(Vector2 playerPos) {
        if (playerPos.equals(lastPosition)) {
            // Nếu nhân vật đứng yên, không làm gì cả, giữ nguyên tiến trình
            return;
        }

        // Nếu chưa bắt đầu theo dõi, đặt index hiện tại làm mốc
        if (!tracking) {
            getClockwiseBoundary(playerPos);
            visitedIndex = 0;
            tracking = true;
            lastPosition = playerPos;
            return;
        }

        int index = boundaryPositions.indexOf(playerPos);

        if (index == -1) {
            // Nếu nhân vật rời khỏi biên, hủy việc theo dõi
            resetTracking();
            return;
        }

        if (index == (visitedIndex + 1) % boundaryPositions.size()) {
            // Nếu nhân vật đi đúng thứ tự, cập nhật chỉ mục
            visitedIndex = index;
            lastPosition = playerPos;
            // System.out.println("Tiến trình: " + visitedIndex + "/" + boundaryPositions.size() + "/" + playerPos);

            // Đi đủ 1 vòng
            if (visitedIndex == boundaryPositions.size() - 1) {
                isShownGoddess = true;
                resetTracking();
            }
        } else if (index == (visitedIndex - 1 + boundaryPositions.size()) % boundaryPositions.size()) {
            // Nếu đi ngược lại, reset tiến trình
            resetTracking();
        } else {
            // Nếu nhân vật đi không theo thứ tự, reset tiến trình
            resetTracking();
        }
    }

    // Reset lại quá trình theo dõi
    private static void resetTracking() {
        visitedIndex = -1;
        tracking = false;
        lastPosition = null;
    }
}
