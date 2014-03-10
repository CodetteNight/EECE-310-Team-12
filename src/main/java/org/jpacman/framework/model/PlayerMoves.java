package main.java.org.jpacman.framework.model;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class PlayerMoves extends Moves {

	final int points;
	final Direction dir;
	final Direction original;

	public PlayerMoves(Sprite s, Tile t, Direction d, int pts) {
		super(s, t);
		points = pts;
		original = d;
		dir = reverseDirection(d);
	}

	public int getFoodPoint() {
		return points;
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