package main.java.org.jpacman.framework.model;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class PlayerMoves extends Moves {

	final int points;
	final Direction dir;

	public PlayerMoves(Sprite s, Tile t, Direction d, int pts) {
		super(s, t);
		points = pts;
		dir = d;
	}

	public int getFoodPoint() {
		return points;
	}
	
	public Direction getDirection() {
		return dir;
	}

}