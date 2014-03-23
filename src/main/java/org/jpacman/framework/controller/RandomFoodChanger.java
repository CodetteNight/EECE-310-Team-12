package org.jpacman.framework.controller;

import org.jpacman.framework.model.Food;
import org.jpacman.framework.model.Fruit;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Tile;

public class RandomFoodChanger extends AbstractFoodChanger {

	public RandomFoodChanger(final Game game) {
		super(game);
	}

    /**
     * Actually change a random food into a fruit.
     */
    public void doTick() {
        synchronized (getGame()) {
            Food f = getRandomFood();
            if (f == null || Fruit.getNum() == 5) {
                return;
            }
            Tile t = f.getTile();
            f.deoccupy();
            Fruit fruit = new Fruit(15);
            getGame().addFood(fruit);
            getGame().remFood(f);
            fruit.occupy(t);
            @SuppressWarnings("unused")
			FruitChanger fc = new FruitChanger(getGame(),fruit);
        }
    }
}