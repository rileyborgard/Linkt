package com.rileyborgard.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.rileyborgard.game.game.GameScene;
import com.rileyborgard.game.levelmenu.LevelMenuScene;
import com.rileyborgard.game.menu.MenuScene;

import sun.font.TrueTypeFont;

/**
 * Created by Riley on 6/23/2017.
 */

public class GameManager {

    private Scene scene;

    public MenuScene menuScene;
    public LevelMenuScene levelMenuScene;
    public GameScene gameScene;

    public AssetManager assets;
    public FreeTypeFontGenerator generator;

    public SpriteBatch sb;
    public ShapeRenderer sr;
    public BitmapFont font, smallFont;

    private boolean solved[] = new boolean[60];
    private boolean packUnlocked[] = new boolean[4];

    public GameManager() {
        //initiate variables
        sb = new SpriteBatch();
        sr = new ShapeRenderer();

        //load fonts
        generator = new FreeTypeFontGenerator(Gdx.files.internal("gfs-didot.regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = Math.min(Constants.WIDTH / 7, 128);
        font = generator.generateFont(param);
        param.size = Math.min(Constants.WIDTH / 20, 128);
        smallFont = generator.generateFont(param);

        //load assets
        assets = new AssetManager();
        assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        for(int i = 1; i <= Constants.PACKS; i++) {
            for (int j = 1; j <= Constants.LEVELS; j++) {
                assets.load("pack" + i + "/level" + j + ".tmx", TiledMap.class);
            }
        }

        assets.finishLoading();
        Texture.setAssetManager(assets);

        //load game data
        packUnlocked[0] = true;
        if(Gdx.files.isLocalStorageAvailable()) {
            FileHandle handle = Gdx.files.local("solved.txt");
            if(handle.exists()) {
                String str = handle.readString();
                if(str.length() >= 60) {
                    for(int i = 0; i < 60; i++) {
                        solved[i] = (str.charAt(i) == '1');
                    }
                    unlockPacks();
                }
            }
        }

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
        smallFont.dispose();
        generator.dispose();
        assets.dispose();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        scene.init();
    }

    public boolean isSolved(int pack, int level) {
        return solved[(pack - 1) * 15 + level - 1];
    }
    public boolean isPackUnlocked(int pack) {
        return packUnlocked[pack - 1];
    }

    public void solve(int pack, int level) {
        solved[(pack - 1) * 15 + level - 1] = true;
        String str = "";
        for(int i = 0; i < solved.length; i++) {
            str = str + (solved[i] ? '1' : '0');
        }
        if(Gdx.files.isLocalStorageAvailable()) {
            FileHandle handle = Gdx.files.local("solved.txt");
            handle.writeString(str, false);
        }
        unlockPacks();
    }
    public void unlockPacks() {
        for(int i = 0; i < packUnlocked.length; i++) {
            packUnlocked[i] = true;
        }
        for(int i = 0; i < solved.length; i++) {
            if(!solved[i]) {
                int n = i / 15 + 1;
                for(int j = n; j < packUnlocked.length; j++) {
                    packUnlocked[j] = false;
                }
                break;
            }
        }
    }

}
