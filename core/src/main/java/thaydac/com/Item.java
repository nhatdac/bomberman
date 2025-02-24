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
        } else if(this.type.equals(ItemType.BONUS_TARGET)){
            tRegion.setRegion(16 * 8, 0, 16, 16);
        }else if(this.type.equals(ItemType.COLA_BOTTLE)){
            tRegion.setRegion(16 * 10, 0, 16, 16);
        }else if(this.type.equals(ItemType.FAMICOM )){
            tRegion.setRegion(16 * 11, 0, 16, 16);
        }else if(this.type.equals(ItemType.NAKAMOTO_SAN )){
            tRegion.setRegion(16 * 12, 0, 16, 16);
        }else if(this.type.equals(ItemType.DEZENIMAN_SAN)){
            tRegion.setRegion(16 * 13, 0, 16, 16);
        }
        textureRegion = tRegion;
        setSize(32, 32);
    }
}
