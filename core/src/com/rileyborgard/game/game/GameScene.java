package com.rileyborgard.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.Constants;
import com.rileyborgard.game.GameManager;
import com.rileyborgard.game.Scene;
import com.rileyborgard.game.object.Button;
import com.rileyborgard.game.object.ButtonAction;
import com.rileyborgard.game.object.ImageButton;

import java.util.ArrayList;

/**
 * Created by Riley on 6/24/2017.
 */

public class GameScene extends Scene {

    private Board board;
    private ArrayList<Button> buttons;
    private boolean isDragging = false;
    private boolean started = false;
    private boolean moving[] = new boolean[4];
    private int tapX, tapY;
    private int level = 1;

    public GameScene(final GameManager gm) {
        super(gm);
        int w = Constants.WIDTH;
        int h = Constants.HEIGHT;
        int a = Constants.ARROW_BUTTON_SIZE;
        buttons = new ArrayList<Button>();
        ImageButton leftButton = new ImageButton(gm.assets.get("leftbutton.png", Texture.class),
                new Rectangle(0, a, a, a), new ButtonAction() {
            public void run() {
                moving[Constants.LEFT] = true;
            }
        });
        leftButton.setTint(new Color(1f, 1f, 1f, Constants.BUTTON_TRANSPARENCY));

        ImageButton rightButton = new ImageButton(gm.assets.get("rightbutton.png", Texture.class),
                new Rectangle(2 * a, a, a, a), new ButtonAction() {
            public void run() {
                moving[Constants.RIGHT] = true;
            }
        });
        rightButton.setTint(new Color(1f, 1f, 1f, Constants.BUTTON_TRANSPARENCY));

        ImageButton upButton = new ImageButton(gm.assets.get("upbutton.png", Texture.class),
                new Rectangle(a, 2 * a, a, a), new ButtonAction() {
            public void run() {
                moving[Constants.UP] = true;
            }
        });
        upButton.setTint(new Color(1f, 1f, 1f, Constants.BUTTON_TRANSPARENCY));

        ImageButton downButton = new ImageButton(gm.assets.get("downbutton.png", Texture.class),
                new Rectangle(a, 0, a, a), new ButtonAction() {
            public void run() {
                moving[Constants.DOWN] = true;
            }
        });
        downButton.setTint(new Color(1f, 1f, 1f, Constants.BUTTON_TRANSPARENCY));

        ImageButton backButton = new ImageButton(gm.assets.get("backbutton.png", Texture.class),
                new Rectangle(0, h - a, a, a), new ButtonAction() {
            public void run() {
                gm.setScene(gm.menuScene);
            }
        });
        backButton.setTint(new Color(0f, 0f, 1f, 1f));

        ImageButton restartButton = new ImageButton(gm.assets.get("restartbutton.png", Texture.class),
                new Rectangle(a, h - a, a, a), new ButtonAction() {
            public void run() {
                restart();
            }
        });
        restartButton.setTint(new Color(0f, 0f, 1f, 1f));

        buttons.add(leftButton);
        buttons.add(rightButton);
        buttons.add(upButton);
        buttons.add(downButton);
        buttons.add(backButton);
        buttons.add(restartButton);
    }

    @Override
    public void init() {
        super.init();
        started = false;
        isDragging = false;
        Color color = Constants.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
    }

    public void load(int level) {
        this.level = level;
        restart();
    }

