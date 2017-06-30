package com.rileyborgard.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter {

	private GameManager gm;
	
	@Override
	public void create() {
		Constants.config();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		gm = new GameManager();
	}

	@Override
	public void render() {
		gm.update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gm.render();
	}
	
	@Override
	public void dispose() {
		gm.dispose();
	}

}
