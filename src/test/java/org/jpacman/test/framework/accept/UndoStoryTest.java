package test.java.org.jpacman.test.framework.accept;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import main.java.org.jpacman.framework.ui.UndoablePacman;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.IBoardInspector;
import org.jpacman.framework.model.IBoardInspector.SpriteType;
import org.jpacman.framework.model.Tile;
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
		int xOld = getPlayer().getTile().getX();
		int yOld = getPlayer().getTile().getY();

		// and my Pacman has made a movement;

		getEngine().up();

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then my Pacman should revert to its previous cell.
		assertThat(getPlayer().getTile().getX(), is(xOld));
		assertThat(getPlayer().getTile().getY(), is(yOld));
	}

	@Test
	public void test_S7_12_UndoPlayerEats() {
		// given
		// Given the game has started,
		getEngine().start();

		// and my Pacman is on a cell that contained food;
		int xOld = getPlayer().getTile().getX();
		int yOld = getPlayer().getTile().getY();

		Tile previousTile = getPlayer().getTile();
		Tile foodTile = tileAt(0, 1);
		SpriteType foodTileSprite = foodTile.topSprite().getSpriteType();
		int previousPoints = getPlayer().getPoints();

		getEngine().left();

		assertThat(getPlayer().getPoints(), greaterThan(0));

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then my Pacman should revert to its previous cell.
		assertEquals(getPlayer().getTile(), is(previousTile));

		// Unsure if we should be comparing (x,y) coordinates instead
		// assertThat(getPlayer().getTile().getX(), is(xOld));
		// assertThat(getPlayer().getTile().getY(), is(yOld));

		// and I lose the points for that cell,
		assertEquals(previousPoints, getPlayer().getPoints());

		// and the food re-appears on that cell.
		// assertTrue("Top Sprite reappeared", foodTile.containsSprite(foodTileSprite));
		assertEquals(IBoardInspector.SpriteType.FOOD, foodTileSprite);

	}

	@Test
	public void test_S7_21_UndoGhostMoves() {
		// given
		// Given the game has started,
		getEngine().start();

		// and a Ghost has made movements
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

		fail("Test not written.");

	}

	@Test
	public void test_S7_23_UndoGhostLeavesFoodCell() {

		fail("Test not written.");
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
		assertNotSame(ghostTile, theGhost().getTile());

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then my Pacman will stay on the same cell,

		assertEquals(getPlayer().getTile(), playerTile);

		// and the ghost will move to the original position at the start of the game.
		assertEquals(theGhost().getTile(), ghostTile);

	}

	@Test
	public void test_S7_41_UndoAtEnd() {
		// Undo at End of Game after Loosing.

		// given
		// Given the game has started,
		getEngine().start();

		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();

		getEngine().right(); // Player Looses, Game ends.

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then the game will not do any undo movements.
		assertEquals(getPlayer().getTile(), playerTile);
		assertEquals(theGhost().getTile(), ghostTile);

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
		assertEquals(getPlayer().getTile(), playerTile);
		assertEquals(theGhost().getTile(), ghostTile);

		// Player Points and Available Food unchanged
		assertTrue(playerPoints == getPlayer().getPoints());
		assertTrue(foodRemaining == getUI().getGame().getPointManager().totalFoodInGame());

	}

	@Test
	public void test_S7_61_UndoButtonVisible() {

		// given
		// Given the game has started,
		getEngine().start();

		// when

		// then
		// Then the game should show the "UNDO" button

		// getUI().eventHandler().undo(); //Currently not implemented.
		fail("Test not complete.");

	}

	@Test
	public void test_S7_42_UndoAtEndWins() {
		// Undo at End of Game after Winning.

		// given
		// Given the game has started,
		getEngine().start();

		Tile playerTile = getPlayer().getTile();
		Tile ghostTile = theGhost().getTile();
		getEngine().start();
		getEngine().left(); // eat first food
		getEngine().right(); // go back
		getEngine().up(); // move next to final food
		getEngine().right(); // Player Wins, Game ends.

		// when
		// When the user presses the "Undo" button;
		getUI().undo();

		// then
		// Then the game will not do any undo movements.
		assertEquals(getPlayer().getTile(), playerTile);
		assertEquals(theGhost().getTile(), ghostTile);

	}
}
