package com.rileyborgard.game.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rileyborgard.game.Constants;
import com.rileyborgard.game.GameManager;

import java.util.ArrayList;

/**
 * Created by Riley on 5/21/2017.
 */

public class Board {

    //state based information about all the tiles
    private Tile tiles[][];

    //arraylist providing object based information about where all the controlled blocks are
    private ArrayList<Integer> playerX, playerY;

    //position of the board, i.e. the position on the screen where it draws the top left corner
    //of the board.
    private int x, y;

    //true if the current level is solved
    private boolean solved;

    //keeps track of how far the players are in the transition animation.
    //ranges from 0 to 1
    private float transition;
    //the direction to move players in after the transition animation
    private int transitionDX, transitionDY;

    //whether or not a tile has been visited/checked in the place free method
    private boolean visited[][];
    private boolean placeFree[][];
    //whether or not a tile has been checked if it is on ice
    private boolean iceVisited[][];
    //whether or not we have determined if this tile is transitioning because
    //transitionUnit was called
    private boolean unitVisited[][];

    public Board(int boardWidth, int boardHeight) {
        x = 0;
        y = 0;
        tiles = new Tile[boardWidth][boardHeight];
        playerX = new ArrayList<Integer>();
        playerY = new ArrayList<Integer>();
    }

