package thaydac.com;

public enum EnemySpeed {
    SLOWEST(0.5f),
    SLOW(1f),
    NORMAL(2),
    FAST(4);

    private final float value;

    EnemySpeed(float value){
        this.value = value;
    }

    public float getValue(){
        return value;
    }

}
