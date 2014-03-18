package org.jpacman.framework.model;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class GhostMoves extends Moves {

	private final Direction directionOfMovement;
	private final Direction reverseDirection;

	GhostMoves(Sprite s, Tile t, Direction d) {
		super(s, t);
		directionOfMovement = d;
		reverseDirection = reverseDirection(d);
	}

	public Direction getDirectionOfMovement() {
		return directionOfMovement;
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
//			default:
//				break;
		}

		assert result != null : "GhostMoves: Direction & reverse Direction not null";
		return result;
	}

}