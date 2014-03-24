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
            if (t == null) {
                return;
            }
            Fruit fruit = new Fruit(15);
            getGame().getPointManager().addPointsToBoard(fruit.getPoints());
            getGame().remFood(f);
            f.deoccupy();
			FruitChanger fc = new FruitChanger(getGame(),fruit);
            fruit.setFC(fc);
            fruit.occupy(t);
        }
    }
}