package Game_Logic;

import java.util.ArrayList;
import java.util.Random;

// plays random moves
public class Randomness_AI extends simple_AI {


    public int[] choice__maker(Game game){

        ArrayList<int[]> possiblePillars = new ArrayList<>();
        System.out.println("AI is choosing r move.");


        possiblePillars = Analyser.available_Moves(game.getBoard(),game.getList_moves(), game.getPlayer2())[0];
        possiblePillars.addAll(Analyser.available_Moves(game.getBoard(),game.getList_moves(), game.getPlayer2())[1]);
        Random r = new Random();

        int Play_random = r.nextInt(possiblePillars.size());
        System.out.println("Random number: " + Play_random);
        System.out.println("Size of possible pillars: " + possiblePillars.size());
        if(game.getBoard().getMiddlePillars()[1].getStones().size()!=0) {
            possiblePillars = ValidMoveFilter(possiblePillars);
        }

    return possiblePillars.get(Play_random);
    }



    public  ArrayList<int[]> ValidMoveFilter(ArrayList<int[]> possiblePillars){
        ArrayList<int[]> possiblePills = new ArrayList<int[]>();
        for (int i = 0; i<possiblePillars.size(); i++){
            if(possiblePillars.get(i)[0]== 25){
                possiblePills.add(possiblePillars.get(i));
            }
        }
        return possiblePills;
    }

    public ArrayList<int[]> possibleMoves(Game game){

        ArrayList<int[]> possiblePillars = new ArrayList<>();

        gameBoard gameBoard = game.getBoard();

        // gets all possible moves
        for (int i = 0; i < 24; i++) {
            if (gameBoard.getPillars()[i].getStones().size() > 0) { //check if unempty col
                if (gameBoard.getPillars()[i].getStones().get(0).getPlayerAi() == game.getPlayer2()) { //if AI owns the chips

                    for (int j = 0; j < game.getList_moves().size(); j++) {
                        if (i + game.getList_moves().get(j) < 24) { //if valid move in terms of moving to "to" col
                            if (gameBoard.getPillars()[i + game.getList_moves().get(j)].getStones().size() > 0) {//full column, check owner
                                if (gameBoard.getPillars()[i + game.getList_moves().get(j)].getStones().get(0).getPlayerAi() == game.getPlayer2() || gameBoard.getPillars()[i + game.getList_moves().get(j)].getStones().size() == 1) {
                                    int[] move = {i, i + game.getList_moves().get(j)};
                                    possiblePillars.add(move);
                                }
                            } else { //if empty column
                                int[] list_move = {i, i + game.getList_moves().get(j)};
                                possiblePillars.add(list_move);
                            }
                        }
                    }
                }
            }
        }
        return possiblePillars;
    }

    public Randomness_AI(){ super("Random"); }

}
