package org.jpacman.framework.model;

import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

/**
 * A list of moves that was made by the Sprites(ghost and players) during the game
 */

abstract class Moves {

	final Sprite sprite;
	final Tile tile;

	public Moves(Sprite s, Tile t) {
		sprite = s;
		tile = t;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Tile getTile() {
		return tile;
	}

}
