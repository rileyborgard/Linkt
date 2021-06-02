package com.rileyborgard.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.rileyborgard.game.game.Tile;

/**
 * Created by Riley on 6/23/2017.
 */

public class Constants {

    public static int WIDTH, HEIGHT;
    public static int ARROW_BUTTON_SIZE;
    public static int ARROW_BUTTON_MARGIN;
    public static int TILE_SIZE;
    public static int OUTLINE_SIZE;
    public static int LEVEL_BUTTON_MARGIN;
    public static int KNIFE_WIDTH;
    public static int FIXED_SIZE;

    public static final String TITLE = "Linkt";

    public static final int LEVELS_PER_ROW = 3, LEVELS_PER_COLUMN = 5;
    public static final int PACKS = 4;
    public static final int LEVELS = 15;
    public static final int TILE_TYPES = 3;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;

    //the speed of transition in blocks per second
    public static final float TRANSITION_SPEED = 5f;

    public static final float BUTTON_TRANSPARENCY = 0.5f;

    //color scheme
    //colors of tile types are set in the config function.
    //blocks and boxes are not considered types, because their position can change
    public static Color color[] = new Color[TILE_TYPES];
    public static Color outlineColor[] = new Color[TILE_TYPES];

    public static Color PLAYER_COLOR = new Color(0, 0.5f, 0, 1f);
    public static Color BLOCK_COLOR = new Color(1f, 0, 0, 1f);
    public static Color CONTROL_COLOR = new Color(0, 1f, 0, 1f);
    public static Color BOX_COLOR = new Color(0.546875f, 0.2734375f, 0.078125f, 1f);
    public static Color ICE_CUBE_COLOR = new Color(0f, 0f, 1f, 1f);
    public static Color KNIFE_COLOR = new Color(0.5f, 0.5f, 0.5f, 1f);

    public static Color PLAYER_COLOR_OUTLINE = new Color(0, 0.25f, 0, 1f);
    public static Color BLOCK_COLOR_OUTLINE = new Color(0.5f, 0, 0, 1f);
    public static Color CONTROL_COLOR_OUTLINE = new Color(0, 0.5f, 0, 1);
    public static Color BOX_COLOR_OUTLINE = new Color(0.2734375f, 0.13671875f, 0.0390625f, 1f);
    public static Color ICE_CUBE_COLOR_OUTLINE = new Color(0f, 0f, 0.5f, 0.75f);

    public static Color BACKGROUND_COLOR = new Color(0.25f, 0.25f, 0.25f, 1f);
    public static Color LEVEL_BUTTON_COLOR = new Color(0, 0.625f, 0.875f, 1f);
    public static Color PACK_BUTTON_COLOR = new Color(0.875f, 0.625f, 0, 1f);

    public static void config() {
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
        ARROW_BUTTON_SIZE = (int) Math.min(WIDTH / 5, (HEIGHT - WIDTH) / 3);
        ARROW_BUTTON_MARGIN = (int) (0.1 * ARROW_BUTTON_SIZE);
        TILE_SIZE = WIDTH / 10;
        OUTLINE_SIZE = TILE_SIZE / 10;
        LEVEL_BUTTON_MARGIN = WIDTH / 30;
        KNIFE_WIDTH = TILE_SIZE / 5;
        FIXED_SIZE = TILE_SIZE / 5;

        color[Tile.WALL] = new Color(0.25f, 0.25f, 0.25f, 1f);
        color[Tile.EMPTY] = new Color(1f, 1f, 1f, 1f);
        color[Tile.ICE] = new Color(0xa5f2f3ff);

        outlineColor[Tile.WALL] = new Color(0.125f, 0.125f, 0.125f, 1f);
        outlineColor[Tile.EMPTY] = color[Tile.EMPTY];
        outlineColor[Tile.ICE] = color[Tile.ICE];
    }

}
