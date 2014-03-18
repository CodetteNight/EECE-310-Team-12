package org.jpacman.framework.model;

import java.util.ArrayDeque;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Food;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;
import org.jpacman.framework.model.IBoardInspector.SpriteType;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class UndoableGame extends Game implements IGameInteractorWithUndo {

	private ArrayDeque<Moves> moves = new ArrayDeque<Moves>();

	@Override
	public void undo() {
		Moves move;
		SpriteType sprite;
		if(!moves.isEmpty() && getPlayer().isAlive()) {
			// Peek the deque for the last move made and attempt to remove 
			// all consecutive ghost moves until a player move is found, 
			// then attempt to remove the next consecutive ghost moves
			move = moves.peekLast();
			sprite = move.getSprite().getSpriteType();
			while( sprite == SpriteType.GHOST ){
				super.moveGhost((Ghost) move.getSprite(), ((GhostMoves) move).getRevDir());
				moves.removeLast();
				if( moves.isEmpty() ) return;
				move = moves.peekLast();
				sprite = move.getSprite().getSpriteType();
			}
			if( sprite == SpriteType.PLAYER ){
				super.movePlayer(((PlayerMoves) move).getRevDir());
				getPlayer().setDirection(((PlayerMoves) move).getOrientation());
				moves.removeLast();

				if( moves.isEmpty() ) return;
				move = moves.peekLast();
				if( move.getSprite().getSpriteType() == SpriteType.FOOD ){
					move.getSprite().occupy(move.getTile());
					super.getPointManager().consumePointsOnBoard(getPlayer(),-((FoodMoves) move).foodPts);
					moves.removeLast();
				}
			}
			if( moves.isEmpty() ) return;
			move = moves.peekLast();
			sprite = move.getSprite().getSpriteType();
			while( sprite == SpriteType.GHOST ){
				super.moveGhost((Ghost) move.getSprite(), ((GhostMoves) move).getRevDir());
				moves.removeLast();
				if( moves.isEmpty() ) return;
				move = moves.peekLast();
				sprite = move.getSprite().getSpriteType();
			}
		}
		return;
	}

	@Override
	public void movePlayer(Direction dir) {
		assert getBoard() != null : "Board can't be null when moving";

		System.out.println("Saving Player Before Move. " + getPlayer());

		Tile player_tile = getPlayer().getTile();
		Direction player_orientation = getPlayer().getDirection();

		Sprite food = checkFood(dir);

		super.movePlayer(dir);

		if (player_tile != getPlayer().getTile()) {
			savePlayer(dir, player_orientation, food);
		}
	}

	private void savePlayer(Direction dir, Direction player_orientation, Sprite food) {
		try {
			System.out.println("Saving Player After Move. " + getPlayer());
			if (food != null && food.getSpriteType() == SpriteType.FOOD) {
				moves.add(new PlayerMoves(getPlayer(), getPlayer().getTile(), dir, true,
				        player_orientation));
			} else
				moves.add(new PlayerMoves(getPlayer(), getPlayer().getTile(), dir, false,
				        player_orientation));
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("####Saving Player: " + e.getLocalizedMessage()
			        + "\ncurrentcontent: " + getPlayer());
		}
	}

	private Sprite checkFood(Direction dir) {
	    Tile target = getBoard().tileAtDirection(getPlayer().getTile(), dir);

		// if top sprite on tile target is food, save food sprite before moving player
		Sprite food = null;
		while (target.topSprite() != null) {
			try {
				food = target.topSprite();
				if (food.getSpriteType() == SpriteType.FOOD) {
					System.out.println("Saving Food Move. " + food);
					moves.add(new FoodMoves(food, food.getTile(), ((Food) food).getPoints()));
				}
				break;
			} catch (NullPointerException e) {
				e.printStackTrace();
				System.out.println("####Saving Food: " + e.getLocalizedMessage()
				        + "\ncurrentcontent: " + getPlayer());
			}
		}
	    return food;
    }


	@Override
	public void moveGhost(Ghost theGhost, Direction dir) {
		// System.out.println("Saving Ghost Move of " + theGhost.hashCode() + " at "
		// + theGhost.getTile() + " " + dir);
		Tile ghostTile = theGhost.getTile();
		super.moveGhost(theGhost, dir);
		if (ghostTile != theGhost.getTile()) {
			try {
				moves.add(new GhostMoves(theGhost, theGhost.getTile(), dir));
			} catch (NullPointerException e) {
				e.printStackTrace();
				System.out.println("####Saving Ghost: " + e.getLocalizedMessage()
				        + "\ncurrentcontent: " + theGhost);
			}
		}
	}
}
