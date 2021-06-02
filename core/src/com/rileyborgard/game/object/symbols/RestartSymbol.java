package com.rileyborgard.game.object.symbols;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.GameManager;

/**
 * Created by Riley on 7/21/2017.
 */

public class RestartSymbol extends Symbol {

    private Color background;

    public RestartSymbol(Color background) {
        this.background = background;
    }

    public void draw(GameManager gm, Rectangle rect) {
        gm.sr.begin(ShapeRenderer.ShapeType.Filled);
        gm.sr.setColor(Color.WHITE);
        gm.sr.arc(rect.x + rect.width / 2, rect.y + rect.height / 2, rect.width * 0.45f, 45, 270);
        gm.sr.setColor(background);
        gm.sr.circle(rect.x + rect.width / 2, rect.y + rect.height / 2, rect.width * 0.25f);

        gm.sr.setColor(Color.WHITE);
        gm.sr.triangle(rect.x + rect.width * 0.5f, rect.y + rect.height / 2, rect.x + rect.width, rect.y + rect.height / 2, rect.x + rect.width, rect.y + rect.height);

        gm.sr.end();
    }

}
