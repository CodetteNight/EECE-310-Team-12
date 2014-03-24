package org.jpacman.framework.controller;

import org.jpacman.framework.model.Food;
import org.jpacman.framework.model.Fruit;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Tile;

public class FruitChanger extends AbstractFruitChanger {
	
	private Fruit fruit;
	
	public FruitChanger(final Game game, Fruit f) {
		super(game);
		fruit = f;
		start();
	}

    /**
     * Actually change a fruit back into a food.
     */
    public void doTick() {
        synchronized (getGame()) {
            Tile t = fruit.getTile();
            if (t == null || fruit == null) {
                return;
            }
            Food f = new Food();
            getGame().addFood(f);
            getGame().getPointManager().remPointsToBoard(fruit.getPoints());
            fruit.deoccupy();
            f.occupy(t);
        }
    }
}