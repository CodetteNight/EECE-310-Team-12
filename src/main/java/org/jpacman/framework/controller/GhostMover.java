package org.jpacman.framework.controller;

import java.util.ArrayList;
import java.util.List;

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
		System.out.println("Pathfinder Start");
    	Direction dir = Direction.RIGHT;
    	int i = 0;
    	Tile testTile = new Tile(Game.getPlayer().getTile().getX(),Game.getPlayer().getTile().getY());
    	count = 0;
		System.out.println("Path init");
    	GhostPath pathTile = new GhostPath(testTile, count);
    	path.add(pathTile);
    	
		System.out.println("Search Start");
    	while(count<50){
    		count++;
    		for(GhostPath tile : path){
     			if((tile.getCount() + 1) == count){
    				i = checkDirection(tile.getTile());
    			}
    		}
    	}
		System.out.println("Switch Case");
    	switch(i){
    		case 1:
    			dir = Direction.RIGHT;
    			break;
    		case 2:
    			dir = Direction.LEFT;
    			break;
    		case 3:
    			dir = Direction.DOWN;
    			break;
    		case 4:
    			dir = Direction.UP;
    			break;
    		default:
    			dir = randomMove();
    			break;
    	}
    	
    	return dir;
    }
    
    // TODO: check which ghost
    public int checkDirection(Tile tile){
    	Tile testTile = new Tile(tile.getX() + 1, tile.getY());
    	GhostPath pathTile;
    	switch(checkTile(testTile)){
    		case -1:
    			// wall or visited tile
    			break;
    		case 1:
    			// ghost
    			//if(theGhost == testTile.topSprite()){
    				return 1;
    			//}
    			//break;
    		case 0:
    			// open space
    			pathTile = new GhostPath(testTile,count);
    			path.add(pathTile);
    			break;
    		default:
    			//other?
    	}
    	
    	testTile = new Tile(tile.getX() - 1, tile.getY());
    	switch(checkTile(testTile)){
    		case -1:
    			// wall or visited tile
    			break;
    		case 1:
    			// ghost
    			//if(theGhost == testTile.topSprite()){
    				return 2;
    			//}
    			//break;
    		case 0:
    			// open space
    			pathTile = new GhostPath(testTile,count);
    			path.add(pathTile);
    			break;
    		default:
    			//other?
    			break;
    	}
    	
    	testTile = new Tile(tile.getX(), tile.getY() + 1);
    	switch(checkTile(testTile)){
    		case -1:
    			// wall or visited tile
    			break;
    		case 1:
    			// ghost
    			//if(theGhost == testTile.topSprite()){
    				return 3;
    			//}
    			//break;
    		case 0:
    			// open space
    			pathTile = new GhostPath(testTile,count);
    			path.add(pathTile);
    			break;
    		default:
    			//other?
    	}
    	
    	testTile = new Tile(tile.getX(), tile.getY() - 1);
    	switch(checkTile(testTile)){
    		case -1:
    			// wall or visited tile
    			break;
    		case 1:
    			// ghost
    			//if(theGhost == testTile.topSprite()){
    				return 4;
    			//}
    			//break;
    		case 0:
    			// open space
    			pathTile = new GhostPath(testTile,count);
    			path.add(pathTile);
    			break;
    		default:
    			//other?
    			break;
    	}
    	return 0;
    }
    
	public int checkTile(Tile tile){
		for(GhostPath pathTile : path){
			if((pathTile.getX() == tile.getX()) && (pathTile.getY() == tile.getY()) && (pathTile.getCount() >= count)){
				return -1;
			}
		}
		if(tile.topSprite().getSpriteType() == SpriteType.WALL){
			return -1;
		}
		else if(tile.topSprite().getSpriteType() == SpriteType.GHOST){
			return 1;
		}
		else
			return 0;
	}
    
}
