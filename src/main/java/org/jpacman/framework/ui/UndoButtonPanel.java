package org.jpacman.framework.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.jpacman.framework.ui.ButtonPanel;

public class UndoButtonPanel extends ButtonPanel {

	PacmanInteractionWithUndo pacmanInteractor;
	/**
	 * 
	 */
	private static final long serialVersionUID = 342724095694331357L;

	private JButton undoButton;

	/**
	 * Actually create the buttons.
	 */
	public void initialize() {
		super.initialize();
		undoButton = new JButton("Undo");
		initializeUndoButton();

		addButton(undoButton);
	}

	/**
	 * Create the start button.
	 */
	protected void initializeUndoButton() {
		undoButton.setEnabled(true);
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				undo();
			}
		});
		undoButton.setName("jpacman.undo");

	}


	public void undo() {
		// pause();
		((IPacmanInteractionWithUndo) getPacmanInteractor()).undo();
		assert invariant();
	}

	@Override
	public void pause() {
		super.pause();
		undoButton.setEnabled(true);
	}

	@Override
	public void start() {
		super.start();
		undoButton.setEnabled(true);
	}

}
