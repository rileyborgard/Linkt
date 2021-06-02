package com.rileyborgard.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.Constants;
import com.rileyborgard.game.GameManager;
import com.rileyborgard.game.Scene;
import com.rileyborgard.game.object.Button;
import com.rileyborgard.game.object.ButtonAction;
import com.rileyborgard.game.object.ImageButton;
import com.rileyborgard.game.object.SymbolButton;
import com.rileyborgard.game.object.symbols.ArrowSymbol;

import java.util.ArrayList;

/**
 * Created by Riley on 6/23/2017.
 */

public class MenuScene extends Scene implements InputProcessor {

    private ArrayList<Button> buttons;
    private GlyphLayout titleLayout;

    public MenuScene(final GameManager gm) {
        super(gm);
        float w = Constants.WIDTH;
        float h = Constants.HEIGHT;
        buttons = new ArrayList<Button>();
        SymbolButton playButton = new SymbolButton(new ArrowSymbol(Constants.RIGHT, ArrowSymbol.TYPE_TRIANGLE, Color.GREEN),
                new Rectangle(w / 2 - h / 16, h / 2 - h / 16 - 2 * gm.font.getLineHeight(), h / 8, h / 8), new ButtonAction() {
                    public void run() {
                        gm.setScene(gm.levelMenuScene);
                    }
                });
        buttons.add(playButton);

        titleLayout = new GlyphLayout(gm.font, "LINKT");
    }

    @Override
    public void init() {
        super.init();
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render() {
        int w = Constants.WIDTH;
        int h = Constants.HEIGHT;

        gm.sb.begin();
        gm.sb.setColor(Color.WHITE);
        gm.font.draw(gm.sb, titleLayout, w / 2 - titleLayout.width / 2, h / 2 + titleLayout.height / 2 + 2 * gm.font.getLineHeight());
        gm.sb.end();

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
