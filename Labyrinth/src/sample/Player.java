package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
* This Class represents the Player.
* @author Cory Lagdon and Steffan Long
* @version 1.0
*/
public class Player {
	private String imgPath;
	private Image playerImage;
	private PlayerProfile playerProfile;
	private Player[] prevBtPlayers = new Player[4];
	private ArrayList<ActionTile> hand = new ArrayList<ActionTile>();
	private ArrayList<FloorTile> backTiles= new ArrayList<FloorTile>();
	private int movesPerTurn = 1;
	private FloorTile playerLocation;
	
	/**
	 * Creates a Player for the board.
	 * @param imgPath The path of image of the player.
	 * @param playerProfile The object playerProfile.
	 * @param location The location of the player on the board.
	 * @param playerImage The image of the player.
	 */
	public Player(String imgPath, PlayerProfile playerProfile, FloorTile location, Image playerImage) {
		this.imgPath = imgPath;
		this.playerProfile = playerProfile;
		this.playerLocation = location;
		this.playerImage = playerImage;

		updateGetBackTiles(location);
	}

	/**
	 * Get the playerImage.
	 * @return The player image.
	 */
	public Image getPlayerImage(){
		return playerImage;
	}

	/**
	 * Get the playerProfile.
	 * @return The player profile.
	 */
	public PlayerProfile getPlayerProfile() {
		return playerProfile;
	}

	/**
	 * Get the list of previous players.
	 * @return The prevBtPlayers.
	 */
	public Player[] getPrevBtPlayers()
	{
		return prevBtPlayers;
	}
	
	/**
	 * Adds a player to the list of backtracked players.
	 * @param btdPlayer The player who has been backtracked.
	 */
	public void addPrevBtPlayers(Player btdPlayer)
	{
		for (int i = 0; i < prevBtPlayers.length-1; i++)
		{
			if (prevBtPlayers[i] == null)
			{
				prevBtPlayers[i] = btdPlayer;
				break;
			}
		}
	}

	/**
	 * Gets the hand of the player.
	 * @param index The position of the tile in the array.
	 * @return ActionTile An action tile from the array.
	 */
	public ActionTile getHand(int index)
	{
	    return hand.get(index);
	}

	/**
	 * Adds new action tile to the players hand.
	 * @param newTile The tile to be added to the player's hand.
	 */
	public void addHand(ActionTile newTile) {
		hand.add(newTile);
	}

	/**
	 * Get the size of the players hand.
	 * @return hand.size() The size of the player's hand.
	 */
	public int sizeOfHand(){
		return hand.size();
	}
	
	/**
	 * Remove a tile from the players hand.
	 * @param tile The tile that needs to get removed from the player's hand.
	 */
	public void removeHandTile(ActionTile tile) {
		hand.remove(tile);
	}
	
	/**
	 * Gets the image path of the player image.
	 * @return imgPath The path of the player's image.
	 */
	public String getImage()
	{
		return imgPath;
	}
	
	/**
	 * Gets the number of moves the players can do in a turn.
	 * @return movesPerTurn The number of moves the player can do in a turn.
	 */
	public int getMovesPerTurn()
	{
		return movesPerTurn;
	}
	
	/**
	 * Sets the amount of moves the player can do this turn.
	 * @param num The amount of moves the player can do in a turn. 
	 */
	public void setMovesPerTurn(int num)
	{
		if (num > 2)
		{
			num = 2;
		}
		else if (num < 0)
		{
			num = 0;
		}
		
		movesPerTurn = num;
	}

	/**
	 * Update the ArrayList backTiles to having the last 2 tiles in it.
	 * @param pastTile The last tile that the player visited.
	 */
	 public void updateGetBackTiles(FloorTile pastTile) {
		 backTiles.add(0, pastTile);
		 if(backTiles.size() > 2) {
			backTiles.remove(2);
		}
	 }

	/**
	 * Gets the last tile that the player visited.
	 * @return temp The last tile that the player visited. 
	 */
	 public FloorTile getBackTiles() {
	 	if(backTiles.size() == 0){
	 		return null;
		}else {
			FloorTile temp = backTiles.get(0);
			backTiles.remove(0);
			return temp;
		}
	 }
	 
	 /**
	  * Sets the position of the player.
	  * @param newPos The position that the player should be set to.
	  */
	 public void setPosition(FloorTile newPos) {
		 this.playerLocation = newPos;
	 }

	 /**
	  * Gets the location of the player.
	  * @return playerLocation The location of the player.
	  */
	 public FloorTile getPosition() {
		return playerLocation;
	 }
}
