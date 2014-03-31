package org.jpacman.framework.model;


public class GhostPath{
	private Tile tile;
	private int count;
	
	public GhostPath(Tile tile, int count){
		this.tile = tile;
		this.count = count;
	}
	
	/**
	 * @return The horizontal X coordinate
	 */
	public int getX() {
		return tile.getX();
	}

	/**
	 * @return The vertical Y coordinate
	 */
	public int getY() {
		return tile.getY();
	}
	
	/**
	 *  @return The tile count in the path array
	 */
	public int getCount() {
		return count;
	}
	
	public Tile getTile(){
		return tile;
	}
	
}