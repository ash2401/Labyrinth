package sample;


import javafx.scene.image.Image;

/**
 * tile which hold all the needed information to be on the board.
 * @author Marijus Gudiskis 1901701.
 */
public class FloorTile extends Tile {
	//variables
	public int isFrozenForTheNextNTurns = -1;
	public int isOnFireForTheNextNTurns = -1;
	private boolean fixedTile;
	public boolean isOnFire = false;
	public boolean isFrozen = false;
	public String name;


	public boolean north;
	public boolean east;
	public boolean south;
	public boolean west;

	/**
	 * Constructs an instance of the floor tile object.
	 * @param imgPath The image path of the tile.
	 * @param tileImage The image of the tile.
	 * @param fixedTile Whether or not its a fixed tile.
	 * @param north Is the tile enterable from the north.
	 * @param east Is the tile enterable from the east.
	 * @param south Is the tile enterable from the south.
	 * @param west Is the tile enterable from the west.
	 * @param name The name of the tile.
	 */
	public FloorTile(String imgPath, Image tileImage, boolean fixedTile, boolean north, boolean east, boolean south, boolean west, String name){
		super(imgPath, tileImage, name);
		this.fixedTile = fixedTile;
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
	}

	/**
	 * checks if the tile is enterable from the north.
	 * @return true if its enterable from the north.
	 */
	public boolean isNorth() {
		return north;
	}

	/**
	 * checks if the tile is enterable from the east.
	 * @return true if its enterable from the east.
	 */
	public boolean isEast() {
		return east;
	}

	/**
	 * checks if the tile is enterable from the south.
	 * @return true if its enterable from the south.
	 */
	public boolean isSouth() {
		return south;
	}

	/**
	 * checks if the tile is enterable from the west.
	 * @return true if its enterable from the west.
	 */
	public boolean isWest() {
		return west;
	}

	/**
	 * checks to see if the tile is a fixed tile.
	 * @return true if the tile is fixed.
	 */
	public boolean isFixedTile() {
		return fixedTile;
	}

}
