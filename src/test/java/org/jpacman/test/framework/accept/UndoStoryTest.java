package org.jpacman.test.framework.accept;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.IBoardInspector;
import org.jpacman.framework.model.Tile;
import org.jpacman.framework.ui.PacmanInteraction.MatchState;
import org.jpacman.framework.ui.UndoablePacman;
import org.junit.Test;

public class UndoStoryTest extends MovePlayerStoryTest {

	/**
	 * The main (Swing) user interface.
	 */
	private UndoablePacman theUI;

	@Override
	public UndoablePacman makeUI() {
		theUI = new UndoablePacman();
		return theUI;

	}

	@Override
	protected UndoablePacman getUI() {
		return theUI;
	}

	@Test
	public void test_S7_11_UndoPlayerMoves() {
		getEngine().start();
		Tile playerTile = getPlayer().getTile();

		// test undo up
		getEngine().up(); // Pacman moves
		getUI().undo();
		// check the pacman has returned to original tile
		assertEquals(playerTile, getPlayer().getTile());

		// test undo left
		getEngine().left();
		getUI().undo();
		assertEquals(playerTile, getPlayer().getTile());

		// test undo right
		getEngine().right();
		getUI().undo();
		assertEquals(playerTile, getPlayer().getTile());

		// moving down causes the pacman to move towards a wall
		// place pacman at a tile so that undo pacman down can be tested
		getEngine().up();
		playerTile = getPlayer().getTile();
		getEngine().down();
		getUI().undo();
		assertEquals(playerTile, getPlayer().getTile());
		
	}

	@Test
	public void test_S7_12_UndoPlayerEats() {
		getEngine().start();

		Tile previousTile = getPlayer().getTile();
		// the food is one tile to the left of Pacman
		Tile foodTile = tileAt(0, 1);

		getEngine().left(); // Pacman moves left
		assertEquals(getPlayer().getTile(), foodTile);

		getUI().undo();

		assertEquals(getPlayer().getTile(), previousTile);

		// check food reappears on the original tile after undo
		assertEquals(IBoardInspector.SpriteType.FOOD, foodTile.topSprite().getSpriteType());
	}

	@Test
	public void test_S7_13_UndoPlayerPoints() {
		getEngine().start();
		assertEquals(getPlayer().getPoints(), 0);
		int pointsAtBeginning = getPlayer().getPoints();

		getEngine().left(); // Pacman moves left to collect non-zero point value
		assertThat("Points", getPlayer().getPoints(), greaterThan(0));

		getUI().undo();

		assertEquals(pointsAtBeginning, getPlayer().getPoints());
	}

	@Test
	public void test_S7_14_UndoPlayerPointsWhenEmpty() {
		getEngine().start();
		assertEquals(getPlayer().getPoints(), 0);

		int pointsAtBeginning = getPlayer().getPoints();

		getUI().undo();

		// points remain zero
		assertEquals(pointsAtBeginning, getPlayer().getPoints());
	}

	@Test
	public void test_S7_15_UndoPlayerMultipleMoves() {
		getEngine().start();
		Tile playerTile = getPlayer().getTile();

		// check that multiple pacman movements followed by multiple undo calls returns the player
		// to the original position
		getEngine().up();
		getEngine().right();

		getUI().undo();
		getUI().undo();

		assertEquals(getPlayer().getTile(), playerTile);
	}

	@Test
	public void test_S7_17_UndoPlayerMovesAgainstWall() {
		getEngine().start();
		Tile playerTile = getPlayer().getTile();

		// these two movements move the pacman against a wall
		getEngine().left();
		getEngine().down();

		getUI().undo(); // only one undo because movement into wall should not be saved

		// pacman should be at the original location before the two movement inputs
		assertEquals(getPlayer().getTile(), playerTile);

	}

	@Test
	public void test_S7_21_UndoGhostMoves() {
		getEngine().start();

		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);
		getUI().getGame().moveGhost(theGhost(), Direction.RIGHT);
		assertNotSame(ghostTile, theGhost().getTile());

		getUI().undo();

