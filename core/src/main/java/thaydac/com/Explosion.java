package thaydac.com;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class Explosion extends MyActor {
    Animation<TextureRegion> animation;
    float time;
    Array<Explosion> explosions;

    public Explosion(float x, float y, Stage s, Animation<TextureRegion> animation, Array<Explosion> _explosions) {
        super(x, y, s);
        this.explosions = _explosions;
        this.animation = animation;
        this.time = 0;
        setSize(32, 32);
        textureRegion = animation.getKeyFrame(time);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
        textureRegion = animation.getKeyFrame(time);

        if (animation.isAnimationFinished(time)) {
            remove(); // Xóa hiệu ứng nổ khi kết thúc
            explosions.removeValue(this, true);
        }
    }
}