    public void draw(GameManager gm) {
        int drawX = x;
        int drawY = y;
        boolean drawOutlines[] = new boolean[4];
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[0].length; j++) {
                //set drawOutlines based on whether there are walls next to it
                drawOutlines[Constants.LEFT] = (tiles[i][j].getType() == Tile.WALL &&
                        inBounds(i - 1, j) && tiles[i - 1][j].getType() != Tile.WALL);
                drawOutlines[Constants.RIGHT] = (tiles[i][j].getType() == Tile.WALL &&
                        inBounds(i + 1, j) && tiles[i + 1][j].getType() != Tile.WALL);
                drawOutlines[Constants.UP] = (tiles[i][j].getType() == Tile.WALL &&
                        inBounds(i, j - 1) && tiles[i][j - 1].getType() != Tile.WALL);
                drawOutlines[Constants.DOWN] = (tiles[i][j].getType() == Tile.WALL &&
                        inBounds(i, j + 1) && tiles[i][j + 1].getType() != Tile.WALL);
                //draw that tile
                tiles[i][j].draw(gm, drawX, drawY, solved, drawOutlines);
            }
        }
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[0].length; j++) {
                //set drawOutlines based on whether there are walls next to it
                drawOutlines[Constants.LEFT] = drawOutlineBetween(i, j, -1, 0);
                drawOutlines[Constants.RIGHT] = drawOutlineBetween(i, j, 1, 0);
                drawOutlines[Constants.UP] = drawOutlineBetween(i, j, 0, -1);
                drawOutlines[Constants.DOWN] = drawOutlineBetween(i, j, 0, 1);
                //draw that tile
                tiles[i][j].drawLayer2(gm, drawX, drawY, solved, transition, transitionDX, transitionDY, drawOutlines);
            }
        }
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[0].length; j++) {
                tiles[i][j].drawKnives(gm, drawX, drawY);
            }
        }
    }

    public void update(float dt) {
        if(transition > 0) {
            transition += Constants.TRANSITION_SPEED * dt;
            if(transition >= 1f) {
                transition = -0.0000001f;
                movePlayers();
            }
        }
//        System.out.println(playerX.size() + " " + transition);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setTile(int x, int y, Tile tile) {
        tiles[x][y] = tile;
        if(tile.isControlled()) {
            playerX.add(x);
            playerY.add(y);
        }
    }

    //controls any blocks touching a controlled block.
    public void convertBlocks() {
//        for(int i = 0; i < playerX.size(); i++) {
//            int px = playerX.get(i);
//            int py = playerY.get(i);
//            if(px > 0 && tiles[px - 1][py].isBlock() && !tiles[px][py].isKnifeVert()) {
//                tiles[px - 1][py].setControllable(true);
//                if(!tiles[px - 1][py].isControlled()) {
//                    tiles[px - 1][py].setControlled(true);
//                    playerX.add(px - 1);
//                    playerY.add(py);
//                }
//            }
//            if(px < tiles.length - 1 && tiles[px + 1][py].isBlock() && !tiles[px + 1][py].isKnifeVert()) {
//                tiles[px + 1][py].setControllable(true);
//                if(!tiles[px + 1][py].isControlled()) {
//                    tiles[px + 1][py].setControlled(true);
//                    playerX.add(px + 1);
//                    playerY.add(py);
//                }
//            }
//            if(py > 0 && tiles[px][py - 1].isBlock() && !tiles[px][py].isKnifeHorz()) {
//                tiles[px][py - 1].setControllable(true);
//                if(!tiles[px][py - 1].isControlled()) {
//                    tiles[px][py - 1].setControlled(true);
//                    playerX.add(px);
//                    playerY.add(py - 1);
//                }
//            }
//            if(py < tiles[0].length - 1 && tiles[px][py + 1].isBlock() && !tiles[px][py + 1].isKnifeHorz()) {
//                tiles[px][py + 1].setControllable(true);
//                if(!tiles[px][py + 1].isControlled()) {
//                    tiles[px][py + 1].setControlled(true);
//                    playerX.add(px);
//                    playerY.add(py + 1);
//                }
//            }
//        }
        ArrayList<Integer> controlX = new ArrayList<Integer>();
        ArrayList<Integer> controlY = new ArrayList<Integer>();
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[0].length; j++) {
                if(tiles[i][j].isControllable()) {
                    controlX.add(i);
                    controlY.add(j);
                }
            }
        }
        for(int i = 0; i < controlX.size(); i++) {
            int px = controlX.get(i);
            int py = controlY.get(i);
            if(px > 0 && tiles[px - 1][py].isBlock() && !tiles[px][py].isKnifeVert()) {
                if(!tiles[px - 1][py].isControllable()) {
                    controlX.add(px - 1);
                    controlY.add(py);
                }
                tiles[px - 1][py].setControllable(true);
            }
            if(px < tiles.length - 1 && tiles[px + 1][py].isBlock() && !tiles[px + 1][py].isKnifeVert()) {
                if(!tiles[px + 1][py].isControllable()) {
                    controlX.add(px + 1);
                    controlY.add(py);
                }
                tiles[px + 1][py].setControllable(true);
            }
            if(py > 0 && tiles[px][py - 1].isBlock() && !tiles[px][py].isKnifeHorz()) {
                if(!tiles[px][py - 1].isControllable()) {
                    controlX.add(px);
                    controlY.add(py - 1);
                }
                tiles[px][py - 1].setControllable(true);
            }
            if(py < tiles[0].length - 1 && tiles[px][py + 1].isBlock() && !tiles[px][py + 1].isKnifeHorz()) {
                if(!tiles[px][py + 1].isControllable()) {
                    controlX.add(px);
                    controlY.add(py + 1);
                }
                tiles[px][py + 1].setControllable(true);
            }
        }
        if(playerX.size() > 0) {
            control(playerX.get(0), playerY.get(0));
        }
    }

    //begins transition animation of players moving in the direction <dx, dy>.
    //after animation is done, movePlayers will be called from the update method
    public boolean transitionPlayers(int dx, int dy) {
        if(playerX.size() > 0 && placeFree(playerX.get(0), playerY.get(0), dx, dy)) {
            transition = 0.00001f;
            transitionDX = dx;
            transitionDY = dy;
            for(int i = 0; i < tiles.length; i++) {
                for(int j = 0; j < tiles[0].length; j++) {
                    if(visited[i][j] && (tiles[i][j].isBox() || tiles[i][j].isBlock())) {
                        tiles[i][j].setInTransition(true);
                    }
                }
            }
            return true;
        }
        return false;
    }
    public boolean transitionUnit(int x, int y, int dx, int dy) {
        visited = new boolean[tiles.length][tiles[0].length];
        placeFree = new boolean[tiles.length][tiles[0].length];
        if(placeFreeAt(x, y, dx, dy)) {
            transition = 0.00001f;
            transitionDX = dx;
            transitionDY = dy;
            for(int i = 0; i < tiles.length; i++) {
                for(int j = 0; j < tiles[0].length; j++) {
                    if(visited[i][j] && (tiles[i][j].isBox() || tiles[i][j].isBlock())) {
                        tiles[i][j].setInTransition(true);
                        unitVisited[i][j] = true;
                    }
                }
            }
            return true;
        }
        return false;
    }

    //moves all players in the direction <transitionDX, transitionDY>.
    private void movePlayers() {
        int dx = transitionDX;
        int dy = transitionDY;
        boolean playerMoving = playerX.size() > 0 && tiles[playerX.get(0)][playerY.get(0)].isInTransition();

        //labels all of the moving tiles as visited
        Tile tiles2[][] = new Tile[tiles.length][tiles[0].length];
        ArrayList<Integer> moveX = new ArrayList<Integer>();
        ArrayList<Integer> moveY = new ArrayList<Integer>();
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[0].length; j++) {
                if(tiles[i][j].isInTransition() && (tiles[i][j].isBlock() || tiles[i][j].isBox())) {
                    tiles2[i + dx][j + dy] = new Tile(tiles[i][j]);
                    tiles2[i + dx][j + dy].setPosition(i + dx, j + dy);
                    tiles2[i + dx][j + dy].setType(tiles[i + dx][j + dy].getType());
                    tiles2[i + dx][j + dy].setKnifeHorz(tiles[i + dx][j + dy].isKnifeHorz());
                    tiles2[i + dx][j + dy].setKnifeVert(tiles[i + dx][j + dy].isKnifeVert());
                    boolean h = tiles[i][j].isKnifeHorz();
                    boolean v = tiles[i][j].isKnifeVert();
                    tiles[i][j] = new Tile(tiles[i][j].getType(), i, j);
                    tiles[i][j].setKnifeHorz(h);
                    tiles[i][j].setKnifeVert(v);
                    moveX.add(i + dx);
                    moveY.add(j + dy);
                }
            }
        }

        //pick a random player in the list, and reselect it.
        //this is useful for the case where a knife splits your player in half,
        //so you have to reselect one of the halves
        int randX = -1;
        int randY = -1;
        //we should reset the players only if they are moving
        if(playerMoving) {
            int rand = (int) (Math.random() * playerX.size());
            randX = playerX.get(rand) + dx;
            randY = playerY.get(rand) + dy;
            playerX.clear();
            playerY.clear();
        }

        //paste the tiles back in their new positions, and update players arraylist
        for(int i = 0; i < moveX.size(); i++) {
            tiles[moveX.get(i)][moveY.get(i)] = tiles2[moveX.get(i)][moveY.get(i)];
            if(tiles[moveX.get(i)][moveY.get(i)].isControlled()) {
                playerX.add(moveX.get(i));
                playerY.add(moveY.get(i));
            }
        }
        convertBlocks();

        //slide things on ice if necessary
        unitVisited = new boolean[tiles.length][tiles[0].length];
        for(int idx = 0; idx < moveX.size(); idx++) {
            int i = moveX.get(idx);
            int j = moveY.get(idx);
            if(unitVisited[i][j])
                continue;
            iceVisited = new boolean[tiles.length][tiles[0].length];
            if(tiles[i][j].isInTransition() && keepSliding(i, j, dx, dy)) {
                if(!transitionUnit(i, j, dx, dy)) {
                    tiles[i][j].setInTransition(false);
                }
            }else {
                tiles[i][j].setInTransition(false);
            }
        }

        if(inBounds(randX, randY)) {
            control(randX, randY);
        }

        //check if solved, and make sure no tiles are in transition
        solved = true;
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[0].length; j++) {
                if(tiles[i][j].isBlock() && !tiles[i][j].isControlled()) {
                    solved = false;
                }
            }
        }
    }

    //whether or not box/block at (px, py) is completely on ice
    private boolean keepSliding(int px, int py, int dx, int dy) {
        visited = new boolean[tiles.length][tiles[0].length];
        placeFree = new boolean[tiles.length][tiles[0].length];
        placeFreeAt(px, py, dx, dy);
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[0].length; j++) {
                if(visited[i][j] && (tiles[i][j].isBlock() || tiles[i][j].isBox()) &&
                        tiles[i][j].getType() != Tile.ICE && !tiles[i][j].isSlippery()) {
                    return false;
                }
            }
        }
        return true;

