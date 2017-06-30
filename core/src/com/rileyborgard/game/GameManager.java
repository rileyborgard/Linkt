package com.rileyborgard.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.rileyborgard.game.game.GameScene;
import com.rileyborgard.game.levelmenu.LevelMenuScene;
import com.rileyborgard.game.menu.MenuScene;

/**
 * Created by Riley on 6/23/2017.
 */

public class GameManager {

    private Scene scene;

    public MenuScene menuScene;
    public LevelMenuScene levelMenuScene;
    public GameScene gameScene;

    public AssetManager assets;

    public SpriteBatch sb;
    public ShapeRenderer sr;
    public BitmapFont font;

    //sprites
    public Texture sprites;
    public TextureRegion red, green[], wall[], fixedRed, fixedGreen, floor, ice, box, iceCube;

    public GameManager() {
        //initiate variables
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1);
        font.setColor(Color.BLACK);

        //load assets
        assets = new AssetManager();
        assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        assets.load("playbutton.png", Texture.class);
        assets.load("levelbutton1.png", Texture.class);
        assets.load("leftbutton.png", Texture.class);
        assets.load("rightbutton.png", Texture.class);
        assets.load("upbutton.png", Texture.class);
        assets.load("downbutton.png", Texture.class);
        assets.load("backbutton.png", Texture.class);
        assets.load("restartbutton.png", Texture.class);
        assets.load("sprites.png", Texture.class);

        for(int i = 1; i <= Constants.LEVELS; i++) {
            assets.load("level" + i + ".tmx", TiledMap.class);
        }

        assets.finishLoading();
        Texture.setAssetManager(assets);

        //sprites
        sprites = assets.get("sprites.png", Texture.class);
        red = new TextureRegion(sprites, 0, 0, 32, 32);
        green = new TextureRegion[16];
        wall = new TextureRegion[16];
        for(int i = 0; i < 16; i++) {
            green[i] = new TextureRegion(sprites, 64 + 32 * (i % 4), 32 * (i / 4), 32, 32);
            wall[i] = new TextureRegion(sprites, 192 + 32 * (i % 4), 32 * (i / 4), 32, 32);
        }
        fixedRed = new TextureRegion(sprites, 0, 64, 32, 32);
        fixedGreen = new TextureRegion(sprites, 32, 64, 32, 32);
        floor = new TextureRegion(sprites, 0, 96, 32, 32);
        box = new TextureRegion(sprites, 32, 96, 32, 32);
        ice = new TextureRegion(sprites, 0, 128, 32, 32);
        iceCube = new TextureRegion(sprites, 32, 128, 32, 32);

        //initialize scenes
        menuScene = new MenuScene(this);
        levelMenuScene = new LevelMenuScene(this);
        gameScene = new GameScene(this);

        setScene(menuScene);
    }

    public void render() {
        if(assets.update()) {
            //done loading
            scene.render();
        }else {
            //TODO load screen
        }
    }
    public void update(float dt) {
        scene.update(dt);
    }

    public void dispose() {
        sb.dispose();
        sr.dispose();
        font.dispose();
        assets.dispose();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        scene.init();
    }

}
