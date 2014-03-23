package org.jpacman.framework.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import org.jpacman.framework.model.Game;

public abstract class AbstractFruitChanger implements IController, ActionListener {

    /**
     * Timer to be used to limit the life of a fruit
     */
    private final Timer timer;

    /**
     * Underlying game engine.
     */
    private transient Game theGame;

    /**
     * Create a new fruitcontroller using the default
     * delay and the given game engine.
     *
     * @param game The underlying model of the game.
     */
    public AbstractFruitChanger(Game game) {
        theGame = game;
        timer = new Timer(5000, this);
        assert controllerInvariant();
    }

    /**
     * Variable that should always be set.
     * @return true iff all vars non-null.
     */
    protected final boolean controllerInvariant() {
        return timer != null && theGame != null;
    }

    /**
     * ActionListener event caught when timer ticks.
     * @param e Event caught.
     */
    @Override
	public void actionPerformed(ActionEvent e) {
        assert controllerInvariant();
        synchronized (theGame) {
            doTick();
            timer.stop();
        }
        assert controllerInvariant();
    }

    @Override
	public void start() {
        assert controllerInvariant();
        // the game may have been restarted -- refresh the ghost list
        // contained.
        synchronized (theGame) {
            timer.start();
        }
        assert controllerInvariant();
     }

    @Override
	public void stop() {
        assert controllerInvariant();
        timer.stop();
        assert controllerInvariant();
    }
    
    /**
     * @return The object to manipulate the game model.
     */
    protected Game getGame() {
    	return theGame;
    }
}
