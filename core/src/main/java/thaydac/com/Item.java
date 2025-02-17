package thaydac.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Item extends MyActor{
    ItemType type;

    Item(float x, float y, ItemType type, Stage s) {
        super(x, y, s);
        TextureRegion tRegion = new TextureRegion(new Texture("items.png"));
        this.type = type;
        if(this.type.equals(ItemType.BOMB_NUMBER)){
            tRegion.setRegion(0, 0, 16, 16);
        } else if(this.type.equals(ItemType.BOMB_POWER)){
            tRegion.setRegion(16, 0, 16, 16);
        }else if(this.type.equals(ItemType.DETONATOR)){
            tRegion.setRegion(16 * 4, 0, 16, 16);
        }else if(this.type.equals(ItemType.BOMB_PASS)){
            tRegion.setRegion(16 * 5, 0, 16, 16);
        }else if(this.type.equals(ItemType.GODDESS_MASK)){
            tRegion.setRegion(16 * 9, 0, 16, 16);
        }else if(this.type.equals(ItemType.Wall_pass)){
            tRegion.setRegion(16 * 3, 0, 16, 16);
        }else if(this.type.equals(ItemType.Flame_pass)){
            tRegion.setRegion(16 * 6, 0, 16, 16);
        }else if(this.type.equals(ItemType.Mystery)){
            tRegion.setRegion(16 * 7, 0, 16, 16);
        }

        textureRegion = tRegion;
        setSize(32, 32);
    }
}
