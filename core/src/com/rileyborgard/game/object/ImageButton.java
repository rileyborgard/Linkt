package com.rileyborgard.game.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.GameManager;

import java.awt.Canvas;

/**
 * Created by Riley on 5/22/2017.
 */

public class ImageButton extends Button {

    private Texture tex;
    private Color tint = new Color(1f, 1f, 1f, 1f);

    public ImageButton(Texture tex, Rectangle rect, ButtonAction action) {
        super(rect, action);
        this.tex = tex;
    }

    public void draw(GameManager gm) {
        gm.sb.begin();
        gm.sb.setColor(tint);
        gm.sb.draw(tex, bounds.x, bounds.y, bounds.width, bounds.height);
        gm.sb.end();
    }

    public void setTint(Color tint) {
        this.tint = tint;
    }

}
