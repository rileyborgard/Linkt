package com.rileyborgard.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class MenuScene extends Scene implements InputProcessor {

    private ArrayList<Button> buttons;

    public MenuScene(final GameManager gm) {
        super(gm);
        float w = Constants.WIDTH;
        float h = Constants.HEIGHT;
        buttons = new ArrayList<Button>();
        buttons.add(new ImageButton(gm.assets.get("playbutton.png", Texture.class),
                new Rectangle(w / 2 - h / 8, h * 3 / 8, h / 4, h / 4),
                new ButtonAction() {
                    public void run() {
                        gm.setScene(gm.levelMenuScene);
                    }
                }));
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
//        if(btn == Input.Buttons.LEFT) {
            for(Button button : buttons) {
                if(button.click(screenX, screenY)) {
                    break;
                }
            }
//        }
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
