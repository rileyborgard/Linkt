package com.rileyborgard.game.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rileyborgard.game.Constants;
import com.rileyborgard.game.GameManager;

/**
 * Created by Riley on 5/21/2017.
 */

public class Tile {

    public static final int WALL = 0, EMPTY = 1, ICE = 2;

    private int x, y;

    private boolean block = false; //is there a block on this tile, controllable or not
    private boolean box = false; //is there a pushable box on this tile
    private boolean controllable = false; //is the user allowed to select this block, assuming block is true
    private boolean controlled = false; //is the block currently selected, assuming block is true
    private boolean fixed = false; //is the block fixed to this tile, aka it cannot move
    private boolean slippery = false; //pretty self explanatory
    private boolean knifeHorz = false; //is there a horizontal knife on this tile
    private boolean knifeVert = false; //is there a vertical knife on this tile

    private int type;

    //true if a block or box on this tile is in transition
    private boolean inTransition;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = EMPTY;
    }
    public Tile(int type, int x, int y) {
        this(x, y);
        this.type = type;
    }
    public Tile(Tile tile) {
        this.x = tile.x;
        this.y = tile.y;
        this.type = tile.type;
        this.block = tile.block;
        this.box = tile.box;
        this.controllable = tile.controllable;
        this.controlled = tile.controlled;
        this.fixed = tile.fixed;
        this.slippery = tile.slippery;
        this.knifeHorz = tile.knifeHorz;
        this.knifeVert = tile.knifeVert;
        this.inTransition = tile.inTransition;
    }

    public void draw(GameManager gm, int boardX, int boardY, boolean solved, boolean drawOutline[]) {
        int drawX = boardX + Constants.TILE_SIZE * x;
        int drawY = boardY + Constants.TILE_SIZE * y;

//        gm.sb.begin();
//        gm.sb.setColor(Color.WHITE);
//        if(type == WALL)
//            gm.sb.draw(gm.wall[code], drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
//        else if(type == EMPTY)
//            gm.sb.draw(gm.floor, drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
//        else if(type == ICE)
//            gm.sb.draw(gm.ice, drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
//        gm.sb.end();

        gm.sr.begin(ShapeRenderer.ShapeType.Filled);

        gm.sr.setColor(Constants.color[type]);
        gm.sr.rect(drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);

        //draw outline
        gm.sr.setColor(Constants.outlineColor[type]);
        if(drawOutline[Constants.LEFT]) {
            gm.sr.rect(drawX, drawY, Constants.OUTLINE_SIZE, Constants.TILE_SIZE);
        }
        if(drawOutline[Constants.RIGHT]) {
            gm.sr.rect(drawX + Constants.TILE_SIZE - Constants.OUTLINE_SIZE, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }
        if(drawOutline[Constants.UP]) {
            gm.sr.rect(drawX, drawY, Constants.TILE_SIZE, Constants.OUTLINE_SIZE);
        }
        if(drawOutline[Constants.DOWN]) {
            gm.sr.rect(drawX, drawY + Constants.TILE_SIZE - Constants.OUTLINE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }

        gm.sr.end();
    }

    public void drawLayer2(GameManager gm, int boardX, int boardY, boolean solved, float transition, int dx, int dy, boolean drawOutline[]) {
        int drawX = boardX + Constants.TILE_SIZE * x;
        int drawY = boardY + Constants.TILE_SIZE * y;

        if (inTransition) {
            drawX += transition * dx * Constants.TILE_SIZE;
            drawY += transition * dy * Constants.TILE_SIZE;
        }
//        int code = (drawOutline[Constants.LEFT] ? 1 : 0) * 8 +
//                (drawOutline[Constants.RIGHT] ? 1 : 0) * 4 +
//                (drawOutline[Constants.DOWN] ? 1 : 0) * 2 +
//                (drawOutline[Constants.UP] ? 1 : 0);
//
//        gm.sb.begin();
//        if (block) {
//            if (controllable) {
//                gm.sb.draw(gm.green[code], drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
//                if (fixed) {
//                    gm.sb.draw(gm.fixedGreen, drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
//                }
//            } else {
//                gm.sb.draw(gm.red, drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
//                if (fixed) {
//                    gm.sb.draw(gm.fixedRed, drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
//                }
//            }
//        } else if (box) {
//            if (slippery) {
//                gm.sb.draw(gm.iceCube, drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
//            } else {
//                gm.sb.draw(gm.box, drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
//            }
//        }
//
//        gm.sb.end();

        gm.sr.begin(ShapeRenderer.ShapeType.Filled);

        boolean b = true;

        //draw block/box
        if(block && !controllable) {
            gm.sr.setColor(Constants.BLOCK_COLOR);
        }else if(block && !controlled) {
            gm.sr.setColor(Constants.PLAYER_COLOR);
        }else if(block && controlled) {
            gm.sr.setColor(Constants.CONTROL_COLOR);
        }else if(box && !slippery) {
            gm.sr.setColor(Constants.BOX_COLOR);
        }else if(box && slippery) {
            gm.sr.setColor(Constants.ICE_CUBE_COLOR);
        }else {
            b = false;
        }
        if(b) {
            gm.sr.rect(drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }

        b = true;
        //draw outline
        Color outlineColor = new Color(1f, 1f, 1f, 0f);
        if(block && !controllable) {
            outlineColor = Constants.BLOCK_COLOR_OUTLINE;
        }else if(block && !controlled) {
            outlineColor = Constants.PLAYER_COLOR_OUTLINE;
        }else if(block && controlled) {
            outlineColor = Constants.CONTROL_COLOR_OUTLINE;
        }else if(box && !slippery) {
            outlineColor = Constants.BOX_COLOR_OUTLINE;
        }else if(box && slippery) {
            outlineColor = Constants.ICE_CUBE_COLOR_OUTLINE;
        }else {
            b = false;
        }
        if(b) {
            gm.sr.setColor(outlineColor);

            if (drawOutline[Constants.LEFT]) {
                gm.sr.rect(drawX, drawY, Constants.OUTLINE_SIZE, Constants.TILE_SIZE);
            }
            if (drawOutline[Constants.RIGHT]) {
                gm.sr.rect(drawX + Constants.TILE_SIZE - Constants.OUTLINE_SIZE, drawY,
                        Constants.OUTLINE_SIZE, Constants.TILE_SIZE);
            }
            if (drawOutline[Constants.UP]) {
                gm.sr.rect(drawX, drawY, Constants.TILE_SIZE, Constants.OUTLINE_SIZE);
            }
            if (drawOutline[Constants.DOWN]) {
                gm.sr.rect(drawX, drawY + Constants.TILE_SIZE - Constants.OUTLINE_SIZE,
                        Constants.TILE_SIZE, Constants.OUTLINE_SIZE);
            }
        }

        //center circle
        if(fixed) {
            gm.sr.setColor(outlineColor);
            gm.sr.circle(drawX + Constants.TILE_SIZE / 2, drawY + Constants.TILE_SIZE / 2, Constants.FIXED_SIZE + Constants.OUTLINE_SIZE);
            gm.sr.setColor(Constants.outlineColor[WALL]);
            gm.sr.circle(drawX + Constants.TILE_SIZE / 2, drawY + Constants.TILE_SIZE / 2, Constants.FIXED_SIZE);
            gm.sr.setColor(Constants.color[WALL]);
            gm.sr.circle(drawX + Constants.TILE_SIZE / 2, drawY + Constants.TILE_SIZE / 2, Constants.FIXED_SIZE - Constants.OUTLINE_SIZE);
        }

        gm.sr.end();
    }

    public void drawKnives(GameManager gm, int boardX, int boardY) {
        int drawX = boardX + Constants.TILE_SIZE * x;
        int drawY = boardY + Constants.TILE_SIZE * y;

        gm.sr.begin(ShapeRenderer.ShapeType.Filled);
        gm.sr.setColor(Constants.KNIFE_COLOR);
        if(knifeHorz) {
            gm.sr.rect(drawX, drawY - Constants.KNIFE_WIDTH / 2, Constants.TILE_SIZE, Constants.KNIFE_WIDTH);
        }
        if(knifeVert) {
            gm.sr.rect(drawX - Constants.KNIFE_WIDTH / 2, drawY, Constants.KNIFE_WIDTH, Constants.TILE_SIZE);
        }
        gm.sr.end();
    }

    public boolean isFree() {
        if (type == EMPTY || type == ICE) {
            return !isBlock() && !isBox();
        }else if(type == WALL) {
            return false;
        }
        return false;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setControlled(boolean controlled) {
        this.controlled = controlled;
    }
    public boolean isControlled() {
        return controlled;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
    public boolean isFixed() {
        return fixed;
    }

    public void setBlock(boolean block) {
        this.block = block;
        if(block)
            box = false;
    }
    public boolean isBlock() {
        return block;
    }

    public void setBox(boolean box) {
        this.box = box;
        if(box)
            block = false;
    }
    public boolean isBox() {
        return box;
    }

    public void setControllable(boolean controllable) {
        this.controllable = controllable;
    }
    public boolean isControllable() {
        return controllable;
    }

    public void setInTransition(boolean inTransition) {
        this.inTransition = inTransition;
    }
    public boolean isInTransition() {
        return inTransition;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setKnifeHorz(boolean knifeHorz) {
        this.knifeHorz = knifeHorz;
    }
    public boolean isKnifeHorz() {
        return knifeHorz;
    }

    public void setKnifeVert(boolean knifeVert) {
        this.knifeVert = knifeVert;
    }
    public boolean isKnifeVert() {
        return knifeVert;
    }

    public void setSlippery(boolean slippery) {
        this.slippery = slippery;
    }
    public boolean isSlippery() {
        return slippery;
    }

}
