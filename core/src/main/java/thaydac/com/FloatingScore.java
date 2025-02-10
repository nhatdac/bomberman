package thaydac.com;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;

public class FloatingScore extends Actor {
    private String text;
    private BitmapFont font;

    public FloatingScore(float x, float y, int score) {
        text = "" + score;
        font = new BitmapFont(); // Font mặc định của LibGDX
        font.setColor(Color.WHITE);

        setPosition(x, y + 32);
        setColor(1, 1, 1, 1); // Đảm bảo alpha ban đầu là 1 (không trong suốt)

        // Hiệu ứng di chuyển lên + làm mờ dần + xóa sau 1 giây
        addAction(Actions.sequence(
            Actions.moveBy(0, 30, 1f),  // Bay lên 30 pixel trong 1 giây
            Actions.fadeOut(1f),        // Làm mờ trong 1 giây
            Actions.removeActor()       // Xóa khỏi stage
        ));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.setColor(1, 1, 1, getColor().a * parentAlpha);
        font.draw(batch, text, getX(), getY());
    }
}
