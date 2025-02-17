package thaydac.com;

public class GameState {
    public static int level = 108;
    public static int score = 0;
    public static int bombNumber = 100;
    public static int bombPower = 3;
    public static int left = 1000;
    public static boolean decorator = false;
    public static boolean goddessMask = false;
    public static boolean bombPass = false;
    public static boolean speed = false;

    public static void reset(){
        left = 3;
        level = 1;
        score = 0;
        bombPower = 1;
        bombNumber = 1;
        decorator = false;
        bombPass = false;
        goddessMask = false;
        speed = false;
    }
}
