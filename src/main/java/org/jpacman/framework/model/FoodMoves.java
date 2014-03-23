package org.jpacman.framework.model;

import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class FoodMoves extends Moves {

	private final int foodPts;

	FoodMoves(Sprite s, Tile t, int pts) {
		super(s, t);
		foodPts = pts;
	}

	public int getFoodPts() {
		return foodPts;
	}
}