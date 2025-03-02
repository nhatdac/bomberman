package thaydac.com;

import com.badlogic.gdx.math.MathUtils;
import thaydac.com.enemies.EnemyActor;

public enum EnemySpeed {
    ZERO(0),
    SLOWEST(0.5f),
    SLOW(1f),
    NORMAL(2),
    FAST(4f);

    private final float value;

    EnemySpeed(float value){
        this.value = value;
    }

    public float getValue(){
        return value;
    }

}
