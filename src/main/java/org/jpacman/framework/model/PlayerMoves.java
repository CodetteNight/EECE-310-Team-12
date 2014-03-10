package main.java.org.jpacman.framework.model;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class PlayerMoves extends Moves {

	final Direction dir;

	PlayerMoves(Sprite s, Tile t, Direction d) {
		super(s, t);
		dir = d;
	}
	
	public Direction getDirection() {
		return dir;
	}

}