    private void restart() {
        //load / reload the level
        TiledMap map = gm.assets.get("level" + level + ".tmx", TiledMap.class);
        TiledMapTileLayer layer1 = (TiledMapTileLayer) map.getLayers().get("Layer1"); //walls
        TiledMapTileLayer layer2 = (TiledMapTileLayer) map.getLayers().get("Layer2"); //blocks and boxes
        TiledMapTileLayer layer3 = (TiledMapTileLayer) map.getLayers().get("Layer3"); //knives

        board = new Board(layer1.getWidth(), layer1.getHeight());
        for(int i = 0; i < layer1.getWidth(); i++) {
            for(int j = 0; j < layer1.getHeight(); j++) {
                int type = Integer.parseInt((String) layer1.getCell(i, j).getTile().getProperties().get("type"));
                Tile t = new Tile(type, i, j);
                TiledMapTileLayer.Cell cell = layer2.getCell(i, j);
                if(cell != null) {
                    t.setBlock(cell.getTile().getProperties().get("block").equals("1"));
                    t.setBox(cell.getTile().getProperties().get("box").equals("1"));
                    t.setControllable(cell.getTile().getProperties().get("controllable").equals("1"));
                    t.setControlled(cell.getTile().getProperties().get("controlled").equals("1"));
                    t.setFixed(cell.getTile().getProperties().get("fixed").equals("1"));
                    t.setSlippery(cell.getTile().getProperties().get("slippery").equals("1"));
                }
                if(layer3 != null) {
                    cell = layer3.getCell(i, j);
                    if(cell != null) {
                        t.setKnifeHorz(cell.getTile().getProperties().get("horz").equals("1"));
                        t.setKnifeVert(cell.getTile().getProperties().get("vert").equals("1"));
                    }
                }
                board.setTile(i, j, t);
            }
        }
        board.setPosition(Constants.WIDTH / 2 - layer1.getWidth() * Constants.TILE_SIZE / 2,
                Constants.HEIGHT / 2 - layer1.getHeight() * Constants.TILE_SIZE / 2);
        started = true;
    }

    @Override
    public void render() {
        if(started) {
            board.draw(gm);
            for(Button button : buttons) {
                button.draw(gm);
            }
        }
    }

    @Override
    public void update(float dt) {
        if(started) {
            if(!board.isPlayerInTransition()) {
                if(moving[Constants.LEFT])
                    board.transitionPlayers(-1, 0);
                else if(moving[Constants.RIGHT])
                    board.transitionPlayers(1, 0);
                else if(moving[Constants.UP])
                    board.transitionPlayers(0, 1);
                else if(moving[Constants.DOWN])
                    board.transitionPlayers(0, -1);
                //keyboard input, for testing on desktop
                else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                    board.transitionPlayers(-1, 0);
                else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||Gdx.input.isKeyPressed(Input.Keys.D))
                    board.transitionPlayers(1, 0);
                else if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
                    board.transitionPlayers(0, 1);
                else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                    board.transitionPlayers(0, -1);
            }
            board.update(dt);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ENTER && board.isSolved()) {
            if (level == Constants.LEVELS) {
                gm.setScene(gm.menuScene);
            } else {
                init();
                load(level + 1);
            }
        }else if(keycode == Input.Keys.BACKSPACE) {
            gm.setScene(gm.menuScene);
        }else if(keycode == Input.Keys.R) {
            restart();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int btn) {
        //limit the number of simultaneous taps are being registered
        if(pointer < 1) {
            boolean b = false;
            screenY = Constants.HEIGHT - screenY;
            tapX = screenX;
            tapY = screenY;
            for (Button button : buttons) {
                if (button.click(tapX, tapY)) {
                    b = true;
                    break;
                }
            }
            if (!b) {
                if (board.isSolved()) {
                    if (level == Constants.LEVELS) {
                        gm.setScene(gm.menuScene);
                    } else {
                        init();
                        load(level + 1);
                    }
                } else if (board.isPlayerAt(board.getIndexX(tapX), board.getIndexY(tapY)) && !board.isPlayerInTransition()) {
                    board.control(board.getIndexX(tapX), board.getIndexY(tapY));
                } else {
                    isDragging = true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 1) {
            isDragging = false;
            moving[Constants.LEFT] = false;
            moving[Constants.RIGHT] = false;
            moving[Constants.UP] = false;
            moving[Constants.DOWN] = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer < 1) {
            screenY = Constants.HEIGHT - screenY;
            if (isDragging) {
                board.move(screenX - tapX, screenY - tapY);
                tapX = screenX;
                tapY = screenY;
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
