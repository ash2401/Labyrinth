package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * sets appropriate tiles on fire
 * @author Marijus Gudiskis 1901701
 */
public class Fire extends ActionTile {
    public Fire(String imgPath, Image tileImage, String name) {
        super(imgPath, tileImage, name);
    }

    /**
     * sets the FloorTiles on fire
     * @param player the player who called this class, it is not needed but otherwise it would not work in other classes
     */
    @Override
    public boolean ActionTile(Player player) {
        ArrayList<FloorTile> temp = Game.getEffectedTiles();
        boolean ret = isIsSafeToSetFire(temp);
        if(ret) {
            for(FloorTile tile : temp){
                tile.isOnFire = true;
                tile.isOnFireForTheNextNTurns = Game.currentTurn + (2 * Game.numOfPlayers);
            }

        }else {
            System.out.println("there are players in that area");
        }
        return ret;
    }

    /**
     * checks if in the given field there were players
     * @param field the 3 by 3 filed on the board
     * @return true if there are no players on the field and false otherwise
     */
    private boolean isIsSafeToSetFire(ArrayList<FloorTile> field){
        boolean noPlayers = true;

        for(FloorTile tile : field){
            for(int k = 0; k < Game.numOfPlayers; k++) {
                if(Game.players[k].getPosition() == tile) {
                    noPlayers = false;
                }
            }
        }

        return noPlayers;
    }
}
