package thaydac.com;

public class GameState {
    public static int level = 10;
    public static int score = 0;
    public static int bombNumber = 5;
    public static int bombPower = 4;
    public static int left = 3;
    public static boolean decorator = false;
    public static boolean goddessMask = false;
    public static boolean bonusTarget = false;
    public static boolean colaBottle = false;
    public static boolean nakamotoSan = false;
    public static boolean dezenimanSan = false;
    public static boolean bombPass = false;
    public static boolean wallPass = false;
    public static boolean flamepass = false;
    public static boolean mystery = false;

    public static void reset(){
        left = 3;
        level = 1;
        score = 0;
        bombPower = 1;
        bombNumber = 1;
        decorator = false;
        bombPass = false;
        mystery = false;
        wallPass = false;
        flamepass = false;
        goddessMask = false;
        bonusTarget  = false;
        colaBottle  = false;
        nakamotoSan  = false;
        dezenimanSan  = false;
    }
}
