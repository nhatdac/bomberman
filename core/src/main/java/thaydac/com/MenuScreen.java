package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class MenuScreen implements Screen {
    private static final int PLAY_X = 200;
    private static final int PLAY_Y_OPTION1 = 110;
    private static final int PLAY_Y_OPTION2 = 145;
    private static final int PLAY_WIDTH = 32;
    private static final int PLAY_HEIGHT = 16;

    private Texture background;
    private Texture playTexture;
    private Music welcomeMusic;

    private final StartGame game;
    private int currentPlayY;

    public MenuScreen(StartGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        background = new Texture("about.png");
        playTexture = new Texture("play.png");
        welcomeMusic = Gdx.audio.newMusic(Gdx.files.internal("welcome.mp3"));
        welcomeMusic.setLooping(true);
        welcomeMusic.play();

        currentPlayY = PLAY_Y_OPTION1;
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(playTexture, PLAY_X, currentPlayY, PLAY_WIDTH, PLAY_HEIGHT);
        game.batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            currentPlayY = (currentPlayY == PLAY_Y_OPTION1) ? PLAY_Y_OPTION2 : PLAY_Y_OPTION1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (currentPlayY == PLAY_Y_OPTION1) {
                Utils.loadGame();
            }
            game.setScreen(new StageScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        if (welcomeMusic != null) {
            welcomeMusic.stop();
        }
    }

    @Override
    public void dispose() {
        if (background != null) background.dispose();
        if (playTexture != null) playTexture.dispose();
        if (welcomeMusic != null) welcomeMusic.dispose();
    }
}
