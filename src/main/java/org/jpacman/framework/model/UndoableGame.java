package main.java.org.jpacman.framework.model;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Food;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;
import org.jpacman.framework.model.IBoardInspector.SpriteType;
import org.jpacman.framework.model.Sprite;
import org.jpacman.framework.model.Tile;

public class UndoableGame extends Game implements IGameInteractorWithUndo {

	private static ArrayDeque<Moves> moves = new ArrayDeque<Moves>();
	
	@Override
	public void undo() {

		// test case: Reduce points of player upon undo
		while (!moves.isEmpty()
		        && moves.peekFirst().getSprite().getSpriteType() != SpriteType.FOOD) {
			moves.remove();
		}

		if (moves.peekFirst() != null
		        && moves.peekFirst().getSprite().getSpriteType() == SpriteType.FOOD) {
			try {
				int foodPts = ((Food) moves.getFirst().getSprite()).getPoints();
				getPointManager().consumePointsOnBoard(getPlayer(), -foodPts);
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				System.out.println("####Reducing points: " + e.getLocalizedMessage()
				        + "\ncurrentcontent: " + getPlayer());
			}
		}

		// Food food = new Food();
		// getPointManager().consumePointsOnBoard(getPlayer(), -food.getPoints());

		/* Commented this out to test other test cases
		 * 
		Moves m;
		// Peek deque for Moves and undo moves until a player element is found.
		// while (moves.isEmpty() == false
		// && !((m = moves.peekFirst()).getSprite() instanceof Player)) {
		//
		// // && !((m = moves.peekFirst()).getSprite() instanceof Player)
		// switch (m.getSprite().getSpriteType()) {
		// case EMPTY:
		// break;
		// case FOOD:
		// System.out.println("####Error: Retriveing Food.");
		// break;
		// case GHOST:
		// System.out.println("Retriveing Ghost.");
		// Ghost uGhost = (Ghost) ((GhostMoves) m).getSprite();
		// Direction ghostDir = (Direction) ((GhostMoves) m).getDirection();
		// // Direction undoDir = Direction(-ghostDir.getDx(), -ghostDir.getDy());
		// Direction undoGhostDir = reverseDirection(ghostDir);
		// super.moveGhost(uGhost, undoGhostDir);
		// moves.pollFirst(); // Remove item from deque.
		//
		// break;
		//
		// // if the code works as expected, it should enter this case statement
		// case PLAYER:
		// System.out.println("Retriveing Player.");
		//
		// Direction playerDir = (Direction) ((PlayerMoves) m).getDirection();
		// //Direction undoDir = Direction(-playerDir.getDx(), -playerDir.getDy());
		//
		// Direction undoPlayerDir = reverseDirection(playerDir);
		// super.movePlayer(undoPlayerDir);
		// moves.poll();
		//
		// // Undo a Food Movement (if present)
		// m = moves.peekFirst();
		// if (m != null && m.getSprite() instanceof Food) {
		// System.out.println("Retriveing Food.");
		// Food food = (Food) m.getSprite();
		// Tile target = (Tile) m.getTile();
		// getPointManager().consumePointsOnBoard(getPlayer(), -(food.getPoints()));
		// getBoard().put(food, target.getX(), target.getY());
		// moves.poll();
		// }
		//
		// // continue; // Exit Loop
		// break;
		// default:
		// break;
		// }
		// }
		 * 
		 */

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
		super.moveGhost(theGhost, dir);
		System.out.println("Saving Ghost Move.");
		try {
			moves.add(new GhostMoves(theGhost, theGhost.getTile(), dir));
		} catch (NullPointerException e) {
			e.printStackTrace();
			 System.out.println("####Saving Ghost: " + e.getLocalizedMessage()
			 + "\ncurrentcontent: "
			 + theGhost);
		}
	}

}
