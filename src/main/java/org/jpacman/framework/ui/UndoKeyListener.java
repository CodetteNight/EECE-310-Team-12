package org.jpacman.framework.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UndoKeyListener implements KeyListener {
	/**
	 * The interface to the underlying model.
	 */
	private final IPacmanInteractionWithUndo modelEvents;

	/**
	 * Create a new keyboard listener, given a handler for model events keyboard events should be
	 * mapped to.
	 * 
	 * @param me
	 *            Events the model can handle.
	 */
	UndoKeyListener(IPacmanInteractionWithUndo me) {

		modelEvents = me;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int code;

		code = event.getKeyCode();

		switch (code) {
			case KeyEvent.VK_UP: // or
			case KeyEvent.VK_K:
				modelEvents.up();
				break;
			case KeyEvent.VK_DOWN: // or
			case KeyEvent.VK_J:
				modelEvents.down();
				break;
			case KeyEvent.VK_LEFT: // or
			case KeyEvent.VK_H:
				modelEvents.left();
				break;
			case KeyEvent.VK_RIGHT: // or
			case KeyEvent.VK_L:
				modelEvents.right();
				break;
			case KeyEvent.VK_Q:
				modelEvents.stop();
				break;
			case KeyEvent.VK_X:
				modelEvents.exit();
				break;
			case KeyEvent.VK_S:
				modelEvents.start();
				break;
			case KeyEvent.VK_Z:
				// modelEvents.undo();
				// TODO: Temporary action
				modelEvents.stop();
				break;
			default:
				// all other events ignored.
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
