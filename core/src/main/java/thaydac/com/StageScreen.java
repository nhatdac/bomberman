package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class StageScreen implements Screen {
    StartGame game;
    int timing = 60;
    GlyphLayout layout;
    StageScreen(StartGame game){
        this.game = game;
        layout = new GlyphLayout();
    }
    @Override
    public void show() {
        layout.setText(game.font,"STAGE " + Master.level);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.BLACK);
        game.batch.begin();
        game.font.draw(game.batch, layout, Gdx.graphics.getWidth()/2 - layout.width/2, Gdx.graphics.getHeight()/2 + layout.height/2);
        game.batch.end();
        timing--;
        if(timing < 1){
            game.setScreen(new Master(game));
        }
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
