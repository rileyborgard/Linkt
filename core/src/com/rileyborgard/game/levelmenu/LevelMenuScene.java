package com.rileyborgard.game.levelmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.Constants;
import com.rileyborgard.game.GameManager;
import com.rileyborgard.game.Scene;
import com.rileyborgard.game.object.Button;
import com.rileyborgard.game.object.ButtonAction;
import com.rileyborgard.game.object.ImageButton;

import java.util.ArrayList;

/**
 * Created by Riley on 6/23/2017.
 */

public class LevelMenuScene extends Scene {

    ArrayList<Button> buttons;

    public LevelMenuScene(final GameManager gm) {
        super(gm);
        int w = Constants.WIDTH;
        int h = Constants.HEIGHT;
        int r = Constants.LEVELS_PER_ROW;
        int c = Constants.LEVELS_PER_COLUMN;
        int m = Constants.LEVEL_BUTTON_MARGIN;
        buttons = new ArrayList<Button>();
        for(int i = 0; i < Constants.LEVELS; i++) {
            final int idx = i;
            buttons.add(new LevelButton(gm.font, idx + 1,
                    new Rectangle((idx % r) * (w / r) + m, h - (idx / r + 1) * (h / c) + m, w / r - 2 * m, h / c - 2 * m),
                    new ButtonAction() {
                        public void run() {
                            gm.setScene(gm.gameScene);
                            gm.gameScene.load(idx + 1);
                        }
                    }));
        }
    }

    @Override
    public void init() {
        super.init();
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render() {
        for(Button button : buttons) {
            button.draw(gm);
        }
    }

    @Override
    public void update(float dt) {

    }

    //input methods
    @Override
    public boolean keyDown(int keycode) {
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
        for(Button button : buttons) {
            if(button.click(screenX, screenY)) {
                break;
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
