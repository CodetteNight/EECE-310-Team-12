package test.java.org.jpacman.test.framework.model;

import static org.junit.Assert.assertEquals;
import main.java.org.jpacman.framework.factory.UndoGameFactory;
import main.java.org.jpacman.framework.model.UndoableGame;

import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.factory.IGameFactory;
import org.jpacman.framework.factory.MapParser;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;
import org.jpacman.test.framework.model.GameTest;

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

	public void testC1b_GhostMovesToEmpty() throws FactoryException {
		Game g = makePlay("G #");
		Ghost theGhost = (Ghost) g.getBoard().spriteAt(0, 0);

		g.moveGhost(theGhost, Direction.RIGHT);

		assertEquals("Ghost moved", tileAt(g, 1, 0), theGhost.getTile());
	}
}
