package thaydac.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Map;

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
        GameState.enemyInDoor = true;

        // Ánh xạ level với chữ tương ứng
        Map<Integer, String> stageMap = Map.of(
            Level.A.getValue(), "STAGE A",
            Level.B.getValue(), "STAGE B",
            Level.C.getValue(), "STAGE C",
            Level.D.getValue(), "STAGE D",
            Level.E.getValue(), "STAGE E",
            Level.F.getValue(), "STAGE F",
            Level.G.getValue(), "STAGE G",
            Level.H.getValue(), "STAGE H",
            Level.I.getValue(), "STAGE I",
            Level.J.getValue(), "STAGE J"
        );
        // Nếu là Level.WIN thì không hiển thị gì
        if (GameState.level == Level.WIN.getValue()) {
            layout.setText(game.font, "WONDERFUL");
        } else {
            // Lấy giá trị tương ứng hoặc dùng "STAGE X" nếu không có trong map
            String stageText = stageMap.getOrDefault(GameState.level, "STAGE " + GameState.level);
            layout.setText(game.font, stageText);
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
