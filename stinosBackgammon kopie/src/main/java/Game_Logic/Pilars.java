/**
 * Column class that exists on the backgammon board; holds the chips inside.
 *
 * <p>
 * This class will be used to
 *
 * - hold chips in the column
 *
 * @author pietro99, rdadrl
 */
package Game_Logic;

import java.util.ArrayList;

public class Pilars {
    private ArrayList <Stones> stones;

    /**
     * Getter for @chips
     *
     * @return ArrayList containing chips
     */
    public ArrayList<Stones> getStones(){
        return stones;
    }

    /**
     * Default constructor for the column
     * initializes the @chips
     */
    public Pilars(){
        stones = new ArrayList<Stones>();

    }
    public boolean empty() {
        if (stones.size() > 0) return false;
        return true;
    }
}
