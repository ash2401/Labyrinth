package sample;

import java.util.ArrayList;

/**
 * This class controls the main processes of the game(moves the player, slides the tile and so on)
 *
 * @author Marijus Gudiskis 1901701
 *
 * @version 1.0
 */
public class Game {
    public static int numOfPlayers;
    public static int currentTurn;
    public static Player[] players;//this for testing
    public static FloorTile[][] board;//FloorTile
    private static FloorTile goalTile;
    public static ArrayList<Tile> silkBag = new ArrayList<Tile>();

    /**
     * Creates an instance of the Game class.
     * @param players An aray of players which are a part of the game.
     * @param board A 2d array of floor tiles which represents the board layout.
     * @param goalTile The floor tile which when stepped on by the player will cause a win condition and result in the player who stepped on the tile winning.
     * @param silkBag The silk bag which contains the floor tiles that the players will recieve their tiles from when drawing at the start of a round.
     * @param currentTurn The current turn number of the game.
     * @param numOfPlayers The amount of players which are partaking in the game.
     */
    public Game(Player[] players, FloorTile[][] board, FloorTile goalTile,
                ArrayList<Tile> silkBag, int currentTurn, int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        this.currentTurn = currentTurn;
        this.players = players;
        this.board = board;
        this.silkBag = silkBag;
        this.goalTile = goalTile;
    }

    /**
     * Progresses the turn number and removes fire/ice if there is fire and ice.
     */
    public void nextTurn(){
        removeIceAndFire();
        currentTurn++;
    }

    /**
     * Retrieves the tile at the specified position at the specified location.
     * @param row The y position of the tile.
     * @param col The x position of the tile.
     * @return The tile on board at position [row][column] in the 2d board array.
     */
    public FloorTile getTileFromTheBoard(int row, int col) {
        return board[row][col];
    }


    /**
     * Retrieves the width of the board.
     * @return The width of the board.
     */
    public int getWidthOfBoard() {
        return board[0].length;
    }

    /**
     * Retrieves the height of the board.
     * @return The height of the board.
     */
    public int getHeightOfBoard() {
        return board.length;
    }
    /**
     * slides the tile if the row or column is not fixed or frozen. Row represents in which row you want to insert and
     * col tell in which column to insert, so a row:0 and col:2 will insert from the top at column 2 and row:2 and
     * col:3(max_num_of_col_ will insert from right in the row 2 or row:3(max_num_of_row) col:3 will insert at col
     * 3 from the bottom. The numbers in example ar arbitrary.
     * @param row the row where it is wanted to insert.
     * @param col the col where it is wanted to insert.
     * @param newTile the new tile.
     * @return returns true if successful, false otherwise.
     */
    static public Boolean slideTile(int row, int col, FloorTile newTile) {
        FloorTile oldTile1 = newTile;
        FloorTile oldTile2;
        boolean isNotFixed = true;
        if(row == 0 || row == board.length-1){//determine that the columns will be changed
            for (int i = 0; i < board.length; i++) {//check if there are any fixed or frozen tiles in the col
                if(board[i][col].isFixedTile() || board[i][col].isFrozen){
                    isNotFixed = false;
                }
            }
            //move the tiles
            if(row == 0 && isNotFixed) {

                for(int i = 0; i < board.length; i++) {
                    oldTile2 = board[i][col];
                    board[i][col] = oldTile1;
                    oldTile1 = oldTile2;
                }
            }else if(isNotFixed) {

                for(int i = board.length-1; i >= 0; i--) {
                    oldTile2 = board[i][col];
                    board[i][col] = oldTile1;
                    oldTile1 = oldTile2;
                }
            }else {
                return false;
            }
        }else {//determine that the rows will be changed

            for (int i = 0; i < board[row].length; i++) {
                if(board[row][i].isFixedTile() || board[i][col].isFrozen){
                    isNotFixed = false;
                }
            }

            if(col == 0  && isNotFixed) {
                for(int i = 0; i < board[row].length; i++) {
                    oldTile2 = board[row][i];
                    board[row][i] = oldTile1;
                    oldTile1 = oldTile2;
                }

            }else if(isNotFixed) {
                for(int i = board[row].length-1; i >= 0; i--) {
                    oldTile2 = board[row][i];
                    board[row][i] = oldTile1;
                    oldTile1 = oldTile2;
                }
            }else {
                return false;
            }

        }
        //if a player was standing on that tile it will be relocated to a new tile
        for(int i = 0; i < numOfPlayers; i++) {
            if(oldTile1 == players[i].getPosition()) {
                players[i].updateGetBackTiles(players[i].getPosition());
                players[i].setPosition(newTile);
            }
        }
        oldTile1.isFrozen = false;
        oldTile1.isOnFire = false;
        oldTile1.isFrozenForTheNextNTurns = 0;
        oldTile1.isOnFireForTheNextNTurns = 0;
        silkBag.add(oldTile1);
        return true;
    }

