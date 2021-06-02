package com.rileyborgard.game.levelmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.Constants;
import com.rileyborgard.game.GameManager;
import com.rileyborgard.game.Scene;
import com.rileyborgard.game.object.Button;
import com.rileyborgard.game.object.ButtonAction;

import java.util.ArrayList;

/**
 * Created by Riley on 6/23/2017.
 */

public class LevelMenuScene extends Scene {

    ArrayList<LevelButton> levelButtons;
    ArrayList<LevelButton> packButtons;
    //0 if displaying all packs. Otherwise, it shows levels within the pack-th pack
    private int pack;

    public LevelMenuScene(final GameManager gm) {
        super(gm);
        int w = Constants.WIDTH;
        int h = Constants.HEIGHT;
        int r = Constants.LEVELS_PER_ROW;
        int c = Constants.LEVELS_PER_COLUMN;
        int m = Constants.LEVEL_BUTTON_MARGIN;

        levelButtons = new ArrayList<LevelButton>();
        for (int i = 0; i < Constants.LEVELS; i++) {
            final int idx = i;
            levelButtons.add(new LevelButton(gm.font, idx + 1,
                    new Rectangle((idx % r) * (w / r) + m, h - (idx / r + 1) * (h / c) + m, w / r - 2 * m, h / c - 2 * m),
                    new ButtonAction() {
                        public void run() {
                            gm.setScene(gm.gameScene);
                            gm.gameScene.load(pack, idx + 1);
                        }
                    }));
        }
        packButtons = new ArrayList<LevelButton>();
        for(int i = 0; i < Constants.PACKS; i++) {
            final int idx = i;
            packButtons.add(new LevelButton(gm.font, idx + 1,
                    new Rectangle((idx % r) * (w / r) + m, h - (idx / r + 1) * (h / c) + m, w / r - 2 * m, h / c - 2 * m),
                    new ButtonAction() {
                        public void run() {
                            setPack(idx + 1);
                        }
                    }));
        }
        for(LevelButton btn : packButtons) {
            btn.setPack(0);
        }
    }

    @Override
    public void init() {
        super.init();
        setPack(0);
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render() {
        if(pack > 0) {
            for (Button button : levelButtons) {
                button.draw(gm);
            }
        }else {
            for(Button button : packButtons) {
                button.draw(gm);
            }
        }
    }

    @Override
    public void update(float dt) {

    }

    public void setPack(int pack) {
        this.pack = pack;
        for(LevelButton btn : levelButtons) {
            btn.setPack(pack);
        }
    }

    //input methods
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK) {
            if(pack == 0) {
                gm.setScene(gm.menuScene);
            }else {
                setPack(0);
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int btn) {
        screenY = Constants.HEIGHT - screenY;
        if(pack > 0) {
            for (Button button : levelButtons) {
                if (button.click(screenX, screenY)) {
                    break;
                }
            }
        }else {
            for (LevelButton button : packButtons) {
                if(button.isUnlocked(gm) && button.click(screenX, screenY)) {
                    break;
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