//        if(tiles[px][py].getType() != Tile.ICE)
//            return false;
//        if(!tiles[px][py].isControllable())
//            return true;
//        iceVisited[px][py] = true;
//        if(inBounds(px - 1, py) && !iceVisited[px - 1][py] && tiles[px - 1][py].isControllable() &&
//                !tiles[px][py].isKnifeVert() && !onIce(px - 1, py))
//            return false;
//        if(inBounds(px + 1, py) && !iceVisited[px + 1][py] && tiles[px + 1][py].isControllable() &&
//                !tiles[px + 1][py].isKnifeVert() && !onIce(px + 1, py))
//            return false;
//        if(inBounds(px, py - 1) && !iceVisited[px][py - 1] && tiles[px][py - 1].isControllable() &&
//                !tiles[px][py].isKnifeHorz() && !onIce(px, py - 1))
//            return false;
//        if(inBounds(px, py + 1) && !iceVisited[px][py + 1] && tiles[px][py + 1].isControllable() &&
//                !tiles[px][py + 1].isKnifeHorz() && !onIce(px, py + 1))
//            return false;
//        return true;
    }

    //move board (scroll)
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    //whether or not to draw outline between tile at (x, y) and at (x+dx, y+dy)
    public boolean drawOutlineBetween(int x, int y, int dx, int dy) {
        if(!inBounds(x + dx, y + dy))
            return false;
        if(tiles[x][y].isBox() || (tiles[x][y].isBlock() && !tiles[x][y].isControllable())) {
            return true;
        }
        if(tiles[x][y].isBlock() && !tiles[x + dx][y + dy].isBlock())
            return true;
        if(tiles[x][y].isBlock() && tiles[x + dx][y + dy].isBlock()) {
            if(dx == -1 && dy == 0 && tiles[x][y].isKnifeVert())
                return transitionDY == 0 || !tiles[x + dx][y + dy].isControlled();
            if(dx == 1 && dy == 0 && inBounds(x + 1, y) && tiles[x + 1][y].isKnifeVert())
                return transitionDY == 0 || !tiles[x + dx][y + dy].isControlled();
            if(dx == 0 && dy == -1 && tiles[x][y].isKnifeHorz())
                return transitionDX == 0 || !tiles[x + dx][y + dy].isControlled();
            if(dx == 0 && dy == 1 && inBounds(x, y + 1) && tiles[x][y + 1].isKnifeHorz())
                return transitionDX == 0 || !tiles[x + dx][y + dy].isControlled();
        }
        return false;
    }

    //true if moving players in the direction <dx, dy> is valid.
    //e.g. no walls blocking the way
    public boolean placeFree(int px, int py, int dx, int dy) {
        visited = new boolean[tiles.length][tiles[0].length];
        placeFree = new boolean[tiles.length][tiles[0].length];
        return placeFreeAt(px, py, dx, dy);
    }

    //returns true if a box/block at (x, y) is able to move in the direction of <dx, dy>
    //a controllable block can only move if all blocks it's connected to can move as well.
    //uncontrollable blocks and boxes are not linked to anything, so they move independently
    private boolean placeFreeAt(int x, int y, int dx, int dy) {
        if (!inBounds(x, y)) {
            return false;
        }
        if(visited[x][y])
            return placeFree[x][y];
        visited[x][y] = true;
        placeFree[x][y] = true;
        //treated as independent units
        if(tiles[x][y].isBox() || (tiles[x][y].isBlock() && !tiles[x][y].isControllable())) {
            placeFree[x][y] = !tiles[x][y].isFixed() && placeFreeAt(x + dx, y + dy, dx, dy);
            if(dx == -1 && dy == 0 && tiles[x][y].isKnifeVert())
                placeFree[x][y] = false;
            if(dx == 1 && dy == 0 && inBounds(x + 1, y) && tiles[x + 1][y].isKnifeVert())
                placeFree[x][y] = false;
            if(dx == 0 && dy == -1 && tiles[x][y].isKnifeHorz())
                placeFree[x][y] = false;
            if(dx == 0 && dy == 1 && inBounds(x, y + 1) && tiles[x][y + 1].isKnifeHorz())
                placeFree[x][y] = false;
        }else if(tiles[x][y].isBlock() && tiles[x][y].isControllable()) {
            placeFree[x][y] = !tiles[x][y].isFixed() && placeFreeAt(x + dx, y + dy, dx, dy);
            if(placeFree[x][y] && inBounds(x - 1, y) && tiles[x - 1][y].isControllable() && !tiles[x][y].isKnifeVert()) {
                placeFree[x][y] &= placeFreeAt(x - 1, y, dx, dy);
            }
            if(placeFree[x][y] && inBounds(x + 1, y) && tiles[x + 1][y].isControllable() && !tiles[x + 1][y].isKnifeVert()) {
                placeFree[x][y] &= placeFreeAt(x + 1, y, dx, dy);
            }
            if(placeFree[x][y] && inBounds(x, y - 1) && tiles[x][y - 1].isControllable() && !tiles[x][y].isKnifeHorz()) {
                placeFree[x][y] &= placeFreeAt(x, y - 1, dx, dy);
            }
            if(placeFree[x][y] && inBounds(x, y + 1) && tiles[x][y + 1].isControllable() && !tiles[x][y + 1].isKnifeHorz()) {
                placeFree[x][y] &= placeFreeAt(x, y + 1, dx, dy);
            }

            //knives
            if(dx == -1 && dy == 0 && tiles[x][y].isKnifeVert())
                placeFree[x][y] = false;
            if(dx == 1 && dy == 0 && inBounds(x + 1, y) && tiles[x + 1][y].isKnifeVert())
                placeFree[x][y] = false;
            if(dx == 0 && dy == -1 && tiles[x][y].isKnifeHorz())
                placeFree[x][y] = false;
            if(dx == 0 && dy == 1 && inBounds(x, y + 1) && tiles[x][y + 1].isKnifeHorz())
                placeFree[x][y] = false;
        }else {
            placeFree[x][y] = tiles[x][y].isFree();
        }
        return placeFree[x][y];
    }

    //makes all blocks uncontrolled, then controls the block cluster at the given point
    public void control(int px, int py) {
        for(int i = 0; i < playerX.size(); i++) {
            tiles[playerX.get(i)][playerY.get(i)].setControlled(false);
        }
        playerX.clear();
        playerY.clear();
        select(px, py);
    }

    //uses flood fill algorithm to control a cluster of blocks.
    private void select(int px, int py) {
        if(inBounds(px, py) && tiles[px][py].isBlock() &&
                tiles[px][py].isControllable() &&
                !tiles[px][py].isControlled()) {
            playerX.add(px);
            playerY.add(py);
            tiles[px][py].setControlled(true);

            if(!tiles[px][py].isKnifeVert())
                select(px - 1, py);
            if(inBounds(px + 1, py) && !tiles[px + 1][py].isKnifeVert())
                select(px + 1, py);
            if(!tiles[px][py].isKnifeHorz())
                select(px, py - 1);
            if(inBounds(px, py + 1) && !tiles[px][py + 1].isKnifeHorz())
                select(px, py + 1);

        }
    }

    //true if players are still in motion. This presumes there is a transition animation when
    //players are moving.
    public boolean isPlayerInTransition() {
        return transition > 0;
    }

    public boolean isSolved() {
        return solved;
    }

    public boolean isPlayerAt(int px, int py) {
        if(!inBounds(px, py))
            return false;
        return tiles[px][py].isBlock() && tiles[px][py].isControllable();
    }

    //true if point is in the bounds of the board, i.e. the argument is a valid tile index
    public boolean inBounds(int px, int py) {
        return px >= 0 && px < tiles.length &&
                py >= 0 && py < tiles[0].length;
    }

    //converts between different coordinate systems.
    //absolute point refers to screen position in pixels
    //index point refers to the 2d index of a tile
//    public Point getAbsolutePoint(int px, int py) {
//        return new Point(x + px * Constants.TILE_SIZE, y + indexPoint.y * Constants.TILE_SIZE);
//    }
    public int getIndexX(int ax) {
        return (ax - x) / Constants.TILE_SIZE;
    }
    public int getIndexY(int ay) {
        return (ay - y) / Constants.TILE_SIZE;
    }

}
