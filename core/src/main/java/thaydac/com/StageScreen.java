package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class StageScreen implements Screen {
    StartGame game;
    GlyphLayout layout;
    Music stageMusic;
    StageScreen(StartGame game){
        this.game = game;
        layout = new GlyphLayout();
        stageMusic = Gdx.audio.newMusic(Gdx.files.internal("stage.mp3"));
    }
    @Override
    public void show() {
        layout.setText(game.font,"STAGE " + Master.level);
        stageMusic.play();
        stageMusic.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                game.setScreen(new Master(game));
            }
        });
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.BLACK);
        game.batch.begin();
        game.font.draw(game.batch, layout, Gdx.graphics.getWidth()/2 - layout.width/2, Gdx.graphics.getHeight()/2 + layout.height/2);
        game.batch.end();
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
