package main.java.org.jpacman.framework.model;

import java.util.ArrayDeque;

import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;

public class UndoableGame extends Game {

	private static ArrayDeque<Moves> moves = new ArrayDeque<Moves>();
	
	public void undo() {
		return;

	}

	@Override
	public void movePlayer(Direction dir) {
		super.movePlayer(dir);
	}

	@Override
	public void moveGhost(Ghost theGhost, Direction dir) {
		super.moveGhost(theGhost, dir);
	}

}
