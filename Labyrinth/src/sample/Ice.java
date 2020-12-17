package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * ices appropriate tiles
 * @author Marijus Gudiskis 1901701
 */

public class Ice extends ActionTile {

    public Ice(String imgPath, Image tileImage, String name) {
        super(imgPath, tileImage, name);
    }

    /**
     * sets the FloorTiles iced
     * @param player the player who called this class, it is not needed but otherwise it would not work in other classes
     */
    @Override
    public boolean ActionTile(Player player) {
        ArrayList<FloorTile> temp = Game.getEffectedTiles();


        for(FloorTile tile : temp){
            tile.isFrozen = true;
            tile.isFrozenForTheNextNTurns = Game.currentTurn + Game.numOfPlayers;
        }
        return true;
    }
}
