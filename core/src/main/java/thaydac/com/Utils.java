package thaydac.com;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Utils {
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
        Preferences preferences = Gdx.app.getPreferences("game_pref");
        preferences.putInteger("level", GameStage.level);
        preferences.putInteger("score",GameStage.score);
        preferences.putInteger("bombNumber",GameStage.bomNumber);
        preferences.putInteger("bombPower",GameStage.bombPower);
        preferences.flush();
    }
    public static void loadGame(){
        Preferences preferences = Gdx.app.getPreferences("game_pref");
        GameStage.level = preferences.getInteger("level",0);
        GameStage.score = preferences.getInteger("score",0);
        GameStage.bomNumber = preferences.getInteger("bombPower",1);
        GameStage.bomNumber = preferences.getInteger("bombNumber",1);
    }
}
