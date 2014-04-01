package org.jpacman.framework.controller;

import java.util.ArrayList;
import java.util.List;

import org.jpacman.framework.model.Board;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;
import org.jpacman.framework.model.GhostPath;
import org.jpacman.framework.model.IBoardInspector.SpriteType;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;
import org.jpacman.test.framework.model.SpriteTest;


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
        count = 0;
        path.clear();
        System.out.println(path.size());
    	int i = 0;
    	int j = 0;
    	final Direction dir;
    	// init first tile as pacman's
    	Tile testTile = Game.getPlayer().getTile();
    	GhostPath pathTile = new GhostPath(testTile, count);
    	path.add(pathTile);
    	
    	while(i == 0){ // while no path is returned, iterate through map
    		int plength = path.size();
    		//System.out.println("count:" + count);
    		//System.out.println("size:" + plength);
    		count++;
    		for(j=0;j<plength;j++){
     			if((path.get(j).getCount() + 1) == count){ // count is preincremented, so test each tile in the path list where count(current) == tile.count(prev) + 1
    				i = checkDirection(path.get(j).getTile());
    				System.out.println(i);
    				if(i == 1 || i == 2 || i == 3 || i == 4){
    					break;
    				}
    			}
    		}
//    		if(count>50){
//    			System.out.println("array start");
//    			for(GhostPath tile : path){
//    				System.out.println("("+tile.getX()+","+tile.getY()+")\t" +tile.getCount());
//    			}
//    			System.out.println("array end");
//    		}
    	}
    	
    	// return direction when ghost is found
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
    	
	    if(tile.getX() + 1 < getTheGame().getBoard().getWidth()){ // check if tile is in bounds
	        testTile = tileAt(tile.getX()+1,tile.getY()); // check right tile
	        if(checkTile(testTile) == -1){
	    			// wall or visited tile
	    	}
	    	else if(checkTile(testTile) == 1){
	    		// ghost
	    		return 1;
	    	}
	    	else if(checkTile(testTile) == 0){
			    // open space
				pathTile = new GhostPath(testTile,count);
				path.add(pathTile);
	    	}
    	}

    	if(tile.getX() - 1 > 0){ // check if tile is in bounds
        	testTile = tileAt(tile.getX()-1,tile.getY()); //check left tile
        	if(checkTile(testTile) == -1){
        			// wall or visited tile
        	}
        	else if(checkTile(testTile) == 1){
        		// ghost
        		return 2;
        	}
        	else if(checkTile(testTile) == 0){
    		    // open space
    			pathTile = new GhostPath(testTile,count);
    			path.add(pathTile);
        	}
    	}
    	
    	testTile = tileAt(tile.getX(),tile.getY()+1); // check down tile
    	if(checkTile(testTile) == -1){
			// wall or visited tile
		}
		else if(checkTile(testTile) == 1){
			// ghost
			return 3;
		}
		else if(checkTile(testTile) == 0){
		    // open space
			pathTile = new GhostPath(testTile,count);
			path.add(pathTile);
		}
    	
    	testTile = tileAt(tile.getX(),tile.getY()-1); //check up tile
    	if(checkTile(testTile) == -1){
			// wall or visited tile
		}
		else if(checkTile(testTile) == 1){
			// ghost
			return 4;
		}
		else if(checkTile(testTile) == 0){
		    // open space
			pathTile = new GhostPath(testTile,count);
			path.add(pathTile);
		}
			//System.out.println("noghost");
    	return 0;
	}
    
	private int checkTile(Tile tile){
		for(GhostPath pathTile : path){
			if((pathTile.getX() == tile.getX()) && (pathTile.getY() == tile.getY())){ // if tile has been visited before
				//System.out.println("parity");
				return -1; // dont add to path
			}
		}
		if(tile.containsSprite(theGhost)){
			return 1; // return dir
		}
		else if(tile.topSprite() == null){
			return 0; // add to path
		}
		else if(tile.topSprite().getSpriteType() == SpriteType.FOOD){
			return 0; // add to path
		}
		else if(tile.topSprite().getSpriteType() == SpriteType.FRUIT){
			return 0; // add to path
		}
		else if(tile.topSprite().getSpriteType() == SpriteType.WALL){
			//System.out.println("wall");
			return -1; // dont add to path
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
