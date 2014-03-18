package org.jpacman.framework.ui;

import java.util.Observable;

import org.jpacman.framework.controller.IController;
import org.jpacman.framework.model.IGameInteractorWithUndo;
import org.jpacman.framework.ui.PacmanInteraction;

public class PacmanInteractionWithUndo extends PacmanInteraction
        implements IPacmanInteractionWithUndo {

	private IGameInteractorWithUndo gameInteractorUndo;

	@Override
	public void up() {
		super.up();
	}

	@Override
	public void down() {
		super.down();
	}

	@Override
	public void left() {
		super.left();
	}

	@Override
	public void right() {
		super.right();
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void exit() {
		super.exit();
	}

	@Override
	public void update(Observable o, Object arg) {
		updateState();
	}

	@Override
	public void undo() {
		// super.stop();
		System.out.println("Entering undo state");
		System.out.println(getCurrentState());
		if (getGame().died() || getGame().won())
			System.out.println("won/lost");
		else if (getCurrentState() == MatchState.PLAYING
		        || getCurrentState() == MatchState.PAUSING) {
			((IGameInteractorWithUndo) this.getGame()).undo();
			System.out.println("Returned from undo");
			stop();
			updateState();
		}
		// super.updateState();
		// TODO: additional updates as needed here
	}

	/**
	 * Add an external controller, which should be stopped/started via the ui.
	 * 
	 * @param controller
	 *            The controller to be added.
	 * @return Itself, for fluency.
	 */
	@Override
	public PacmanInteractionWithUndo controlling(IController controller) {
		super.controlling(controller);
		return this;
	}
}
