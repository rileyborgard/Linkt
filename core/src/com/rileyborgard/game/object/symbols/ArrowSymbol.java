package com.rileyborgard.game.object.symbols;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.Constants;
import com.rileyborgard.game.GameManager;

/**
 * Created by Riley on 7/21/2017.
 */

public class ArrowSymbol extends Symbol {

    public static final int TYPE_TRIANGLE = 0;
    public static final int TYPE_ARROW = 1;

    private int dir;
    private int type;
    private Color color;

    public ArrowSymbol(int dir, int type, Color color) {
        this.dir = dir;
        this.type = type;
        this.color = color;
    }

    public void draw(GameManager gm, Rectangle rect) {
        gm.sr.begin(ShapeRenderer.ShapeType.Filled);
        gm.sr.setColor(color);
        switch(type) {
        case TYPE_TRIANGLE:
            switch (dir) {
            case Constants.LEFT:
                gm.sr.triangle(rect.x, rect.y + rect.height / 2, rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height);
                break;
            case Constants.RIGHT:
                gm.sr.triangle(rect.x + rect.width, rect.y + rect.height / 2, rect.x, rect.y, rect.x, rect.y + rect.height);
                break;
            case Constants.UP:
                gm.sr.triangle(rect.x, rect.y, rect.x + rect.width, rect.y, rect.x + rect.width / 2, rect.y + rect.height);
                break;
            case Constants.DOWN:
                gm.sr.triangle(rect.x, rect.y + rect.height, rect.x + rect.width, rect.y + rect.height, rect.x + rect.width / 2, rect.y);
                break;
            }
            break;
        case TYPE_ARROW:
            switch (dir) {
            case Constants.LEFT:
                gm.sr.triangle(rect.x, rect.y + rect.height / 2, rect.x + rect.width / 2, rect.y, rect.x + rect.width / 2, rect.y + rect.height);
                gm.sr.rect(rect.x + rect.width / 2, rect.y + rect.height * 0.3f, rect.width / 2, rect.height * 0.4f);
                break;
            case Constants.RIGHT:
                gm.sr.triangle(rect.x + rect.width, rect.y + rect.height / 2, rect.x + rect.width / 2, rect.y, rect.x + rect.width / 2, rect.y + rect.height);
                gm.sr.rect(rect.x, rect.y + rect.height * 0.3f, rect.width / 2, rect.height * 0.4f);
                break;
            case Constants.UP:
                gm.sr.triangle(rect.x, rect.y, rect.x + rect.width, rect.y, rect.x + rect.width / 2, rect.y + rect.height);
                break;
            case Constants.DOWN:
                gm.sr.triangle(rect.x, rect.y + rect.height, rect.x + rect.width, rect.y + rect.height, rect.x + rect.width / 2, rect.y);
                break;
            }
            break;
        }
        gm.sr.end();
    }

}
