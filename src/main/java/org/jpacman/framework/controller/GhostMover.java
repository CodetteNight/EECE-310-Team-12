package org.jpacman.framework.controller;

import java.util.ArrayList;
import java.util.List;

import org.jpacman.framework.model.Board;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;
import org.jpacman.framework.model.GhostPath;
import org.jpacman.framework.model.IBoardInspector.SpriteType;
import org.jpacman.framework.model.Tile;


/**
 * Example, simple ghost mover that just moves ghosts randomly.
 *
 * @author Arie van Deursen; Aug 18, 2003
 */
public class GhostMover extends AbstractGhostMover {

	private Ghost theGhost;
	
	private List<GhostPath> path;
	
	private int count;
	
    /**
     * Start a new mover with the given engine.
     *
     * @param theEngine Engine used.
     */
    public GhostMover(final Game theEngine) {
        super(theEngine);
        path = new ArrayList<GhostPath>();
    }

    /**
     * Actually conduct a random move in the underlying engine.
     */
    public void doTick() {
        synchronized (gameInteraction()) {
            theGhost = getNextGhost();
            count = 0;
            path.clear();
            if (theGhost == null) {
                return;
            }
            //final Direction dir = randomMove();
            final Direction dir = pathfinder();
            gameInteraction().moveGhost(theGhost, dir);
        }
    }
    
    private Direction randomMove() {
    	int dirIndex = getRandomizer().nextInt(Direction.values().length);
        final Direction dir = Direction.values()[dirIndex];
        return dir;
    }
    
    private Direction pathfinder() {
    	System.out.println(nextGhost);
    	int i = 0;
    	int j = 0;
    	final Direction dir;
    	// init first tile as pacman's
    	Tile testTile = Game.getPlayer().getTile();
    	GhostPath pathTile = new GhostPath(testTile, count);
    	path.add(pathTile);
    	
    	while((count < 50) && (i == 0)){ // while no path is returned, iterate through map
    		int plength = path.size();
    		System.out.println("count:" + count);
    		//System.out.println("size:" + plength);
    		count++;
    		for(j=0;j<plength;j++){
     			if((path.get(j).getCount() + 1) == count){ // count is preincremented, so test each tile in the path list where count(current) == tile.count(prev) + 1
    				i = checkDirection(path.get(j).getTile());
    			}
    		}
    	}
    	
    	// return direction when ghost is found
		System.out.println("Switch Case" + i);
    	switch(i){
    		case 1:
    			dir = Direction.LEFT;
    			break;
    		case 2:
    			dir = Direction.RIGHT;
    			break;
    		case 3:
    			dir = Direction.UP;
    			break;
    		case 4:
    			dir = Direction.DOWN;
    			break;
    		default:
    			dir = randomMove();
    			break;
    	}
    	
    	return dir;
    }
    
    private int checkDirection(Tile tile){
    	Tile testTile;
    	GhostPath pathTile;
    	if(tile.getX() + 1 < getTheGame().getBoard().getWidth()){
        	testTile = tileAt(tile.getX()+1,tile.getY());
        	switch(checkTile(testTile)){
        		case -1:
        			// wall or visited tile
        			break;
        		case 1:
        			// ghost
        			return 1;        				
        		case 0:
        			// open space
        			pathTile = new GhostPath(testTile,count);
        			path.add(pathTile);
        			break;
        		default:
        			//other?
        			System.out.println("error1");
        			break;
        	}
    	}

    	if(tile.getX() - 1 > 0){
        	testTile = tileAt(tile.getX()-1,tile.getY());
        	switch(checkTile(testTile)){
        		case -1:
        			// wall or visited tile
        			break;
        		case 1:
        			// ghost
        			return 2;        				
        		case 0:
        			// open space
        			pathTile = new GhostPath(testTile,count);
        			path.add(pathTile);
        			break;
        		default:
        			//other?
        			System.out.println("error2");
        			break;
        	}
    	}
    	
    	testTile = tileAt(tile.getX(),tile.getY()+1);
    	switch(checkTile(testTile)){
    		case -1:
    			// wall or visited tile
    			break;
    		case 1:
    			// ghost
    			return 3;        				
    		case 0:
    			// open space
    			pathTile = new GhostPath(testTile,count);
    			path.add(pathTile);
    			break;
    		default:
    			//other?
    			System.out.println("error3");
    			break;
    	}
    	
    	testTile = tileAt(tile.getX(),tile.getY()-1);
    	switch(checkTile(testTile)){
    		case -1:
    			// wall or visited tile
    			break;
    		case 1:
    			// ghost
    			return 4;        				
    		case 0:
    			// open space
    			pathTile = new GhostPath(testTile,count);
    			path.add(pathTile);
    			break;
    		default:
    			//other?
    			System.out.println("error4");
    			break;
    	}
		System.out.println("noghost");
    	return 0;
    }
    
	private int checkTile(Tile tile){
		for(GhostPath pathTile : path){
			if((pathTile.getX() == tile.getX()) && (pathTile.getY() == tile.getY())){ // if tile has been visited before
				System.out.println("parity");
				return -1; // dont add to path
			}
		}
		if(tile.topSprite() == null){
			return 0; // add to path
		}
		else if(tile.topSprite().getSpriteType() == SpriteType.WALL){
			System.out.println("wall");
			return -1; // dont add to path
		}
		else if(tile == theGhost.getTile()){
			return 1; // return dir
		}
		else if(tile.topSprite().getSpriteType() == SpriteType.FOOD){
			return 0; // add to path
		}
		else
			return -1;
	}
	
	/**
	 * Convenience method to return a tile at a given location.
	 * Helpful for testing effect of moves.
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @return Tile at (x,y)
	 */
	protected Tile tileAt(int x, int y) {
		return getTheGame().getBoardInspector().tileAt(x, y);
	}
    
}
