package sample;
import javafx.scene.image.Image;

/**
 * abstract action tile for other action tiles to follow
 * @author Marijus Gudiskis 1901701
 */
public abstract class ActionTile extends Tile {

    public ActionTile(String imgPath, Image tileImage, String name) {
        super(imgPath, tileImage, name);
    }


    /**
     * united method that all action tiles will use
     * @param player the player who calls this class
     */
    public abstract boolean ActionTile(Player player);
}
