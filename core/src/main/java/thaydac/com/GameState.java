package thaydac.com;

public class GameState {
    public static int level = 1;
    public static int score = 0;
    public static int bombNumber = 1;
    public static int bombPower = 1;
    public static int left = 3;

    public static void reset(){
        left = 3;
        level = 1;
        score = 0;
        bombPower = 1;
        bombNumber = 1;
    }
}
