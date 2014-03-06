package main.java.org.jpacman.framework.model;

import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class Moves {

	// final Player player;
	// final Ghost theGhost;

	// final Direction dir;
	// final Food food;

	final Sprite sprite;
	final Tile tile;

	/*
	 * Moves(Player p, Ghost g, Sprite s, Direction d, Food f) { // player = p; // theGhost = g; if
	 * (s.getSpriteType() == SpriteType.GHOST) { theGhost = (Ghost) s; } if (s.getSpriteType() ==
	 * SpriteType.PLAYER) { player = (Player) s; } if (s.getSpriteType() == SpriteType.FOOD) { food
	 * = (Food) s; } dir = d; food = f; }
	 */

	Moves(Sprite s, Tile t) {
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
