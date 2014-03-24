package org.jpacman.framework.controller;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;


/**
 * Example, simple ghost mover that just moves ghosts randomly.
 *
 * @author Arie van Deursen; Aug 18, 2003
 */
public class GhostMover extends AbstractGhostMover {

	private Ghost theGhost;
	
    /**
     * Start a new mover with the given engine.
     *
     * @param theEngine Engine used.
     */
    public GhostMover(final Game theEngine) {
        super(theEngine);
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
            final Direction dir = randomMove();
            gameInteraction().moveGhost(theGhost, dir);
        }
    }
    
    private Direction randomMove() {
    	int dirIndex = getRandomizer().nextInt(Direction.values().length);
        final Direction dir = Direction.values()[dirIndex];
        return dir;
    }
    
//    private Direction chaseMove() {
//    	final Direction dir = Direction.RIGHT;
//    	
//    	return dir;
//    }
    
}
