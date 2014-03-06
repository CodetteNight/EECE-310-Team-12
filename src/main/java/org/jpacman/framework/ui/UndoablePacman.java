/**
 * 
 */
package main.java.org.jpacman.framework.ui;

import main.java.org.jpacman.framework.factory.UndoGameFactory;

import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.model.IGameInteractor;
import org.jpacman.framework.ui.MainUI;
import org.jpacman.framework.ui.PacmanInteraction;

/**
 * @author
 */
public class UndoablePacman extends MainUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Mapping of UI events to model actions.
	 */
	private transient PacmanInteraction pi;

	/**
	 * The main window components.
	 */

	private UndoButtonPanel buttonPanel;


	// TODO:
	public void undo() {

	}


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
		initialize();
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

	/**
	 * Create the controllers and user interface elements.
	 * 
	 * @throws FactoryException
	 *             If required resources can't be loaded.
	 * @return The main UI object.
	 */
	@Override
	public UndoablePacman initialize() throws FactoryException {
		pi = new PacmanInteractionWithUndo();
		buttonPanel = (UndoButtonPanel) new UndoButtonPanel().withParent(this).withInteractor(pi);

		super.withFactory(new UndoGameFactory());
		super.withModelInteractor(eventHandler());
		super.withModelInteractor(pi).withButtonPanel(buttonPanel);
		super.initialize();

		setTitle("JPacman with Undo.");

		return this;
	}

	/**
	 * @return The mapping between keyboard events and model events.
	 */
	@Override
	public PacmanInteractionWithUndo eventHandler() {
		return (PacmanInteractionWithUndo) pi;
	}
}
