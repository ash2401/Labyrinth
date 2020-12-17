package sample;

import javafx.scene.image.Image;

/**
 * the chosen player goes back to a FloorTile where it was 2 moves ago if it is possible
 * @author Marijus Gudiskis 1901701
 */
public class Backtrack extends ActionTile {

    public Backtrack(String imgPath, Image tileImage, String name){
        super(imgPath, tileImage, name);
    }

    /**
     * selected player goes back to its past position if it is possible, max 2 turns back
     * @param player the player who is making other players go back
     */
    @Override
    public boolean ActionTile(Player player) {
        Player effectedPlayer = Game.getEffectedPlayers(player);

        //find if the previous location tile still exist on the board
        boolean tileFound = true;//if tile not found, do not proceed with the next tile search
        for(int j = 0; j < 2 && tileFound; j++) {

            tileFound = false;
            FloorTile temp = effectedPlayer.getBackTiles();//for what tile we are searching

            for (int i = 0; i < Game.board.length && temp != null; i++) {
                for (int e = 0; e < Game.board[i].length; e++) {
                    if (Game.board[i][e] == temp) {
                        effectedPlayer.setPosition(temp);
                        tileFound = true;
                    }
                }
            }

        }
        return true;
    }
}
