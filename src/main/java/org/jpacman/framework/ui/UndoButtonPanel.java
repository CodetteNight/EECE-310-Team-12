package org.jpacman.framework.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;

import org.jpacman.framework.ui.ButtonPanel;
import org.jpacman.framework.ui.PacmanInteraction.MatchState;

public class UndoButtonPanel extends ButtonPanel {

	
	PacmanInteractionWithUndo pacmanInteractor;
	/**
	 * 
	 */
	private static final long serialVersionUID = 342724095694331357L;

	private JButton undoButton;
	private JButton shareButton;

	/**
	 * Actually create the buttons.
	 */
	public void initialize() {
		super.initialize();
		undoButton = new JButton("Undo");
		shareButton = new JButton("Share");
		
		initializeUndoButton();
		initializeShareButton();

		addButton(undoButton);
		addButton(shareButton);
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
	
	protected void initializeShareButton() {
		shareButton.setEnabled(false);
		shareButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				share();
			}
		});
		undoButton.setName("jpacman.share");
	}
	
	public void share(){
		((IPacmanInteractionWithUndo) getPacmanInteractor()).share();
		assert invariant();
	}

	public void undo() {
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
	
	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
		enableShareButton();
	}
	
	private void enableShareButton(){
		MatchState s = ((PacmanInteractionWithUndo) getPacmanInteractor()).getCurrentState();
		if(s == MatchState.LOST || s == MatchState.WON){
			shareButton.setEnabled(true);
		}
		else
			shareButton.setEnabled(false);
	}

}
