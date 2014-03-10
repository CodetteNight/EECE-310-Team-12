package main.java.org.jpacman.framework.model;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class PlayerMovesOverFood extends PlayerMoves {

	final int points;

	PlayerMovesOverFood(Sprite s, Tile t, Direction d, int pts) {
		super(s, t, d);
		points = pts;
	}

	public int getFoodPoint() {
		return points;
	}
}