package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class MenuScreen implements Screen {
    Texture background;
    Texture playTexture;
    StartGame game;
    int playX;
    int playY;

    Music welcomeMusic;

    MenuScreen(StartGame game){
        this.game = game;
        welcomeMusic = Gdx.audio.newMusic(Gdx.files.internal("welcome.mp3"));
    }
    @Override
    public void show() {
        background = new Texture("about.png");
        playTexture = new Texture("play.png");
        playX = 200;
        playY = 110;
        welcomeMusic.play();
    }

    @Override
    public void render(float v) {
        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(playTexture, playX, playY, 32, 16);
        game.batch.end();
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            if(playY == 110){
                playY = 145;
            } else {
                playY = 110;
            }
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            if (playY == 110){
                Utils.loadGame();
            }
            game.setScreen(new StageScreen(game));
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
        welcomeMusic.stop();
    }

    @Override
    public void dispose() {

    }
}
