package com.rileyborgard.game.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.GameManager;

/**
 * Created by Riley on 5/22/2017.
 */

public abstract class Button {

    protected Rectangle bounds;
    protected ButtonAction action;

    public Button(Rectangle bounds, ButtonAction action) {
        this.bounds = bounds;
        this.action = action;
    }

    public abstract void draw(GameManager gm);

    //checks if point is within button bounds. if so, it will run button action
    public boolean click(int x, int y) {
        if(x >= bounds.x && x <= bounds.x + bounds.width &&
                y >= bounds.y && y <= bounds.y + bounds.height) {
            action.run();
            return true;
        }
        return false;
    }

}
