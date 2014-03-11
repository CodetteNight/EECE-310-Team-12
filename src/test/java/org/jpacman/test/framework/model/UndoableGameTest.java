package test.java.org.jpacman.test.framework.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import main.java.org.jpacman.framework.factory.UndoGameFactory;
import main.java.org.jpacman.framework.model.UndoableGame;

import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.factory.IGameFactory;
import org.jpacman.framework.factory.MapParser;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Food;
import org.jpacman.framework.model.Player;
import org.jpacman.framework.model.Tile;
import org.jpacman.test.framework.model.GameTest;
import org.junit.Test;

public class UndoableGameTest extends GameTest {

	@Override
	public IGameFactory makeFactory() {
		return new UndoGameFactory();
	}

	/**
	 * Simply create a single row game. The resulting game is returned, and can also be obtained via
	 * getGame().
	 * 
	 * @param singleRow
	 *            String representation of one row
	 * @throws FactoryException
	 *             If singleRow contains illegal chars.
	 * @return The game created while parsing the row.
	 */
	@Override
	protected UndoableGame makePlay(String singleRow) throws FactoryException {
		MapParser p = new MapParser(makeFactory());
		UndoableGame theGame = (UndoableGame) p.parseMap(new String[] { singleRow });
		return theGame;
	}

	//
	// Player Moves Test Cases
	//
	/**
	 * Player Moves to an Empty Cell & Undo
	 * 
	 * @throws FactoryException
	 */
	@Test
	public void testP1_PlayerMovesToEmptyAndUndo() throws FactoryException {
		UndoableGame g = makePlay("P #");
		g.movePlayer(Direction.RIGHT);

		assertEquals("Player moved", tileAt(g, 1, 0), g.getPlayer().getTile());
		assertEquals("No food eaten.", 0, g.getPlayer().getPoints());
		assertEquals(Direction.RIGHT, g.getPlayer().getDirection());

		g.undo();
		assertEquals("Undo Player moved", tileAt(g, 0, 0), g.getPlayer().getTile());
		assertEquals(Direction.LEFT, g.getPlayer().getDirection());

	}

	/**
	 * Player Moves into a Wall & Undo
	 * 
	 * @throws FactoryException
	 */
	@Test
	public void testP2_PlayerMovesToWallAndUndo() throws FactoryException {
		UndoableGame g = makePlay("#P.");
		g.movePlayer(Direction.LEFT);
		assertThat("Still at the start", tileAt(g, 1, 0), equalTo(g.getPlayer().getTile()));
		g.undo();
		String msg =
		        "Still at the start after undo (original,current) " + tileAt(g, 1, 0) + " "
		                + g.getPlayer().getTile();
		assertThat(msg, tileAt(g, 1, 0), equalTo(g.getPlayer()
		        .getTile()));

		g.movePlayer(Direction.LEFT);
		assertThat("A2.Still at the start", tileAt(g, 1, 0), equalTo(g.getPlayer().getTile()));
		g.undo();
		String msg2 =
		        "A2.Still at the start after undo (original,current) " + tileAt(g, 1, 0) + " "
		                + g.getPlayer().getTile();
		assertThat(msg2, tileAt(g, 1, 0), equalTo(g.getPlayer().getTile()));
	}


	/**
	 * Player Moves to a Food cell & Undo
	 * 
	 * @throws FactoryException
	 *             Never.
	 */
	@Test
	public void testP3_PlayerMovesToFoodAndUndo() throws FactoryException {
		UndoableGame game = makePlay("P.#");
		Food food = (Food) game.getBoard().spriteAt(1, 0);
		Player player = game.getPlayer();

		int pts = game.getPlayer().getPoints();
		int ptsInGameTotal = game.getPointManager().totalFoodInGame();
		int ptsEaten = game.getPointManager().getFoodEaten();

		game.movePlayer(Direction.RIGHT);

		Tile newTile = tileAt(game, 1, 0);
		assertEquals("Food added", food.getPoints(), player.getPoints());
		assertEquals("Player moved", newTile.topSprite(), player);
		assertFalse("Food gone", newTile.containsSprite(food));

		game.undo();


		player = game.getPlayer();
		Tile oldTile = tileAt(game, 0, 0);
		// int ptsEaten = game.getPointManager().getFoodEaten();

		assertEquals("Player moved undo", oldTile.topSprite(), player);
		assertEquals("Player points removed", pts, game.getPlayer().getPoints());

		assertEquals("Food points replaced", game.getPointManager().getFoodEaten(), ptsEaten);
		assertTrue("Food Tile replaced", newTile.containsSprite(food));

		assertEquals("Points in Game", ptsInGameTotal, game.getPointManager()
		        .totalFoodInGame());
	}

