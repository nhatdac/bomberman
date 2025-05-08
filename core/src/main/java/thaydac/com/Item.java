package thaydac.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Item extends MyActor {
    ItemType type;

    Item(float x, float y, ItemType type, Stage s) {
        super(x, y, s);
        this.type = type;

        TextureRegion tRegion = new TextureRegion(new Texture("items.png"));

        int tileSize = 16;
        int xIndex = 0;

        switch (type) {
            case BOMB_NUMBER -> xIndex = 0;
            case BOMB_POWER -> xIndex = 1;
            case SPEED -> xIndex = 2;
            case WALLPASS -> xIndex = 3;
            case DETONATOR -> xIndex = 4;
            case BOMB_PASS -> xIndex = 5;
            case FLAME_PASS -> xIndex = 6;
            case MYSTERY -> xIndex = 7;
            case BONUS_TARGET -> xIndex = 8;
            case GODDESS_MASK -> xIndex = 9;
            case COLA_BOTTLE -> xIndex = 10;
            case FAMICOM -> xIndex = 11;
            case NAKAMOTO_SAN -> xIndex = 12;
            case DEZENIMAN_SAN -> xIndex = 13;
        }

        tRegion.setRegion(xIndex * tileSize, 0, tileSize, tileSize);

        textureRegion = tRegion;
        setSize(32, 32);
    }
}
