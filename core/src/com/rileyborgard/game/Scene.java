package com.rileyborgard.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Riley on 6/23/2017.
 */

public abstract class Scene implements InputProcessor {

    protected GameManager gm;

    public Scene(GameManager gm) {
        this.gm = gm;
    }

    public void init() {
        Gdx.input.setInputProcessor(this);
    }

    public abstract void render();
    public abstract void update(float dt);

}
