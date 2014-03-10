package main.java.org.jpacman.framework.model;

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
		System.out.println(".. From UndoableGame.");
		System.out.println(".. Loops");
		// Debug Info:
		System.out.println(".. Player Points: " + getPlayer().getPoints() + " Points Eaten: "
		        + getPointManager().getFoodEaten() + " Food in Game: "
		        + getPointManager().totalFoodInGame());
		System.out.println("PRE Loop:moves: " + moves.size());

		Moves m;
		m = moves.peek();
		// Peek deque for Moves and undo moves until a player element is found.
		while (moves.isEmpty() == false) {
			// && !((m = moves.pop()).getSprite() instanceof Player)) {
			// && !((m = moves.peekFirst()).getSprite() instanceof Player)
			switch (m.getSprite().getSpriteType()) {
				case FOOD:
					// Undo a Food Movement (if present)
					if (m != null && m.getSprite() instanceof Food) {
						System.out.println("Retriveing Food.");
						Food food = (Food) m.getSprite();
						Tile target = (Tile) m.getTile();

						getPointManager().consumePointsOnBoard(getPlayer(), -(food.getPoints()));
						getBoard().put(food, target.getX(), target.getY());
						moves.pop();
					}
					break;
				case GHOST:
					// TODO: Incomplete. Similar to PLAYER CASE.
					break;
					
				case EMPTY:
				case PLAYER:
					System.out.println("Retriveing Player.");

					Direction playerDir = ((PlayerMoves) m).reverseDirection();

					super.movePlayer(playerDir);
					moves.pop();

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
		System.out.println("POST Loop:moves: " + moves.size());
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
		
		// The deque should store all othe Sprite Moves before a Player Move.

		Tile target = getBoard().tileAtDirection(getPlayer().getTile(), dir);

		int preX = getPlayer().getTile().getX();
		int preY = getPlayer().getTile().getY();

		// Save the Food Sprite if it is about to be consumed.
		Sprite food = null;
		Tile foodTile = null;
		try {
			food = target.topSprite();
			foodTile = food.getTile();
		} catch (NullPointerException npe) {
			// e.printStackTrace();
			System.out.println("####Saving Food/FoodTile: " + npe.getLocalizedMessage()
			        + "\ncurrentcontent: " + getPlayer());
		}

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
			int pts = 0;

			moves.push(new PlayerMoves(getPlayer(), getPlayer().getTile(), dir, pts
			        - getPlayer().getPoints()));

			if (food != null && foodTile != null && food.getSpriteType() == SpriteType.FOOD) {
				Food f = (Food) food;
				pts = f.getPoints();
				System.out.println("Saving Food Move.");

				moves.push(new FoodMoves(food, foodTile));
			}
		} catch (NullPointerException npe) {
			// e.printStackTrace();
			System.out.println("#### Player not saved. Player: " + npe.getLocalizedMessage()
			        + "\ncurrentcontent: " + getPlayer());
		}

	}

	@Override
	public void moveGhost(Ghost theGhost, Direction dir) {
		// int g = getGhosts().indexOf(theGhost);
		// Ghost preGhost = getGhosts().get(g);

		// TODO: Similar to Player case (without accouting for Food)
		super.moveGhost(theGhost, dir);

	}


}
