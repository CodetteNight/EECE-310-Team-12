package org.jpacman.framework.model;

import org.jpacman.framework.model.IBoardInspector.SpriteType;

/**
 * A ghost element on the board.
 * 
 * @author Arie van Deursen, TU Delft, Feb 10, 2012
 */
public class Ghost extends Sprite {

	private int x1, x2, y1, y2;
	private static int num = 0;
	
	public int id;
	
	public Ghost(){
		super();
		id = num++;
		//TODO: set x and y based on the id you just set
		int width = Board.width();
		int height = Board.height();
		switch(id){
			case 0:
				x1 = 0;
				y1 = 0;
				x2 = width/2 + 3;
				y2 = height/2 + 3;
				break;
			case 1:
				x1 = width/2 - 3;
				y1 = 0;
				x2 = width;
				y2 = height/2 + 3;
				break;
			case 2:
				x1 = 0;
				y1 = height/2 - 3;
				x2 = width/2 + 3;
				y2 = height;
				break;
			case 3:
				x1 = width/2 - 3;
				y1 = height/2 - 3;
				x2 = width;
				y2 = height;
				break;
		}
	}
	
	/**
	 * Verify that the given location falls within the
	 * borders of the board.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return True iff (x,y) falls within the board.
	 */
	public boolean withinBorders(int x, int y) {
		return
			x >= x1 && x < x2
			&& y >= y1 && y < y2;
	}
	
	/**
	 * @return That this sprite is a ghost.
	 */
	@Override
	public SpriteType getSpriteType() {
		return SpriteType.GHOST;
	}

}
