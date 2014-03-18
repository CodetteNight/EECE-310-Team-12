package org.jpacman.framework.model;

import org.jpacman.framework.model.IGameInteractor;

/**
 * Interface for manipulating the game.
 * 
 * @author Arie van Deursen, TU Delft, Jan 30, 2012
 */
public interface IGameInteractorWithUndo extends IGameInteractor {
	
	/**
	 * Undo player moves by altering the model.
	 */
	void undo();

}
	