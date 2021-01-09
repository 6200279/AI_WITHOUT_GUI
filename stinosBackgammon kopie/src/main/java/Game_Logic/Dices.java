/**
 * Dice class that rolls a single dice & returns the current value.
 *
 * @author pietro99, rdadrl
 */

package Game_Logic;
import java.util.Random;

public class Dices {
    private int number;

    /**
     * Generate a random number from 1:6 and set current @num
     *
     * @return <code>null</code>
     */
    public void roll_dice(){
        Random r = new Random();
        number = r.nextInt(6) + 1;
    }

    /**
     * Getter for @num
     *
     * @return Integer @num
     */
    public int getNumber(){
        return number;
    }


}