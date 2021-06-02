package com.rileyborgard.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.rileyborgard.game.Constants;
import com.rileyborgard.game.GameManager;
import com.rileyborgard.game.Scene;
import com.rileyborgard.game.object.Button;
import com.rileyborgard.game.object.ButtonAction;
import com.rileyborgard.game.object.SymbolButton;
import com.rileyborgard.game.object.symbols.ArrowSymbol;
import com.rileyborgard.game.object.symbols.RestartSymbol;

import java.util.ArrayList;

/**
 * Created by Riley on 6/24/2017.
 */

public class GameScene extends Scene {

    private Board board;
    private ArrayList<Button> buttons;
    private ArrayList<Button> solvedButtons;
    private GlyphLayout solvedLayout;
    private GlyphLayout tutorialLayout;
    private GlyphLayout levelLayout;

    private boolean tutorial = false;
    private String tutorialText = "";
    private boolean started = false;
    private boolean moving[] = new boolean[4];
    private int tapX, tapY;
    private int pack = 1, level = 1;

    public GameScene(final GameManager gm) {
        super(gm);
        int w = Constants.WIDTH;
        int h = Constants.HEIGHT;
        int a = Constants.ARROW_BUTTON_SIZE;
        int m = Constants.ARROW_BUTTON_MARGIN;
        buttons = new ArrayList<Button>();
        solvedButtons = new ArrayList<Button>();

        SymbolButton leftButton = new SymbolButton(new ArrowSymbol(Constants.LEFT, ArrowSymbol.TYPE_TRIANGLE, new Color(0.7f, 0.7f, 0.7f, 1f)),
                new Rectangle(m, a + m, a - 2 * m, a - 2 * m), new ButtonAction() {
            public void run() {
                moving[Constants.LEFT] = true;
            }
        });

        SymbolButton rightButton = new SymbolButton(new ArrowSymbol(Constants.RIGHT, ArrowSymbol.TYPE_TRIANGLE, new Color(0.7f, 0.7f, 0.7f, 1f)),
                new Rectangle(2 * a + m, a + m, a - 2 * m, a - 2 * m), new ButtonAction() {
            public void run() {
                moving[Constants.RIGHT] = true;
            }
        });

        SymbolButton upButton = new SymbolButton(new ArrowSymbol(Constants.UP, ArrowSymbol.TYPE_TRIANGLE, new Color(0.7f, 0.7f, 0.7f, 1f)),
                new Rectangle(a + m, 2 * a + m, a - 2 * m, a - 2 * m), new ButtonAction() {
            public void run() {
                moving[Constants.UP] = true;
            }
        });

        SymbolButton downButton = new SymbolButton(new ArrowSymbol(Constants.DOWN, ArrowSymbol.TYPE_TRIANGLE, new Color(0.7f, 0.7f, 0.7f, 1f)),
                new Rectangle(a + m, m, a - 2 * m, a - 2 * m), new ButtonAction() {
            public void run() {
                moving[Constants.DOWN] = true;
            }
        });

//        SymbolButton backButton = new SymbolButton(new ArrowSymbol(Constants.LEFT, ArrowSymbol.TYPE_ARROW, Color.WHITE),
//                new Rectangle(w - a * 3 / 2 + m, a * 3 / 2 + m, a * 3 / 2 - 2 * m, a * 3 / 2 - 2 * m), new ButtonAction() {
//            public void run() {
//                gm.setScene(gm.menuScene);
//            }
//        });

        SymbolButton restartButton = new SymbolButton(new RestartSymbol(Constants.BACKGROUND_COLOR),
                new Rectangle(w - a + m, m, a - 2 * m, a - 2 * m), new ButtonAction() {
            public void run() {
                restart();
            }
        });

        buttons.add(leftButton);
        buttons.add(rightButton);
        buttons.add(upButton);
        buttons.add(downButton);
//        buttons.add(backButton);
        buttons.add(restartButton);

//        SymbolButton solvedBackButton = new SymbolButton(new ArrowSymbol(Constants.LEFT, ArrowSymbol.TYPE_ARROW, Color.WHITE),
//                new Rectangle(w / 4 + m, h / 4 + m, a - 2 * m, a - 2 * m), new ButtonAction() {
//            public void run() {
//                gm.setScene(gm.menuScene);
//            }
//        });
//        SymbolButton solvedRestartButton = new SymbolButton(new RestartSymbol(Color.ORANGE),
//                new Rectangle(w / 2 - a / 2 + m, h / 4 + m, a - 2 * m, a - 2 * m), new ButtonAction() {
//            public void run() {
//                restart();
//            }
//        });
        SymbolButton solvedNextButton = new SymbolButton(new ArrowSymbol(Constants.RIGHT, ArrowSymbol.TYPE_ARROW, Color.WHITE),
                new Rectangle(w / 2 - a * 3 / 4 + m, h / 4 + m, a * 3 / 2 - 2 * m, a * 3 / 2 - 2 * m), new ButtonAction() {
            public void run() {
                if(level == Constants.LEVELS) {
                    gm.setScene(gm.menuScene);
                }else {
                    load(pack, level + 1);
                }
            }
        });

//        solvedButtons.add(solvedBackButton);
//        solvedButtons.add(solvedRestartButton);
        solvedButtons.add(solvedNextButton);

        solvedLayout = new GlyphLayout(gm.font, "SOLVED");
    }

