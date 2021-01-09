/**
 * Board class that holds columns in the board and handles movements.
 *
 * <p>
 * This class will be used to
 *
 * - hold columns that hold chips
 * - hold HIT chips
 * - hold TAKEN chips
 * - initialize the board for ready-to-play state
 *
 * @author pietro99, rdadrl
 */

package Game_Logic;

import java.util.ArrayList;

public class gameBoard {

    //indices 0-23 are the regular spots;
    //indice 24-25 being hit chips and
    // 26 being taken chips
    private Pilars[] pilars = new Pilars[27];


    /**
     * Initializes the board as a new game
     * @param pilars as Column[]
     * @param p1 as Player
     * @param p2 as Player
     * @return <code>null</code>
     */

    private void init(Pilars[] pilars, Player_AI p1, Player_AI p2) {
        for(int i=0; i<pilars.length; i++) {
            pilars[i] = new Pilars();
        }

        for (int j = 0; j < 2; j++)
            pilars[0].getStones().add(new Stones(p2));
        for (int j = 0; j < 5; j++)
            pilars[5].getStones().add(new Stones(p1));
        for (int j = 0; j < 3; j++)
            pilars[7].getStones().add(new Stones(p1));
        for (int j = 0; j < 5; j++)
            pilars[11].getStones().add(new Stones(p2));
        for (int j = 0; j < 5; j++)
            pilars[12].getStones().add(new Stones(p1));
        for (int j = 0; j <3 ; j++)
            pilars[16].getStones().add(new Stones(p2));
        for (int j = 0; j < 5; j++)
            pilars[18].getStones().add(new Stones(p2));
        for (int j = 0; j < 2; j++)
            pilars[23].getStones().add(new Stones(p1));

    }

    public void take_init(Pilars[] pilars, Player_AI p1, Player_AI p2) {
        for(int i=0; i<pilars.length; i++)
            pilars[i] = new Pilars();

        for (int j = 0; j < 2; j++)
            pilars[23].getStones().add(new Stones(p2));
        for (int j = 0; j < 2; j++)
            pilars[22].getStones().add(new Stones(p2));
        for (int j = 0; j < 3; j++)
            pilars[21].getStones().add(new Stones(p2));
        for (int j = 0; j < 3; j++)
            pilars[20].getStones().add(new Stones(p2));
        for (int j = 0; j < 4; j++)
            pilars[19].getStones().add(new Stones(p2));
        for (int j = 0; j <1 ; j++)
            pilars[18].getStones().add(new Stones(p2));
        for (int j = 0; j < 2; j++)
            pilars[0].getStones().add(new Stones(p1));
        for (int j = 0; j < 2; j++)
            pilars[1].getStones().add(new Stones(p1));
        for (int j = 0; j < 3; j++)
            pilars[2].getStones().add(new Stones(p1));
        for (int j = 0; j < 3; j++)
            pilars[3].getStones().add(new Stones(p1));
        for (int j = 0; j < 1; j++)
            pilars[4].getStones().add(new Stones(p1));
        for (int j = 0; j <4 ; j++)
            pilars[5].getStones().add(new Stones(p1));
    }


    /**
     * Default constructor for the Board
     * initializes the board to initial state
     * player p1 is blacks, player p2 is whites
     */

    public gameBoard(Player_AI p1, Player_AI p2){
        init(pilars, p1, p2);
    }

    public Pilars[] getPillars(){
        return pilars;
    }

    public Pilars[] getMiddlePillars(){
        Pilars[] a = new Pilars[2];
        a[0] = pilars[24];
        a[1] = pilars[25];
        return a;
    }
    public Pilars getTakenStones(){
        return pilars[26];
    }

    public void emptyBoard(){
        for(int i = 0; i< pilars.length; i++)
            pilars[i] = new Pilars();
    }

    public gameBoard BoardCopy(gameBoard board, Game game){
        //Make sure the copied board has the same players as the original board
        Player_AI p1 = game.getPlayer1();
        Player_AI p2 = game.getPlayer2();
        gameBoard newBoard = new gameBoard(p1,p2);
        newBoard.emptyBoard();

        //Fill the empty board with the correct number of chips and their owner.
        for (int i = 0; i< pilars.length; i++){
            while (board.getPillars()[i].getStones().size()>newBoard.getPillars()[i].getStones().size()){
                newBoard.getPillars()[i].getStones().add(new Stones(board.getPillars()[i].getStones().get(0).getPlayerAi()));
            }
        }
        return newBoard;
    }



    public String toString() {
        final int STRING_LENGTH = 40;
        StringBuilder result = new StringBuilder("Board:\n");
        for (int diaMeter = 0; diaMeter < STRING_LENGTH + 6; diaMeter++) result.append("=");
        for (int i = 0; i < 12; i++) {
            result.append("\n");
            int X_pillar = pilars[i].getStones().size();
            int O_pillar = pilars[23 - i].getStones().size();
            String X_STRING = "X";
            String O_STRING = "O";

            if (X_pillar > 0) {
                if (pilars[i].getStones().get(0).getPlayerAi().getID() == 2) X_STRING = "O";
            }
            if (O_pillar > 0) {
                if (pilars[23 - i].getStones().get(0).getPlayerAi().getID() == 1) O_STRING = "X";
            }
            if (i < 10) result.append(" ");
            result.append(i + "|");
            for (int b = 0; b < X_pillar; b++) result.append(X_STRING);
            for (int s = 0; s < STRING_LENGTH - (X_pillar + O_pillar); s++) result.append(" ");
            for (int w = 0; w < O_pillar; w++) result.append(O_STRING);
            result.append("|" + (23 - i));

            if (i == 5) {
                result.append("\n");
                for (int diaMeter = 0; diaMeter < STRING_LENGTH + 6; diaMeter++) result.append("=");
            }
        }
        result.append("\n");
        for (int DiaMeter = 0; DiaMeter < STRING_LENGTH + 6; DiaMeter++) result.append("=");
        return result.toString();
    }
}
