package sample;

/**
* This Class represents the profile of the player (information about the player).
* @author Cory Lagdon and Steffan Long
* @version 1.0
*/
public class PlayerProfile {
    private int losses;
    private int wins;
    private final String name;
    private int gamesPlayed;

    /**
	 * Creates of profile of a player.
	 * @param losses The amount of times the player has lost.
	 * @param wins The amount of times the player has won.
	 * @param name The name of the player.
	 * @param gamesPlayed The amount of games the player has played.
	 */
    public PlayerProfile(int losses, int wins, String name, int gamesPlayed) {
        this.losses = losses;
        this.wins = wins;
        this.name = name;
        this.gamesPlayed = gamesPlayed;
    }

    /**
	 * Gets the amount of times the player has lost.
	 * @return losses The amount of times the player has lost.
	 */
    public int getLosses() {
        return losses;
    }

    /**
	 * Increments the losses of the player.
	 */
    public void incLosses() {
        losses++;
    }

    /**
	 * Gets the amount of times the player has won.
	 * @return wins The amount of times the player has won.
	 */
    public int getWins() {
        return wins;
    }

    /**
	 * Increments the wins of the player.
	 */
    public void incWins() {
        wins++;
    }

    /**
	 * Gets the name of the player.
	 * @return The name of the player.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Gets how many games the player has played.
	 * @return gamesPlayed The amount of games the player has played.
	 */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
	 * Increments the amount of games the player has played.
	 */
    public void incGamesPlayed() {
        gamesPlayed++;
    }
}