		// ghost position should revert to its position at last player movement or null deqeue
		assertEquals("Ghost Tile", theGhost().getTile(), ghostTile);
	}

	@Test
	public void test_S7_22_UndoGhostMovesOverFood() {
		getEngine().start();

		Tile foodTile = tileAt(2, 0);

		// Check that a ghost sprite moves on top of a food sprite (does not eat it), but has higher
		// image depth
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.UP);
		assertEquals(foodTile, theGhost().getTile());
		assertEquals(IBoardInspector.SpriteType.GHOST, foodTile.topSprite().getSpriteType());

		getUI().undo();

		// after undo, ghost position reverts, food sprite reappears
		assertEquals(ghostTile, theGhost().getTile());
		assertEquals(IBoardInspector.SpriteType.FOOD, foodTile.topSprite().getSpriteType());
	}

	@Test
	public void test_S7_23_UndoGhostLeavesFoodCell() {
		getEngine().start();

		Tile foodTile = tileAt(2, 0);
		// Ghost movement onto a piece of food and player movement to set up test (allow undo of
		// ghost movement off
		// food and undo stop at player movement)
		//Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.UP);
		Tile ghostTile = theGhost().getTile();
		assertEquals(ghostTile, foodTile);
		getEngine().left();

		// Ghost movement off a piece of food
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);

		getEngine().right();
		getUI().undo();

		// The game should reverse the Ghost movement and display the ghost where the food was
		// (ghost does not eat food)
		assertEquals(ghostTile, theGhost().getTile());
		assertEquals(IBoardInspector.SpriteType.GHOST, foodTile.topSprite().getSpriteType());
	}

	@Test
	public void test_S7_24_UndoGhostMovesAgainstWall() {
		getEngine().start();
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.UP);

		// Ghost movement into a wall
		Tile ghostTileNew = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.RIGHT);

		// Check that the Ghost movement was into a wall
		assertEquals(ghostTileNew, theGhost().getTile());

		getUI().undo();

		// Ghost should be at last position before player movement or null deqeue
		assertEquals(theGhost().getTile(), ghostTile);
	}

	@Test
	public void test_S7_31_UndoAtStart() {
		// game starts and ghost moves, but player does not move
		getEngine().start();
		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);

		getUI().undo();

		// Pacman should stay on the same cell
		assertEquals("Player Unchanged", getPlayer().getTile(), playerTile);

		// ghost should move to the original position at the start of the game.
		assertEquals("Ghost undone", theGhost().getTile(), ghostTile);
	}

	@Test
	public void test_S7_41_UndoAtEndLose() {
		getEngine().start();
		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();

		getPlayer().die();
		assertFalse(getPlayer().isAlive());

		getEngine().right(); // Test player movement to undo

		getUI().undo();

		// Game should not do any undo movements.
		assertEquals(playerTile, getPlayer().getTile());
		assertEquals(ghostTile, theGhost().getTile());

		// The player should still be dead
		assertFalse(getPlayer().isAlive());
	}

	@Test
	public void test_S7_42_UndoAtEndWins() {
		getEngine().start();

		// moves to win the game (collect all food)
		getEngine().left();
		getEngine().right();
		getEngine().up();
		getEngine().right();

		assertTrue(getUI().getGame().getPointManager().allEaten());

		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();

		getUI().undo();

		// Game should not undo any movements.
		assertEquals("Player", playerTile, getPlayer().getTile());
		assertEquals("Ghost", ghostTile, theGhost().getTile());
	}

	@Test
	public void test_S7_51_UndoAtSuspend() {
		getEngine().start();
		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();

		getEngine().left();

		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);
		getUI().getGame().moveGhost(theGhost(), Direction.LEFT);

		Tile playerTileNew = getPlayer().getTile();
		Tile ghostTileNew = theGhost().getTile();
		int playerPoints = getPlayer().getPoints();

		// check that ghosts and player have moved before pausing the game
		assertNotSame(playerTileNew, playerTile);
		assertNotSame(ghostTileNew, ghostTile);
		assertThat(playerPoints, greaterThan(0));
		getEngine().stop();

		getUI().undo();

		// The game should undo player and ghost movements
		assertEquals(getEngine().getCurrentState(), MatchState.PAUSING);
		assertEquals("Player", playerTile, getPlayer().getTile());
		assertEquals("Ghost", ghostTile, theGhost().getTile());

		// and the player points are reduced
		assertEquals(0, getPlayer().getPoints());
	}

	@Test
	public void test_S7_61_UndoButtonVisible() {
		getEngine().start();

		// The game should show the "UNDO" button
		getUI().eventHandler().undo();
	}

}
