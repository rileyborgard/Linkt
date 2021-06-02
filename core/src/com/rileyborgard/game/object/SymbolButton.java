package com.rileyborgard.game.object;

import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.GameManager;
import com.rileyborgard.game.object.symbols.Symbol;

/**
 * Created by Riley on 7/21/2017.
 */

public class SymbolButton extends Button {

    private Symbol symbol;

    public SymbolButton(Symbol symbol, Rectangle rect, ButtonAction action) {
        super(rect, action);
        this.symbol = symbol;
    }

    public void draw(GameManager gm) {
        symbol.draw(gm, bounds);
    }

}