    @Override
    public void init() {
        super.init();
        tutorial = false;
        started = false;
        Color color = Constants.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
    }

    public void load(int pack, int level) {
        this.pack = pack;
        this.level = level;
        restart();
    }

    private void restart() {
        //load / reload the level
        TiledMap map = gm.assets.get("pack" + pack + "/level" + level + ".tmx", TiledMap.class);
        TiledMapTileLayer layer1 = (TiledMapTileLayer) map.getLayers().get("Layer1"); //walls
        TiledMapTileLayer layer2 = (TiledMapTileLayer) map.getLayers().get("Layer2"); //blocks and boxes
        TiledMapTileLayer layer3 = (TiledMapTileLayer) map.getLayers().get("Layer3"); //knives
        MapLayer objLayer = map.getLayers().get("Tutorial");

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
                Constants.HEIGHT - Constants.TILE_SIZE * layer1.getHeight() + Constants.TILE_SIZE / 2);

        if(objLayer != null) {
            MapObject obj = objLayer.getObjects().get(0);
            tutorial = true;
            tutorialText = (String) obj.getProperties().get("text");
            tutorialLayout = new GlyphLayout(gm.smallFont, tutorialText);
        }

        levelLayout = new GlyphLayout(gm.font, pack + "." + level);

        started = true;
    }

    @Override
    public void render() {
        int w = Constants.WIDTH;
        int h = Constants.HEIGHT;
        int a = Constants.ARROW_BUTTON_SIZE;
        int m = Constants.ARROW_BUTTON_MARGIN;

        if(started) {
            board.draw(gm);
            for(Button button : buttons) {
                button.draw(gm);
            }

            gm.sb.begin();
            gm.sb.setColor(Color.WHITE);
            gm.font.draw(gm.sb, levelLayout, w - levelLayout.width - m,
                    2 * a + levelLayout.height / 2);
            gm.sb.end();

            if(board.isSolved()) {
                if(!gm.isSolved(pack, level)) {
                    gm.solve(pack, level);
                }
                gm.sr.begin(ShapeRenderer.ShapeType.Filled);
                gm.sr.setColor(Color.ORANGE);
                gm.sr.rect(w / 8, h / 4, w * 3 / 4, h / 2);
                gm.sr.end();

                gm.sb.begin();
                gm.sb.setColor(Color.WHITE);
                gm.font.draw(gm.sb, solvedLayout, w / 2 - solvedLayout.width / 2,
                        h / 2 + a * 3 / 4 + solvedLayout.height / 2);
                gm.sb.end();

                for(Button button : solvedButtons) {
                    button.draw(gm);
                }
            }
            if(tutorial) {
                gm.sr.begin(ShapeRenderer.ShapeType.Filled);
                gm.sr.setColor(Color.BLACK);
                gm.sr.rect(w / 8, h / 4, w * 3 / 4, h / 2);
                gm.sr.end();

                gm.sb.begin();
                gm.sb.setColor(Color.WHITE);
                gm.smallFont.draw(gm.sb, tutorialLayout, w / 2 - tutorialLayout.width / 2, h / 2 + tutorialLayout.height / 2);
                gm.sb.end();
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
                load(pack, level + 1);
            }
        }else if(keycode == Input.Keys.BACKSPACE) {
            gm.setScene(gm.menuScene);
        }else if(keycode == Input.Keys.R) {
            restart();
        }else if(keycode == Input.Keys.BACK) {
            gm.setScene(gm.levelMenuScene);
            gm.levelMenuScene.setPack(pack);
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
            if(tutorial)
                tutorial = false;
            else {
                boolean b = false;
                screenY = Constants.HEIGHT - screenY;
                tapX = screenX;
                tapY = screenY;
                if (!board.isSolved()) {
                    for (Button button : buttons) {
                        if (button.click(tapX, tapY)) {
                            b = true;
                            break;
                        }
                    }
                }
                if (!b) {
                    if (board.isSolved()) {
                        for (Button button : solvedButtons) {
                            if (button.click(tapX, tapY)) {
                                break;
                            }
                        }
                    } else if (board.isPlayerAt(board.getIndexX(tapX), board.getIndexY(tapY)) && !board.isPlayerInTransition()) {
                        board.control(board.getIndexX(tapX), board.getIndexY(tapY));
                    } else {
//                    isDragging = true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 1) {
            moving[Constants.LEFT] = false;
            moving[Constants.RIGHT] = false;
            moving[Constants.UP] = false;
            moving[Constants.DOWN] = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