	/**
	 * Player Moves to Ghost & Undo
	 * 
	 * @throws FactoryException
	 */
	@Test
	public void testP4_PlayerMovesToGhostAndUndo() throws FactoryException {
		UndoableGame g = makePlay("PG#");
		Player p = g.getPlayer();

		g.movePlayer(Direction.RIGHT);

		assertFalse("Move kills player", p.isAlive());
		assertThat(tileAt(g, 1, 0), equalTo(p.getTile()));

		g.undo();
		assertFalse("Player remains dead", p.isAlive());

		assertThat("Player remains unmoved", tileAt(g, 1, 0), equalTo(p.getTile()));
	}

	/**
	 * Undo Player at the start of the game.
	 * 
	 * @throws FactoryException
	 */
	@Test
	public void testP5_PlayerGameStartsAndUndo() throws FactoryException {
		UndoableGame g = makePlay("P #");

		g.undo();
		assertTrue("Player remains alive", g.getPlayer().isAlive());
		assertEquals("Undo Player moved", tileAt(g, 0, 0), g.getPlayer().getTile());
	}

	/**
	 * Test that tunnels (empty cells on the border) are properly handled.
	 * 
	 * @throws FactoryException
	 *             Never.
	 */
	@Test
	public void testP6_PlayerTunneledMoveAndUndo() throws FactoryException {
		UndoableGame g = makePlay("P# ");
		g.movePlayer(Direction.LEFT);

		Tile newTile = g.getPlayer().getTile();
		assertThat("Player moved", tileAt(g, 2, 0), equalTo(newTile));

		g.undo();

		newTile = g.getPlayer().getTile();
		assertThat("Player moved undo", tileAt(g, 0, 0), equalTo(newTile));

	}

	//
	// Player Composite Moves Test Cases
	//
	/**
	 * Player moves into Empty cell, original position, then Wall & 4xUndo
	 * 
	 * @throws FactoryException
	 */
	@Test
	public void testP_C1_PlayerCompositeMoveEmptyWallAndUndo() throws FactoryException {
		UndoableGame g = makePlay("#P ");
		g.movePlayer(Direction.RIGHT);
		g.movePlayer(Direction.LEFT);
		g.movePlayer(Direction.LEFT);
		assertThat("Still at the start", tileAt(g, 1, 0), equalTo(g.getPlayer().getTile()));

		g.undo();
		g.undo();
		g.undo();
		g.undo();

		String msg =
		        "Still at the start after undo (original,current) " + tileAt(g, 1, 0) + " "
		                + g.getPlayer().getTile();
		assertThat(msg, tileAt(g, 1, 0), equalTo(g.getPlayer().getTile()));

	}

	/**
	 * Player moves across two Empty Cells & into Wall and 3xUndo
	 * 
	 * @throws FactoryException
	 */
	@Test
	public void testP_C2_PlayerCompositeMoveMultipleEmptyAndUndo() throws FactoryException {
		UndoableGame g = makePlay("P  #");
		g.movePlayer(Direction.RIGHT);
		g.movePlayer(Direction.RIGHT);
		g.movePlayer(Direction.RIGHT);

		g.undo();
		g.undo();
		g.undo();

		String msg =
		        "Still at the start after undo (original,current) " + tileAt(g, 0, 0) + " "
		                + g.getPlayer().getTile();
		assertThat(msg, tileAt(g, 0, 0), equalTo(g.getPlayer().getTile()));

	}

	/**
	 * Move across Food, Empty Cells, Wall and 3xUndo
	 * 
	 * @throws FactoryException
	 */
	@Test
	public void testP_C3_PlayerCompositeMoveFoodWallAndUndo() throws FactoryException {
		UndoableGame g = makePlay("P. #");
		Food food = (Food) g.getBoard().spriteAt(1, 0);
		Tile foodTile = tileAt(g, 1, 0);

		int pts = g.getPlayer().getPoints();

		g.movePlayer(Direction.RIGHT);
		g.movePlayer(Direction.RIGHT);
		Tile acceptableTile = g.getPlayer().getTile();

		g.movePlayer(Direction.RIGHT); // Move into Wall
		assertThat("Still at the start", acceptableTile, equalTo(g.getPlayer().getTile()));

		g.undo();
		g.undo();
		g.undo();

		Tile oldTile = tileAt(g, 0, 0);

		assertEquals("Player moved undo", oldTile.topSprite(), g.getPlayer());
		assertEquals("Player points removed", pts, g.getPlayer().getPoints());
		assertTrue("Food replaced", foodTile.containsSprite(food));
	}

	//
	// Ghost Move Test Cases
	//

	// TODO: Test cases missing.
}
