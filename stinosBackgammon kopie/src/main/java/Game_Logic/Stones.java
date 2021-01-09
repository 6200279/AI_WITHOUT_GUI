/**
 * Chip class that holds it's current state and it's owner.
 *
 * <p>
 * In real life, chips property determines it's owner, so we followed the same practice
 *
 * Chips can have 3 states:
 * - it's free (not taken & not hit)
 * - it's hit (not taken & hit)
 * - it's taken (taken & not hit)
 *
 * @author pietro99, rdadrl
 */
package Game_Logic;

public class Stones {
    private Player_AI playerAi;
    private boolean hit;
    private boolean taken;
    private int ID;
    private static int last_ID;

    /**
     * Default constructor for the Chip
     * Sets the owner and initializes status
     *
     * @param a as Player that owns the chip
     */
    public Stones(Player_AI a) {
        playerAi = a;
        taken = false;
        hit = false;
        ID = last_ID++;
    }
    public Player_AI getPlayerAi(){
        return playerAi;
    }

    public int getID(){
        return ID;
    }

    public void hit(){
        this.hit = true;
    }

    public void take(){
        this.taken = true;
        getPlayerAi().StoneIsTaken();
    }
    public boolean getHitStatus(){
        return this.hit;
    }
}