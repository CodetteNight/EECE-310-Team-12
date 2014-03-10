package main.java.org.jpacman.framework.model;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class GhostMoves extends Moves {

	final Direction dir;
	final Direction original;

	GhostMoves(Sprite s, Tile t, Direction d) {
		super(s, t);
		original = d;
		dir = reverseDirection(d);
	}

	public Direction getDirection() {
		return dir;
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

	public Direction reverseDirection() {
		return reverseDirection(this.original);
	}

}