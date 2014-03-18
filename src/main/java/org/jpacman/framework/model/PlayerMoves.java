package org.jpacman.framework.model;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class PlayerMoves extends Moves {

	private final Direction reverseDirection;
	private final Direction orientation;

	PlayerMoves(Sprite s, Tile t, Direction d, boolean b, Direction orig) {
		super(s, t);
		orientation = orig;
		reverseDirection = reverseDirection(d);

	}

	public Direction getOrientation() {
		return orientation;
	}
	public Direction getRevDir() {
		return reverseDirection;
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
		}

		assert result != null : "PlayerMoves: Direction & reverse Direction not null";
		return result;
	}

}