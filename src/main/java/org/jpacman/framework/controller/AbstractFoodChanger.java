package org.jpacman.framework.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import org.jpacman.framework.model.Food;
import org.jpacman.framework.model.Game;

public abstract class AbstractFoodChanger implements IController, ActionListener {
	/**
     * Randomizer used to pick, e.g., a food at random.
     */
    private static Random randomizer = new Random();

    /**
     * Timer to be used to trigger an attempt to replace a food on the board with a fruit
     */
    private final Timer timer;

    /**
     * Vector of foods that are to be changed.
     */
    private List<Food> foods;

    /**
     * Underlying game engine.
     */
    private transient Game theGame;

    /**
     * Create a new foodcontroller using the default
     * delay and the given game engine.
     *
     * @param game The underlying model of the game.
     */
    public AbstractFoodChanger(Game game) {
        theGame = game;
        timer = new Timer(2*1000, this);
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
            timer.setDelay((3 + (int)(Math.random() * ((5 - 3) + 1)))*1000);
        }
        assert controllerInvariant();
    }

    @Override
	public void start() {
        assert controllerInvariant();
        // the game may have been restarted -- refresh the ghost list
        // contained.
        synchronized (theGame) {
            foods = theGame.getFoods();
            timer.start();
            assert foods != null;
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
     * Return a randomly chosen ghost, or null if there
     * are no ghosts in this game.
     * @return Random ghost or null;
     */
    protected Food getRandomFood() {
        Food f = null;
        if (!foods.isEmpty()) {
            f = foods.get(randomizer.nextInt(foods.size()));
        } 
        return f;
    }

    /**
     * Obtain the randomizer used for ghost moves.
     * @return the randomizer.
     */
    protected static Random getRandomizer() {
        return randomizer;
    }
    
    /**
     * @return The object to manipulate the game model.
     */
    protected Game getGame() {
    	return theGame;
    }
}
