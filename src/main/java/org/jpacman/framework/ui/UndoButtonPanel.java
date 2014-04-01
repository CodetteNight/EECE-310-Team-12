package org.jpacman.framework.ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;

import javax.inject.Inject;
import javax.swing.JButton;

import org.jpacman.framework.social.PostToTwitter;
import org.jpacman.framework.ui.PacmanInteraction.MatchState;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
		int p = ((PacmanInteraction)getPacmanInteractor()).getGame().getPointManager().getFoodEaten();
		PostToTwitter.points = Integer.toString(p);
		System.out.println("UndoButtonPanel():Sharing points on twitter: ("+p+")");	
		try {
			URI uri = new URI("http://localhost:8080/");
			Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
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
