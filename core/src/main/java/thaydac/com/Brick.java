package thaydac.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Brick extends MyActor{

    Animation<TextureRegion> animation;
    float time;
    boolean isFire = false;
    boolean hasItem = false;
    boolean hasDoor = false;

    Brick(float x, float y, Stage s) {
        super(x, y, s);

        int cot = 7;
        int hang = 1;

        Texture texture = new Texture("brich.png");
        TextureRegion[][] frameBuff = TextureRegion.split(texture, texture.getWidth()/cot, texture.getHeight()/hang);

        TextureRegion[] frames = new TextureRegion[cot*hang];
        int index = 0;
        for (int i = 0; i < hang; i++) {
            for (int j = 0; j < cot; j++) {
                frames[index++] = frameBuff[i][j];
            }
        }

        animation = new Animation<TextureRegion>(0.1f, frames);

        animation.setPlayMode(Animation.PlayMode.NORMAL);

        setSize(32, 32);

        time = 0;
        textureRegion = animation.getKeyFrame(time);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(isFire){
            time += delta;
            textureRegion = animation.getKeyFrame(time);
        }
        if(animation.isAnimationFinished(time)){
            isFire = false;
            if(hasItem){
                if(GameState.level == 1 || GameState.level == 7 || GameState.level == 11 || GameState.level == 12 || GameState.level == 31){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_POWER, getStage());
                } else if((GameState.level == 2)||(GameState.level == 15)){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_NUMBER, getStage());
                } else if((GameState.level == 3)|| (GameState.level == 13)){
                    Master.item = new Item(getX(), getY(), ItemType.DETONATOR, getStage());
                }else if((GameState.level == 9)||(GameState.level == 14)){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_PASS, getStage());
                }else if((GameState.level == 10)||(GameState.level == 16)){
                    Master.item = new Item(getX(), getY(), ItemType.WALLPASS, getStage());
                } else if(GameState.level == 17){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_NUMBER, getStage());
                }else if(GameState.level == 18){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_PASS, getStage());
                }else if(GameState.level == 19){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_NUMBER, getStage());
                }else if(GameState.level == 20){
                    Master.item = new Item(getX(), getY(), ItemType.DETONATOR, getStage());
                }else if(GameState.level == 21){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_PASS, getStage());
                }else if(GameState.level == 22){
                    Master.item = new Item(getX(), getY(), ItemType.DETONATOR, getStage());
                } else if(GameState.level == 23){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_NUMBER, getStage());
                } else if(GameState.level == 24){
                    Master.item = new Item(getX(),getY(),ItemType.DETONATOR, getStage());
                } else if(GameState.level == 25){
                    Master.item = new Item(getX(),getY(),ItemType.BOMB_PASS,getStage());
                } else if(GameState.level == 26){
                    Master.item = new Item(getX(),getY(),ItemType.MYSTERY, getStage());
                } else if(GameState.level == 27){
                    Master.item = new Item(getX(),getY(),ItemType.BOMB_POWER, getStage());
                } else if(GameState.level == 28){
                    Master.item = new Item(getX(),getY(),ItemType.BOMB_NUMBER, getStage());
                } else if(GameState.level == 29){
                    Master.item = new Item(getX(),getY(),ItemType.DETONATOR, getStage());
                } else if(GameState.level == 30){
                    Master.item = new Item(getX(),getY(),ItemType.FLAME_PASS, getStage());
                } else if(GameState.level == 32){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_NUMBER, getStage());
                }else if(GameState.level == 33){
                    Master.item = new Item(getX(), getY(), ItemType.DETONATOR, getStage());
                }else if(GameState.level == 34){
                    Master.item = new Item(getX(), getY(), ItemType.MYSTERY, getStage());
                }else if(GameState.level == 35){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_PASS, getStage());
                }else if(GameState.level == 36){
                    Master.item = new Item(getX(), getY(), ItemType.FLAME_PASS, getStage());
                }else if(GameState.level == 37){
                    Master.item = new Item(getX(), getY(), ItemType.DETONATOR, getStage());
                }else if(GameState.level == 38){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_POWER, getStage());

                } else if(GameState.level == 39){
                    Master.item = new Item(getX(), getY(), ItemType.WALLPASS, getStage());

                } else if(GameState.level == 40){
                    Master.item = new Item(getX(), getY(), ItemType.MYSTERY, getStage());

                } else if(GameState.level == 41){
                    Master.item = new Item(getX(), getY(), ItemType.DETONATOR, getStage());

                } else if(GameState.level == 42){
                    Master.item = new Item(getX(), getY(), ItemType.WALLPASS, getStage());

                } else if(GameState.level == 43){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_PASS, getStage());

                } else if(GameState.level == 44){
                    Master.item = new Item(getX(), getY(), ItemType.DETONATOR, getStage());
                }else if(GameState.level == 45){
                    Master.item = new Item(getX(), getY(), ItemType.MYSTERY, getStage());
                }else if(GameState.level == 46){
                    Master.item = new Item(getX(), getY(), ItemType.WALLPASS, getStage());
                }else if(GameState.level == 47){
                    Master.item = new Item(getX(), getY(), ItemType.BOMB_PASS, getStage());
                }else if(GameState.level == 48){
                    Master.item = new Item(getX(), getY(), ItemType.DETONATOR, getStage());
                }else if(GameState.level == 49){
                    Master.item = new Item(getX(), getY(), ItemType.FLAME_PASS, getStage());
                }else if(GameState.level == 50){
                    Master.item = new Item(getX(), getY(), ItemType.MYSTERY, getStage());
                }


            } else if(hasDoor){
                Master.door = new Door(getX(), getY(), getStage());
            }
            remove();
            Master.walls.removeValue(this, true);
            Master.briches.removeValue(this, true);
        }
    }
}
