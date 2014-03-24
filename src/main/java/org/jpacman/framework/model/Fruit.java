package org.jpacman.framework.model;

import org.jpacman.framework.controller.FruitChanger;
import org.jpacman.framework.model.IBoardInspector.SpriteType;

public class Fruit extends Food {
	
	private static int numFruit;
	private FruitChanger fc = null;
	
	public Fruit( int points ){
		super();
		super.setPoints(points);
		numFruit++;
	}
	
	public void setFC( FruitChanger fc){
		this.fc = fc;
	}
	
	@Override
	public SpriteType getSpriteType() {
		return SpriteType.FRUIT;
	}
	
	@Override
	public void deoccupy(){
		super.deoccupy();
		fc.stop();
		numFruit--;
	}
	
	@Override
	public void occupy(Tile t){
		super.occupy(t);
		fc.start();
		numFruit++;
	}
	
	public static int getNum(){
		return numFruit;
	}
}