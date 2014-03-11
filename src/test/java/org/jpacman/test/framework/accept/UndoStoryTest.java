package test.java.org.jpacman.test.framework.accept;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import main.java.org.jpacman.framework.ui.UndoablePacman;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.IBoardInspector;
import org.jpacman.framework.model.Tile;
import org.jpacman.framework.ui.PacmanInteraction.MatchState;
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
		// Given the game has started,
		getEngine().start();
		Tile playerTile = getPlayer().getTile();

		// and my Pacman has made a movement (up);
		getEngine().up();

		// When the user presses the "Undo" button;
		getUI().undo();

		// Then my Pacman should revert to its previous cell.
		assertEquals(getPlayer().getTile(), playerTile);

		// test undo left
		getEngine().start();
		getEngine().left();
		getUI().undo();
		assertEquals(getPlayer().getTile(), playerTile);
	}

	@Test
	public void test_S7_12_UndoPlayerEats() {
		// Given the game has started,
		getEngine().start();

		Tile previousTile = getPlayer().getTile();
		// and the food is one tile to the left of Pacman
		Tile foodTile = tileAt(0, 1);

		// and my Pacman moves to the cell that contains food;
		getEngine().left();
		assertEquals(getPlayer().getTile(), foodTile);

		// When the user presses the "Undo" button;
		getUI().undo();

		// Then my Pacman should revert to its previous cell.
		assertEquals(getPlayer().getTile(), previousTile);

		// and the food re-appears on that cell.
		assertEquals(IBoardInspector.SpriteType.FOOD, foodTile.topSprite().getSpriteType());
	}

	@Test
	public void test_S7_13_UndoPlayerPoints() {
		// Given the game has started and my Pacman has no points,
		getEngine().start();
		assertEquals(getPlayer().getPoints(), 0);
		int previousPoints = getPlayer().getPoints();

		// and my player eats a food and get points from it
		getEngine().left();
		assertThat("Points", getPlayer().getPoints(), greaterThan(0));

		// When the user presses the "Undo" button;
		getUI().undo();

		// I lose the points for that cell,
		assertEquals(previousPoints, getPlayer().getPoints());
	}

	@Test
	public void test_S7_14_UndoPlayerPointsWhenEmpty() {
		// Given the game has started, and player has zero points
		getEngine().start();
		assertEquals(getPlayer().getPoints(), 0);

		int previousPoints = getPlayer().getPoints();
		// when player undo
		getUI().undo();

		// points remain zero
		assertEquals(previousPoints, getPlayer().getPoints());
	}

	@Test
	public void test_S7_15_UndoPlayerMultipleMoves() {
		// Given the game has started,
		getEngine().start();
		Tile playerTile = getPlayer().getTile();

		// and my Pacman has made two consecutive movements;
		getEngine().up();
		// Tile playerTile2 = getPlayer().getTile();
		getEngine().right();

		// When the user presses the "Undo" button twice;
		getUI().undo();

		// Then my Pacman should revert to its previous cell,
		// assertEquals(getPlayer().getTile(), playerTile2);
		getUI().undo();

		// Then its original cell.
		assertEquals(getPlayer().getTile(), playerTile);
	}

	@Test
	public void test_S7_17_UndoPlayerMovesAgainstWall() {
		// Given the game has started,
		getEngine().start();
		Tile playerTile = getPlayer().getTile();

		// and a player makes a move
		getEngine().left();

		// then moves towards a wall
		getEngine().down();

		// When the user presses the "Undo" button;
		getUI().undo();

		// Then the game undoes to the movement before player moves towards the wall
		assertEquals(getPlayer().getTile(), playerTile);

	}

	@Test
	public void test_S7_21_UndoGhostMoves() {
		// Given the game has started,
		getEngine().start();

		// and a Ghost and player has made several movements
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);
		getUI().getGame().moveGhost(theGhost(), Direction.RIGHT);
		assertNotSame(ghostTile, theGhost().getTile());

		// When the user presses the "Undo" button;
		getUI().undo();

		// Then the game should reverse the previous Ghost movement
		assertEquals("Ghost Tile", theGhost().getTile(), ghostTile);
	}

	@Test
	public void test_S7_22_UndoGhostMovesOverFood() {
		// Given the game has started
		getEngine().start();

		Tile foodTile = tileAt(2, 0);

		// and a Ghost has moved over food (ghost does not eat food)
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.UP);
		assertEquals(foodTile, theGhost().getTile());
		assertEquals(IBoardInspector.SpriteType.GHOST, foodTile.topSprite().getSpriteType());

		// When a user presses the "Undo" button
		getUI().undo();

		// Then the game should reverse the Ghost movement and display food where the ghost was
		assertEquals(ghostTile, theGhost().getTile());
		assertEquals(IBoardInspector.SpriteType.FOOD, foodTile.topSprite().getSpriteType());
	}

	@Test
	public void test_S7_23_UndoGhostLeavesFoodCell() {
		// Given the game has started
		getEngine().start();

		Tile foodTile = tileAt(2, 0);
		getUI().getGame().moveGhost(theGhost(), Direction.UP);
		Tile ghostTile = theGhost().getTile();
		getEngine().left();

		// and a Ghost has moved off a piece of food
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);

		// When a user presses the "Undo" button
		getUI().undo();

		// Then the game should reverse the Ghost movement and display the ghost where the food was
		// (ghost does not eat food)
		assertEquals(ghostTile, theGhost().getTile());
		assertEquals(IBoardInspector.SpriteType.GHOST, foodTile.topSprite().getSpriteType());
	}

	@Test
	public void test_S7_24_UndoGhostMovesAgainstWall() {
		// Given the game has started,
		getEngine().start();
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.UP);

		// and a Ghost and player has made several movements
		Tile ghostTileNew = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.RIGHT);

		assertEquals(ghostTileNew, theGhost().getTile());

		// When the user presses the "Undo" button;
		getUI().undo();

		// Then the game should reverse the previous Ghost movement
		assertEquals(theGhost().getTile(), ghostTile);
	}

	@Test
	public void test_S7_31_UndoAtStart() {
		// Given the game has started,
		getEngine().start();
		Tile playerTile = getPlayer().getTile();

		// and a Ghost has made movements
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);

		// When the user presses the "Undo" button;
		getUI().undo();

		// Then my Pacman will stay on the same cell,
		assertEquals("Player Unchanged", getPlayer().getTile(), playerTile);

		// and the ghost will move to the original position at the start of the game.
		assertEquals("Ghost undone", theGhost().getTile(), ghostTile);
	}

	@Test
	public void test_S7_41_UndoAtEndLose() {
		// Undo at End of Game after Loosing.

		// Given the game has started,
		getEngine().start();
		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();

		// and the player dies
		getPlayer().die();
		assertFalse(getPlayer().isAlive());

		getEngine().right(); // Player Looses, Game ends.

		// When the user presses the "Undo" button;
		getUI().undo();

		// Then the game will not do any undo movements.
		assertEquals(playerTile, getPlayer().getTile());
		assertEquals(ghostTile, theGhost().getTile());

		// and the player is still dead
		assertFalse(getPlayer().isAlive());
	}

	@Test
	public void test_S7_42_UndoAtEndWins() {
		// Undo at End of Game after Winning.

		// Given the game has started,
		getEngine().start();
		getEngine().left(); // eat first food
		getEngine().right(); // go back
		getEngine().up(); // move next to final food

		getEngine().right();

		assertTrue(getUI().getGame().getPointManager().allEaten());

		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();

		// When the user presses the "Undo" button;
		getUI().undo();

		// Then the game will not do any undo movements.
		assertEquals("Player", playerTile, getPlayer().getTile());
		assertEquals("Ghost", ghostTile, theGhost().getTile());
	}

	/**
	 * player is not allowed to make undo movements during suspended game // @Test // public void
	 * test_S7_51_UndoAtSuspend() { // // given // // Given the game has started, //
	 * getEngine().start(); // // // Player Makes some movements // getEngine().left(); // eat first
	 * food // // // Ghost makes some movements // getUI().getGame().moveGhost(theGhost(),
	 * Direction.DOWN); // // Tile playerTile = getPlayer().getTile(); // Tile ghostTile =
	 * theGhost().getTile(); // int foodRemaining =
	 * getUI().getGame().getPointManager().totalFoodInGame(); // int playerPoints =
	 * getPlayer().getPoints(); // // getEngine().stop(); // Suspend Game // // // when // // When
	 * the user presses the "Undo" button; // getUI().undo(); // // // then // // Then the game
	 * should not be able to undo any movements // // Player and Ghost positions unchanged. //
	 * assertEquals(getEngine().getCurrentState(), MatchState.PAUSING); //
	 * assertEquals(getPlayer().getTile(), playerTile); // assertEquals(theGhost().getTile(),
	 * ghostTile); // // // Player Points and Available Food unchanged // assertEquals(playerPoints,
	 * getPlayer().getPoints()); // assertEquals(foodRemaining,
	 * getUI().getGame().getPointManager().totalFoodInGame()); // }
	 */

	/* Player is allowed to make undo movement during suspended game */
	@Test
	public void test_S7_51_UndoAtSuspend() {
		// Given the game has started,
		getEngine().start();
		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();

		// Player Makes some movements
		getEngine().left(); // eat first food

		// Ghost makes some movements
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);
		getUI().getGame().moveGhost(theGhost(), Direction.LEFT);

		Tile playerTileNew = getPlayer().getTile();
		Tile ghostTileNew = theGhost().getTile();
		int playerPoints = getPlayer().getPoints();

		assertNotSame(playerTileNew, playerTile);
		assertNotSame(ghostTileNew, ghostTile);
		assertThat(playerPoints, greaterThan(0));
		getEngine().stop(); // Suspend Game

		// When the user presses the "Undo" button;
		getUI().undo();

		// Then the game will undo player and ghost movements
		assertEquals(getEngine().getCurrentState(), MatchState.PAUSING);
		assertEquals("Player", playerTile, getPlayer().getTile());
		assertEquals("Ghost", ghostTile, theGhost().getTile());

		// and the player points are reduced
		assertEquals(0, getPlayer().getPoints());
	}

	@Test
	public void test_S7_61_UndoButtonVisible() {
		// Given the engine has started,
		getEngine().start();

		// When the game window is opened;

		// Then the game should show the "UNDO" button
		getUI().eventHandler().undo();
	}

}