    /**
     * before moving a player it checks if it can move on that tile and makes sure that the player
     * doesn't leave the board.
     * @param player the player that is moving.
     * @param right is it going right.
     * @param left is it going left.
     * @param down is it going down.
     * @param up is it going up.
     * @return returns true if successful, false otherwise.
     */
    static public boolean movePlayer(Player player, boolean right, boolean left, boolean down, boolean up) {

        int playerX = 0;
        int playerY = 0;
        int nX;
        int nY;
        //finding the player on the board
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(player.getPosition() == board[i][j]) {
                    playerX = i;
                    playerY = j;
                }
            }
        }
        //making sure that the player stays on the board
        if(playerY == board[0].length-1 && right) {
            return false;
        }else if(playerY == 0 && left) {
            return false;
        }else if(playerX == 0 && up) {
            return false;
        }else if(playerX == board.length-1 && down) {
            return false;
        }else if(right) {
            nX = playerX;
            nY = playerY+1;
            if(board[nX][nY].west && board[playerX][playerY].east && !board[nX][nY].isOnFire){//if the tile will accept the player
                player.updateGetBackTiles(player.getPosition());
                player.setPosition(board[nX][nY]);
                return true;
            }
        }else if(left) {
            nX = playerX;
            nY = playerY-1;
            if(board[nX][nY].east && board[playerX][playerY].west && !board[nX][nY].isOnFire){
                player.updateGetBackTiles(player.getPosition());
                player.setPosition(board[nX][nY]);
                return true;
            }
        }else if(up) {
            nX = playerX-1;
            nY = playerY;
            if(board[nX][nY].south && board[playerX][playerY].north && !board[nX][nY].isOnFire){
                player.updateGetBackTiles(player.getPosition());
                player.setPosition(board[nX][nY]);
                return true;
            }
        }else if(down) {
            nX = playerX+1;
            nY = playerY;
            if(board[nX][nY].north && board[playerX][playerY].south && !board[nX][nY].isOnFire){
                player.updateGetBackTiles(player.getPosition());
                player.setPosition(board[nX][nY]);
                return true;
            }
        }
        return false;
    }

    /**
     *removes the fire and Ice from the board after a certain number of turns
     */
    public static void removeIceAndFire(){
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j].isOnFireForTheNextNTurns == currentTurn){
                    board[i][j].isOnFire = false;
                }
                if(board[i][j].isFrozenForTheNextNTurns == currentTurn){
                    board[i][j].isFrozen = false;
                }
            }
        }
    }

    /**
     * checks if someone has won the game and who has won the game.
     * @return the player who has won the game or null.
     */
    public static boolean hasWonTheGame(Player player) {
        FloorTile pos = null;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (player.getPosition() == board[i][j]) {
                    pos = board[i][j];
                }
            }
        }
        if (goalTile == pos) {
            for (int i = 0; i < numOfPlayers; i++) {
                players[i].getPlayerProfile().incGamesPlayed();
                if (player == players[i]) {
                    players[i].getPlayerProfile().incWins();
                } else {
                    players[i].getPlayerProfile().incLosses();
                }
            }

            return true;
        }
        return false;
    }

    /**
     * Gets the players of the game in an array.
     * @return An array of the players that are in the game.
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Gets the goal tile from the game.
     * @return The goal tile on board.
     */
    public FloorTile getGoalTile() {
        return goalTile;
    }

    /**
     * Removes the top most tile in the silk bag.
     */
    public void removeFromSilkBag() {
        silkBag.remove(0);
    }

    /**
     * Checks to see if the player can move to an adjacent tile.
     * @param player The player in question.
     * @return True or false depending on if the player in question can move.
     */
    public static boolean canPlayerMove(Player player){
        int playerX = 0;
        int playerY = 0;
        int nX;
        int nY;
        //finding the player on the board
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(player.getPosition() == board[i][j]) {
                    playerX = i;
                    playerY = j;
                }
            }
        }
        //making sure that the player stays on the board

        if(playerY != board[0].length-1) {
            nX = playerX;
            nY = playerY + 1;
            if (board[nX][nY].west && board[playerX][playerY].east && !board[nX][nY].isOnFire) {//if the tile will accept the player
                return true;
            }
        }

        if(playerY != 0) {
            nX = playerX;
            nY = playerY - 1;
            if (board[nX][nY].east && board[playerX][playerY].west && !board[nX][nY].isOnFire) {
                return true;
            }
        }

        if(playerX != 0) {
            nX = playerX - 1;
            nY = playerY;
            if (board[nX][nY].south && board[playerX][playerY].north && !board[nX][nY].isOnFire) {
                return true;
            }
        }

        if(playerX != board.length-1) {
            nX = playerX + 1;
            nY = playerY;
            if (board[nX][nY].north && board[playerX][playerY].south && !board[nX][nY].isOnFire) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the silk bag from the game.
     * @return the silk bag.
     */
    public ArrayList<Tile> getSilkBag() {
        return silkBag;
    }

    /**
     * returns an ordered leaderboard.
     * @return array of player profiles.
     */
    public PlayerProfile[] getLeaderboardOrdered() {
        PlayerProfile[] temp = new PlayerProfile[players.length];
        for(int i = 0; i < players.length; i++) {
            temp[i] = players[i].getPlayerProfile();
        }
        int n = temp.length;
        //bubble sort...
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (temp[j].getWins() > temp[j+1].getWins())
                {
                    // swap arr[j+1] and arr[j]
                    PlayerProfile temp2 = temp[j];
                    temp[j] = temp[j+1];
                    temp[j+1] = temp2;
                }
        return temp;
    }

    /**
     * Gets the board from the game.
     * @return The board.
     */
    public FloorTile[][] getBoard(){
        return board;
    }

    /**
     * Gets the specific player from the index of the array of players.
     * @param index The index of the player you want to find.
     * @return the player at the index from the player array.
     */
    public Player getPlayer(int index) {
        return players[index];
    }

    //Needs to be fixed.
    public static Player getEffectedPlayers(Player efPlayer) {
        //the chosen player will be returned
        return efPlayer;
    }

    /**
     * Gets the tiles surrounding a tile specified.
     * @return The tiles surrounding the tile in the parameters.
     */
    public static ArrayList<FloorTile> getEffectedTiles() {

        ArrayList<FloorTile> temp = new ArrayList<>();
        for(int i = GameControl.ytile-1; i < GameControl.ytile + 2; i++) {
            for(int j = GameControl.xtile-1; j < GameControl.xtile + 2; j++) {
                if(i >= 0 && i < board.length && j >= 0 && j < board[0].length) {
                    temp.add(board[i][j]);
                }
            }
        }
        return temp;
    }

    /**
     * gets the players location in terms of x and y.
     * @param player the player you want to find the location of.
     * @return an array of 2 integers x and y respectively.
     */
    public static int[] findPlayerLocation(Player player){
        int playerX = 0;
        int playerY = 0;
        //finding the player on the board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (player.getPosition() == board[i][j]) {
                    playerX = i;
                    playerY = j;
                }
            }
        }

        int[] playerLocation = {playerX, playerY};

        return playerLocation;
    }

    /**
     * gets the current turn of the game.
     * @return the integer value of the current turn.
     */
    public int getCurrentTurn()
    {
        return currentTurn;
    }

    public static int[] findTileLocation(FloorTile curTile){
        int tileX = 0;
        int tileY = 0;
        //finding the player on the board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (curTile == board[i][j]) {
                    tileX = i;
                    tileY = j;
                }
            }
        }
        return new int[]{tileX, tileY};
    }

    /**
     * Creates an array list of all the fixed tiles on the board.
     * @return An array list of fixed tiles that exist on the board.
     */
    public ArrayList<FloorTile> getFixedTilesFromBoard() {
        ArrayList<FloorTile> fixedTiles = new ArrayList<FloorTile>();
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].isFixedTile()) {
                    fixedTiles.add(board[i][j]);
                }
            }
        }

        return fixedTiles;
    }

}
