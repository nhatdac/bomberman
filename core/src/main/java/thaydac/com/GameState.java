package thaydac.com;

public class GameState {
    public static int level = 17;
    public static int score = 0;
    public static int bombNumber = 1;
    public static int bombPower = 1;
    public static int left = 1000;
    public static boolean decorator = false;
    public static boolean goddessMask = false;
    public static boolean bonusTarget = false;
    public static boolean colaBottle = false;
    public static boolean nakamotoSan = false;
    public static boolean dezenimanSan = false;
    public static boolean bombPass = false;

    public static void reset(){
        left = 3;
        level = 1;
        score = 0;
        bombPower = 1;
        bombNumber = 1;
        decorator = false;
        bombPass = false;
        goddessMask = false;
        bonusTarget  = false;
        colaBottle  = false;
        nakamotoSan  = false;
        dezenimanSan  = false;
    }
}
