package org.jpacman.framework.factory;

import org.jpacman.framework.factory.DefaultGameFactory;
import org.jpacman.framework.model.Fruit;
import org.jpacman.framework.model.UndoableGame;

public class UndoGameFactory extends DefaultGameFactory {

	private transient UndoableGame theUndoableGame;

	@Override
	public UndoableGame makeGame() {
		theUndoableGame = new UndoableGame();
		return theUndoableGame;
	}

	/**
	 * @return The game created by this factory.
	 */
	protected UndoableGame getGame() {
		assert theUndoableGame != null;
		return theUndoableGame;
	}
	
	public Fruit makeFruit() {
		assert getGame() != null;
		Fruit f = new Fruit(15);
		getGame().addFood(f);
		return f;
	}
}