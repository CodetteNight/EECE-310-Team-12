package org.jpacman.framework.ui;

import org.jpacman.framework.ui.IPacmanInteraction;

public interface IPacmanInteractionWithUndo extends IPacmanInteraction {

	/**
	 * Undo the game.
	 */
	void undo();

}
