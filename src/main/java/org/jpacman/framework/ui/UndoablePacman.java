/**
 * 
 */
package main.java.org.jpacman.framework.ui;

import main.java.org.jpacman.framework.factory.UndoGameFactory;

import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.model.IGameInteractor;
import org.jpacman.framework.ui.MainUI;

/**
 * @author
 */
public class UndoablePacman extends MainUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The underlying game.
	 */
	private transient IGameInteractor theGame;

	// TODO:
	public void undo() {
	}

	/*
	 * public UndoablePacman() { setup(); } public void setup() { withFactory(new
	 * UndoGameFactory()); }
	 */
	/**
	 * @return The underlying game.
	 */
	public IGameInteractor getGame() {
		return super.getGame();
	}

	/**
	 * Top level method creating the game, and starting up the interactions.
	 * 
	 * @throws FactoryException
	 *             If creating the game fails.
	 */
	@Override
	public void main() throws FactoryException {
		initialize().withFactory(new UndoGameFactory());
		start();
	}

	/**
	 * Main starting point of the JPacman game.
	 * 
	 * @param args
	 *            Ignored
	 * @throws FactoryException
	 *             If reading game map fails.
	 */
	public static void main(String[] args) throws FactoryException {
		new UndoablePacman().main();
	}

}
