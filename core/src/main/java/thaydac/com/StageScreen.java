package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class StageScreen implements Screen {
    StartGame game;
    public static GlyphLayout layout = new GlyphLayout() ;
    Music stageMusic;
    StageScreen(StartGame game){
        this.game = game;
        stageMusic = Gdx.audio.newMusic(Gdx.files.internal("stage.mp3"));
    }
    @Override
    public void show() {
        // Hết mạng rồi thì chơi lại từ đầu
        if(GameState.left < 1){
            GameState.reset();
            Utils.saveGame();
        }
        if ((GameState.level == 100) || (GameState.level == 101)) {
            layout.setText(game.font, "SURPRISE");
        } else if(GameState.level == 103){
            layout.setText(game.font,"STAGE D");
        }else if(GameState.level == 102){
            layout.setText(game.font,"STAGE C");
        } else if(GameState.level == 108){
            layout.setText(game.font,"STAGE G");
        }else if(GameState.level == 109){
            layout.setText(game.font,"STAGE H");
        } else if (GameState.level == 51){

        } else{
            layout.setText(game.font,"STAGE " + GameState.level);
        }
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
