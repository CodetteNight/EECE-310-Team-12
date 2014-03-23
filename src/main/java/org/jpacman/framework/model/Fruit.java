package org.jpacman.framework.model;

import org.jpacman.framework.model.IBoardInspector.SpriteType;

public class Fruit extends Food {
	
	private static int numFruit;
	
	public Fruit( int points ){
		super();
		super.setPoints(points);
		numFruit++;
	}
	
	@Override
	public SpriteType getSpriteType() {
		return SpriteType.FRUIT;
	}
	
	@Override
	public void deoccupy(){
		super.deoccupy();
		numFruit--;
	}
	
	public static int getNum(){
		return numFruit;
	}
}