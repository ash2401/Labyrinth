package sample;

import javafx.scene.image.Image;

/**
 * adds extra move
 * @author Marijus Gudiskis 1901701
 */
public class DoubleMove extends ActionTile {

	/**
	 * Constructs an instance of the double move tile.
	 * @param imgPath The image path for the tile.
	 * @param tileImage The image of the tile.
	 * @param name The name of the tile.
	 */
    public DoubleMove(String imgPath,  Image tileImage, String name) {
        super(imgPath, tileImage, name);

    }

    /**
     * adds extra move to the player.
     * @param player to what player add the extra move.
     */
    @Override
    public boolean ActionTile(Player player) {
        player.setMovesPerTurn(player.getMovesPerTurn()+1);
        return true;
    }
}
