package main.java.org.jpacman.framework.model;

import java.util.ArrayDeque;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Food;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;
import org.jpacman.framework.model.IBoardInspector.SpriteType;
import org.jpacman.framework.model.Player;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class UndoableGame extends Game implements IGameInteractorWithUndo {

	private static ArrayDeque<Moves> moves = new ArrayDeque<Moves>();
	
	@Override
	public void undo() {

		// test case S7_21, S7_22, S7_23, S7_24: Undo Ghost Moves (WORKS!)
		while (!moves.isEmpty()
		        && moves.peekLast().getSprite().getSpriteType() == SpriteType.GHOST) {
			Moves currMoves = moves.peekLast();
			Direction revDir = reverseDirection(((GhostMoves) currMoves).getDirection());
			super.moveGhost((Ghost) currMoves.getSprite(), revDir);
			System.out.println("Removing Ghost Move of "
			        + ((Ghost) currMoves.getSprite()).hashCode() + " at "
			        + ((Ghost) currMoves.getSprite()).getTile() + " from " + currMoves.getTile()
			        + " " + revDir);
			moves.removeLast();
		}

		if (!moves.isEmpty()
		        && moves.peekLast().getSprite().getSpriteType() == SpriteType.PLAYER) {
			Moves currMoves = moves.peekLast();
			Direction revDir = reverseDirection(((PlayerMoves) moves.peekLast()).getDirection());
			super.movePlayer(revDir);
			System.out.println("Removing Player Move of "
			        + ((Player) currMoves.getSprite()).hashCode() + " at "
			        + ((Player) currMoves.getSprite()).getTile() + " from " + currMoves.getTile()
			        + " " + revDir);
			moves.removeLast();

			if (!moves.isEmpty()
			        && moves.peekLast().getSprite().getSpriteType() == SpriteType.FOOD) {
				currMoves = moves.peekLast();
				currMoves.getSprite().occupy(currMoves.getTile());
				super.getPointManager().consumePointsOnBoard(getPlayer(), -10);
				System.out.println("Removing Food Move of "
				        + ((Food) currMoves.getSprite()).hashCode() + " at "
				        + ((Food) currMoves.getSprite()).getTile() + " from "
				        + currMoves.getTile());
				moves.removeLast();
			}
		}

		notifyViewers();
		return;

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

	@Override
	public void movePlayer(Direction dir) {
		assert getBoard() != null : "Board can't be null when moving";

		Tile target = getBoard().tileAtDirection(getPlayer().getTile(), dir);

		// if top sprite on tile target is food, save food sprite before moving player
		Sprite food = null;
		while (target.topSprite() != null) {
			try {
				food = target.topSprite();
				if (food.getSpriteType() == SpriteType.FOOD) {
					System.out.println("Saving Food Move.");
					moves.add(new FoodMoves(food, food.getTile()));
				}
				break;
			} catch (NullPointerException e) {
				e.printStackTrace();
				System.out.println("####Saving Food: " + e.getLocalizedMessage()
				        + "\ncurrentcontent: " + getPlayer());
			}
		}

		super.movePlayer(dir);

		try {
			System.out.println("Saving Player Move.");
			int pts = 0;
			while (food != null) {
				if (food.getSpriteType() == SpriteType.FOOD) {
					Food f = (Food) food;
					pts = f.getPoints();
					moves.add(new PlayerMovesOverFood(getPlayer(), getPlayer().getTile(), dir,
					        pts));
				}
				break;
			}
			if (food == null)
				moves.add(new PlayerMoves(getPlayer(), getPlayer().getTile(), dir));
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("####Saving Player: " + e.getLocalizedMessage()
			        + "\ncurrentcontent: " + getPlayer());
		}

	}

	@Override
	public void moveGhost(Ghost theGhost, Direction dir) {
		System.out.println("Saving Ghost Move of " + theGhost.hashCode() + " at "
		        + theGhost.getTile() + " " + dir);
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
