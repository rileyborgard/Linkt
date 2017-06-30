package com.rileyborgard.game.levelmenu;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.GameManager;
import com.rileyborgard.game.object.Button;
import com.rileyborgard.game.object.ButtonAction;

/**
 * Created by Riley on 6/25/2017.
 */

public class LevelButton extends Button {

    private int level;
    private GlyphLayout layout;
    private BitmapFont font;

    public LevelButton(BitmapFont font, int level, Rectangle bounds, ButtonAction action) {
        super(bounds, action);
        this.level = level;
        this.font = font;
        layout = new GlyphLayout(font, "" + level);
    }

    @Override
    public void draw(GameManager gm) {
        gm.sr.begin(ShapeRenderer.ShapeType.Filled);
        gm.sr.setColor(0, 0.625f, 0.875f, 1f);
        gm.sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        gm.sr.end();

        gm.sb.begin();
        font.draw(gm.sb, layout, bounds.x + bounds.width / 2 - layout.width / 2,
                bounds.y + bounds.height / 2 + layout.height / 2);
        gm.sb.end();
    }

}
