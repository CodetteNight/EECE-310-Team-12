package main.java.org.jpacman.framework.ui;

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
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				undo();
			}
		});
		undoButton.setName("jpacman.undo");
		undoButton.requestFocusInWindow();
		undoButton.setEnabled(false);
	}


		/**
	 * Provide the parent window.
	 * 
	 * @param parent
	 *            The containing parent window
	 * @return Itself for fluency.
	 */
	/*
	 * public UndoButtonPanel withParent(JFrame parent) { this.parent = parent; return this; }
	 */
	/**
	 * Set the listener capable of exercising the requested events.
	 * 
	 * @param pi
	 *            The new pacman interactor
	 * @return Itself for fluency.
	 */
	/*
	 * @Override public UndoButtonPanel withInteractor(PacmanInteraction pi) { pacmanInteractor =
	 * pi; pi.addObserver(this); return this; }
	 */
	/**
	 * Obtain the handler capable of dealing with button events.
	 * 
	 * @return The pacman interactor.
	 */
	/*
	 * public IPacmanInteractionWithUndo getPacmanInteractor() { return pacmanInteractor; }
	 */
	public void undo() {
		((IPacmanInteractionWithUndo) getPacmanInteractor()).undo();
		pause();
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
