package main.java.org.jpacman.framework.model;

import java.util.ArrayDeque;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;
import org.jpacman.framework.model.Tile;

public class UndoableGame extends Game implements IGameInteractorWithUndo {

	private ArrayDeque<Moves> moves = new ArrayDeque<Moves>();
	
	@Override
	public void undo() {
		System.out.println(".. From UndoableGame.");
		System.out.println(".. Loops");

		Moves m;

		System.out.println("PRE:moves: "+moves.size());

		m = moves.peek();
		// Peek deque for Moves and undo moves until a player element is found.
		while (moves.isEmpty() == false) {
			// && !((m = moves.pop()).getSprite() instanceof Player)) {
			// && !((m = moves.peekFirst()).getSprite() instanceof Player)
			switch (m.getSprite().getSpriteType()) {
				case FOOD:
					break;
				case GHOST:
					break;
					
				case EMPTY:
				case PLAYER:
					System.out.println("Retriveing Player.");

					Direction playerDir = ((PlayerMoves) m).reverseDirection();

					super.movePlayer(playerDir);
					moves.pop();

					// Undo a Food Movement (if present)
					// m = moves.peekFirst();
					// if (m != null && m.getSprite() instanceof Food) {
					// System.out.println("Retriveing Food.");
					// Food food = (Food) m.getSprite();
					// Tile target = (Tile) m.getTile();
					// getPointManager().consumePointsOnBoard(getPlayer(), -(food.getPoints()));
					// getBoard().put(food, target.getX(), target.getY());
					// moves.pop();
					// }

					System.out.println("POST:moves: " + moves.size());
					return; // Exit Loop
					// break;
				default:
					System.out.println("Nothing to retrive.");
					break;
			}
			m = moves.peek();
			notifyViewers();
		}
		System.out.println("POST:moves: "+moves.size());
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

		int pts = getPlayer().getPoints();
		int preX = getPlayer().getTile().getX();
		int preY = getPlayer().getTile().getY();

		super.movePlayer(dir);

		// Post Movement.
		int postX = getPlayer().getTile().getX();
		int postY = getPlayer().getTile().getY();

		if (preX == postX && preY == postY) {
			// Player did not complete move.
			System.out.println("###1 Player not saved. "
			        + "Player did not complete move. Player: " + getPlayer());
			return;
		}

		try {
			System.out.println("Saving Player Move.");
			moves.push(new PlayerMoves(getPlayer(), getPlayer().getTile(), dir, pts
			        - getPlayer().getPoints()));
		} catch (NullPointerException e) {
			// e.printStackTrace();
			System.out.println("#### Player not saved. Player: " + e.getLocalizedMessage()
			        + "\ncurrentcontent: " + getPlayer());
		}

	}

	@Override
	public void moveGhost(Ghost theGhost, Direction dir) {
		// int g = getGhosts().indexOf(theGhost);
		// Ghost preGhost = getGhosts().get(g);

		super.moveGhost(theGhost, dir);

	}


}
