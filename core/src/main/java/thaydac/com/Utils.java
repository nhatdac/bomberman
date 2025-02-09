package thaydac.com;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Utils {
    public static final String PREF_NAME = "game_prefs";
    public static final String BOMB_NUMBER = "bombNumber";
    public static final String BOMB_POWER = "bombPower";
    public static final String LEVEL = "level";
    public static final String SCORE = "score";
    public static final String LEFT = "left";


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


        for (int i = 2; i < wallArray.length; i++) {
            for (int j = 0; j < wallArray[i].length; j++) {
                if (wallArray[i][j] == 1) {
                    continue; // nếu vị trí đã có giá trị là 1 thì bỏ qua
                } else if (wallArray[i][j] == 0) {
                    wallArray[i][j] = rand.nextInt(3) < 2 ? 0 : rand.nextInt(3) < 2 ? 2 : rand.nextInt(3) < 2 ? 3 : 4; // Random 0 or 2
                }
            }
        }

        wallArray[3][1] = 0; // Vị trí cho player xuất hiện nên luôn luôn đặt là 0
        wallArray[3][2] = 0; // Vị trí cho player xuất hiện nên luôn luôn đặt là 0
        wallArray[4][1] = 0; // Vị trí cho player xuất hiện nên luôn luôn đặt là 0

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

        preferences.flush();
    }
    public static void loadGame(){
        Preferences preferences = Gdx.app.getPreferences(PREF_NAME);
        GameState.level = preferences.getInteger(LEVEL, 1);
        GameState.score = preferences.getInteger(SCORE, 0);
        GameState.left = preferences.getInteger(LEFT, 3);
        GameState.bombNumber = preferences.getInteger(BOMB_NUMBER, 1);
        GameState.bombPower = preferences.getInteger(BOMB_POWER, 1);

        System.out.println("Saved Data: " + preferences.get());
    }
}
