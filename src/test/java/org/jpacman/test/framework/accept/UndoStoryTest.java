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
		// given
		// Given the game has started,
		getEngine().start();
		Tile playerTile = getPlayer().getTile();

		// and my Pacman has made a movement;
		getEngine().up();

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then my Pacman should revert to its previous cell.
		assertEquals(getPlayer().getTile(), playerTile);

		getEngine().down();
		getUI().undo();
		assertEquals(getPlayer().getTile(), playerTile);

		getEngine().right();
		getUI().undo();
		assertEquals(getPlayer().getTile(), playerTile);

		getEngine().left();
		getUI().undo();
		assertEquals(getPlayer().getTile(), playerTile);
	}

	@Test
	public void test_S7_12_UndoPlayerEats() {
		// given
		// Given the game has started,
		getEngine().start();

		Tile previousTile = getPlayer().getTile();
		Tile foodTile = tileAt(0, 1);

		// and my Pacman moves to a cell that contains food;
		getEngine().left();
		assertEquals(getPlayer().getTile(), foodTile);
		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then my Pacman should revert to its previous cell.
		assertEquals(getPlayer().getTile(), previousTile);

		// and the food re-appears on that cell.
		assertEquals(IBoardInspector.SpriteType.FOOD, foodTile.topSprite().getSpriteType());
	}

	@Test
	public void test_S7_13_UndoPlayerPoints() {
		// given
		// Given the game has started,
		getEngine().start();
		int previousPoints = getPlayer().getPoints();

		// and my player eats a food and get points form it
		getEngine().left();
		assertThat(getPlayer().getPoints(), greaterThan(0));

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// I lose the points for that cell,
		System.out.println(" $$$: " + previousPoints);
		System.out.println(" $$$: " + getPlayer().getPoints());
		assertEquals(previousPoints, getPlayer().getPoints());
	}

	@Test
	public void test_S7_14_UndoPlayerPointsWhenEmpty() {
		// given
		// Given the game has started, and player has zero points
		getEngine().start();

		int previousPoints = getPlayer().getPoints();
		// when player undo
		getUI().undo();

		// points remain zero
		assertEquals(previousPoints, getPlayer().getPoints());
	}

	@Test
	public void test_S7_15_UndoPlayerMultipleMoves() {
		// given
		// Given the game has started,
		getEngine().start();
		Tile playerTile = getPlayer().getTile();

		// and my Pacman has made two consecutive movements;
		getEngine().left();
		getEngine().left();
		// when
		// When the user presses the "Undo" button twice;
		getUI().undo();
		getUI().undo();

		// then
		// Then my Pacman should revert to its original cell.
		assertEquals(getPlayer().getTile(), playerTile);
	}

	@Test
	public void test_S7_17_UndoPlayerMovesAgainstWall() {
		// given
		// Given the game has started,
		getEngine().start();
		Tile playerTile = getPlayer().getTile();
		getEngine().up();

		// and a player moves towards a wall
		Tile playerTileWall = getPlayer().getTile();
		getEngine().left();

		assertEquals(playerTileWall, getPlayer().getTile());

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then the game undo to the movement before player moves towards the wall
		assertEquals(getPlayer().getTile(), playerTile);
	}

	@Test
	public void test_S7_21_UndoGhostMoves() {
		// given
		// Given the game has started,
		getEngine().start();

		// and a Ghost and player has made several movements
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);
		assertNotSame(ghostTile, theGhost().getTile());

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then the game should reverse the previous Ghost movement
		assertEquals(theGhost().getTile(), ghostTile);
	}

	@Test
	public void test_S7_22_UndoGhostMovesOverFood() {
		// given
		// Given the game has started
		getEngine().start();

		Tile foodTile = tileAt(2, 0);

		// and a Ghost has moved over food (ghost does not eat food)
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.UP);
		assertEquals(foodTile, theGhost().getTile());
		assertEquals(IBoardInspector.SpriteType.GHOST, foodTile.topSprite().getSpriteType());

		// when
		// When a user presses the "Undo" button
		getUI().undo();

		// then
		// Then the game should reverse the Ghost movement and display food where the ghost was
		assertEquals(ghostTile, theGhost().getTile());
		assertEquals(IBoardInspector.SpriteType.FOOD, foodTile.topSprite().getSpriteType());
	}

	@Test
	public void test_S7_23_UndoGhostLeavesFoodCell() {
		// given
		// Given the game has started
		getEngine().start();

		Tile foodTile = tileAt(2, 0);
		getUI().getGame().moveGhost(theGhost(), Direction.UP);
		Tile ghostTile = theGhost().getTile();
		getEngine().left();

		// and a Ghost has moved off a piece of food
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);

		// when
		// When a user presses the "Undo" button
		getUI().undo();

		// then
		// Then the game should reverse the Ghost movement and display the ghost where the food was
		// (ghost does not eat food)
		assertEquals(ghostTile, theGhost().getTile());
		assertEquals(IBoardInspector.SpriteType.GHOST, foodTile.topSprite().getSpriteType());
	}

	@Test
	public void test_S7_24_UndoGhostMovesAgainstWall() {
		// given
		// Given the game has started,
		getEngine().start();
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.UP);

		// and a Ghost and player has made several movements
		Tile ghostTileNew = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.RIGHT);

		assertEquals(ghostTileNew, theGhost().getTile());

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then the game should reverse the previous Ghost movement
		assertEquals(theGhost().getTile(), ghostTile);
	}

	@Test
	public void test_S7_31_UndoAtStart() {
		// given
		// Given the game has started,
		getEngine().start();

		Tile playerTile = getPlayer().getTile();

		// and a Ghost has made movements
		Tile ghostTile = theGhost().getTile();
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then my Pacman will stay on the same cell,
		assertEquals(getPlayer().getTile(), playerTile);

		// and the ghost will move to the original position at the start of the game.
		assertEquals("Ghost undone", theGhost().getTile(), ghostTile);
	}

	@Test
	public void test_S7_41_UndoAtEnd() {
		// Undo at End of Game after Losing.

		// given
		// Given the game has started,
		getEngine().start();
		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();

		// and the player dies
		getPlayer().die();
		assertFalse(getPlayer().isAlive());

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then the game will not do any undo movements.
		assertEquals(playerTile, getPlayer().getTile());
		assertEquals(ghostTile, theGhost().getTile());

		// and the player is still dead
		assertFalse(getPlayer().isAlive());
	}

	@Test
	public void test_S7_42_UndoAtEndWins() {
		// Undo at End of Game after Winning.

		// given
		// Given the game has started,
		getEngine().start();
		getEngine().left(); // eat first food
		getEngine().right(); // go back
		getEngine().up(); // move next to final food
		// when
		getEngine().right();
		// then
		assertTrue(getUI().getGame().getPointManager().allEaten());

		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then the game will not do any undo movements.
		assertEquals(playerTile, getPlayer().getTile());
		assertEquals(ghostTile, theGhost().getTile());
	}

	@Test
	public void test_S7_51_UndoAtSuspend() {
		// given
		// Given the game has started,
		getEngine().start();

		// Player Makes some movements
		getEngine().left(); // eat first food

		// Ghost makes some movements
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);

		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();
		int foodRemaining = getUI().getGame().getPointManager().totalFoodInGame();
		int playerPoints = getPlayer().getPoints();

		getEngine().stop(); // Suspend Game

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then the game should not be able to undo any movements
		// Player and Ghost positions unchanged.
		assertEquals(getEngine().getCurrentState(), MatchState.PAUSING);
		assertEquals(getPlayer().getTile(), playerTile);
		assertEquals(theGhost().getTile(), ghostTile);

		// Player Points and Available Food unchanged
		assertEquals(playerPoints, getPlayer().getPoints());
		assertEquals(foodRemaining, getUI().getGame().getPointManager().totalFoodInGame());
	}

	@Test
	public void test_S7_61_UndoButtonVisible() {

		// given
		// Given the game has started,
		getEngine().start();

		// when

		// then
		// Then the game should show the "UNDO" button
		getUI().eventHandler().undo(); // Currently not implemented.
		// fail("Test not complete.");
	}

}
