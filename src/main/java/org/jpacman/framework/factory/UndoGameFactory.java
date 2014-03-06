package main.java.org.jpacman.framework.factory;

import main.java.org.jpacman.framework.model.UndoableGame;

import org.jpacman.framework.factory.DefaultGameFactory;

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
}