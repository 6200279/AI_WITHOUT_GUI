/**
 * Player class that substitutes for each player whether AI or Human.
 * <p>
 * This class will be used to
 *
 * - determine ownership for chips;
 *      as in the chip will have a referance to the particular player.
 * - hold score
 * - hold name
 * - hold an ID
 *
 *
 */
package Game_Logic;

import javafx.scene.paint.Color;

public class Player_AI {
    private String Player_name;

    private boolean hasStonesHit;
    private int takenStones;
    private int ID;
    private static int lastID = 1;
    private int value;

    private Color color;

    /**
     * Getter for the @name
     *
     * @return the name, or if <code>null</code> ID, which increments with every new player introduces.
t     */
    public String getPlayer_name(){
        if (Player_name != null) return Player_name;
        return "" + ID;
    }
    public boolean hasChipHit() {
        return hasStonesHit;
    }
    public void setHitStones(boolean b){
        hasStonesHit = b;
    }
    /**
     * Getter for @ID
     *
     * @return the ID
     */
    public int getID(){
        return ID;
    }

    /**
     * Getter for @score
     *
     * @return the score
     */
    public int getValue(){
        return value;
    }
    public void StoneIsTaken(){
        takenStones++;
    }
    public int getTakenStones(){
        return takenStones;
    }
    public void addNewTakenStones() { takenStones++; }

    public Player_AI() {
        value = 0;
        takenStones = 0;
        ID = lastID++;
    }

    public Player_AI(String name) {
        value = 0;
        ID = lastID++;
        this.Player_name = name;
    }

    public void setColor(Color newCol) {
        color = newCol;
    }
    public Color getColor() {
        return color;
    }
}