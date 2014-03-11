package main.java.org.jpacman.framework.model;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class PlayerMoves extends Moves {

	final Direction dir;
	final Direction revDir;
	final boolean ateFood;

	PlayerMoves(Sprite s, Tile t, Direction d, boolean b) {
		super(s, t);
		dir = d;
		ateFood = b;
		revDir = reverseDirection(d);
	}
	
	public Direction getDirection() {
		return dir;
	}

	public boolean eatenFood() {
		return ateFood;
	}

	public Direction getRevDir() {
		return revDir;
	}

	private Direction reverseDirection(Direction dir) {
		Direction result = null;
		switch (dir) {
			case DOWN:
				result = Direction.UP;
				break;
			case LEFT:
				result = Direction.RIGHT;
				break;
			case RIGHT:
				result = Direction.LEFT;
				break;
			case UP:
				result = Direction.DOWN;
				break;
			default:
				break;
		}

		assert result != null : "UndoableGame: Direction & reverse Direction not null";
		return result;
	}

}