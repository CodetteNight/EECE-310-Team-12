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
		Moves m;
		// Peek deque for Moves and undo moves until a player element is found.
		while (moves.isEmpty() == false
		        && !((m = moves.peekFirst()).getSprite() instanceof Player)) {

			// && !((m = moves.peekFirst()).getSprite() instanceof Player)
			switch (m.getSprite().getSpriteType()) {
				case EMPTY:
					break;
				case FOOD:
					System.out.println("####Error: Retriveing Food.");
					break;
				case GHOST:
					System.out.println("Retriveing Ghost.");
					Ghost uGhost = (Ghost) ((GhostMoves) m).getSprite();
					Direction ghostDir = (Direction) ((GhostMoves) m).getDirection();
					// Direction undoDir = Direction(-ghostDir.getDx(), -ghostDir.getDy());
					Direction undoGhostDir = reverseDirection(ghostDir);
					super.moveGhost(uGhost, undoGhostDir);
					moves.pollFirst(); // Remove item from deque.

					break;
				case PLAYER:
					System.out.println("Retriveing Player.");

					Direction playerDir = (Direction) ((PlayerMoves) m).getDirection();
					//Direction undoDir = Direction(-playerDir.getDx(), -playerDir.getDy());

					Direction undoPlayerDir = reverseDirection(playerDir);
					super.movePlayer(undoPlayerDir);
					moves.poll();

					// Undo a Food Movement (if present)
					m = moves.peekFirst();
					if (m != null && m.getSprite() instanceof Food) {
						System.out.println("Retriveing Food.");
						Food food = (Food) m.getSprite();
						Tile target = (Tile) m.getTile();
						getPointManager().consumePointsOnBoard(getPlayer(), -(food.getPoints()));
						getBoard().put(food, target.getX(), target.getY());
						moves.poll();
					}

					// continue; // Exit Loop
					break;
				default:
					break;
			}
		}

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
		return dir;
	}

	@Override
	public void movePlayer(Direction dir) {
		assert getBoard() != null : "Board can't be null when moving";
		
		Tile target = getBoard().tileAtDirection(getPlayer().getTile(), dir);

		Sprite food = null;
		try {
			food = target.topSprite();
		if (food.getSpriteType() == SpriteType.FOOD) {
			System.out.println("Saving Food Move.");

				moves.add(new FoodMoves(food, food.getTile()));
		}
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("####Saving Food: " + e.getLocalizedMessage()
			        + "\ncurrentcontent: "
			        + getPlayer());
		}

		super.movePlayer(dir);

		try {
		System.out.println("Saving Player Move.");
			int pts = 0;
			if(food.getSpriteType() == SpriteType.FOOD){
				Food f = (Food) food;
				pts = f.getPoints();
			}
			moves.add(new PlayerMoves(getPlayer(), getPlayer().getTile(), dir, pts));
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("####Saving Player: " + e.getLocalizedMessage()
			        + "\ncurrentcontent: "
			        + getPlayer());
		}

	}

	@Override
	public void moveGhost(Ghost theGhost, Direction dir) {
		super.moveGhost(theGhost, dir);
		System.out.println("Saving Ghost Move.");
		try {
			moves.add(new GhostMoves(theGhost, theGhost.getTile(), dir));
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("####Saving Ghost: " + e.getLocalizedMessage()
			        + "\ncurrentcontent: "
			        + theGhost);
		}
	}

	/**
	 * Player intends to move towards tile already occupied: if there's food there, eat it.
	 * 
	 * @param p
	 *            The player
	 * @param currentSprite
	 *            who is currently occupying the tile.
	 */
	private void eatFood(Player player, Sprite currentSprite) {
		if (currentSprite instanceof Food) {
			Food food = (Food) currentSprite;
			getPointManager().consumePointsOnBoard(player, food.getPoints());
			// moves.add(currentSprite);
			food.deoccupy();
		}
	}

	/**
	 * Player intends to move towards an occupied tile: if there's a ghost there, the game is over.
	 * 
	 * @param p
	 *            The player
	 * @param currentSprite
	 */
	private void dieIfGhost(Player p, Sprite currentSprite) {
		if (currentSprite instanceof Ghost) {
			p.die();
		}
	}

	/**
	 * Check if there's room on the target tile for another sprite.
	 * 
	 * @param target
	 *            Tile to be occupied by another sprite.
	 * @return true iff target tile can be occupied.
	 */
	private boolean tileCanBeOccupied(Tile target) {
		assert target != null : "PRE: Argument can't be null";
		Sprite currentOccupier = target.topSprite();
		return currentOccupier == null || currentOccupier.getSpriteType() != SpriteType.WALL;
	}

}
