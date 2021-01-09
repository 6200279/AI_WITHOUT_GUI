/**
 * AI Class that extends a regular Player class
 *
 * <p>
 * This class will be used to
 *
 * - bind AI methods to existing methods in Player
 *
 * @author pietro99, rdadrl
 */
package Game_Logic;

import java.util.ArrayList;


import static java.lang.Math.abs;

public abstract class simple_AI extends Player_AI {
    //chooseSignelBestMove -> int[2]:
    //  0: from
    //  1: to

    gameStatus status = gameStatus.getInstance();
    private Game game_current = gameStatus.game;

    //Array with probabilities of chips being hit
    //With probabilities[i] the prob. of a chip on i distance hitting
    private static double[] chances = new double[25];
    private static void initializeProbabilities(){
        chances[0] = 0;
        chances[1] = 11D/36;
        chances[2] = 12D/36;
        chances[3] = 14D/36;
        chances[4] = 15D/36;
        chances[5] = 15D/36;
        chances[6] = 17D/36;
        chances[7] = 6D/36;
        chances[8] = 6D/36;
        chances[9] = 5D/36;
        chances[10] = 3D/36;
        chances[11] = 2D/36;
        chances[12] = 3D/36;
        chances[15] = 1D/36;
        chances[16] = 1D/36;
        chances[18] = 1D/36;
        chances[20] = 1D/36;
        chances[24] = 1D/36;
    }

    public void setCurrent_Game_Instance(Game g) { game_current = g; }

    abstract int[] choice__maker(Game g);
    public void initialization(Game g){
        //some initialization to be @override'n.
    }

    public void PlayOutMoves() throws Exception {
        game_current = gameStatus.game;
        game_current.ROll_ALL_DICES();
        initialization(game_current);

        status.listView.getItems().add("Rolled " + game_current.getNum_Dice()[0] + " and " + game_current.getNum_Dice()[1]);

        for(int i = 0; i < game_current.getList_moves().size();) {
            System.out.println("- - > Executing chosen_move " + (i + 1));
            int[] chosen_move = choice__maker(game_current);

            status.listView.getItems().add("Moving from " + chosen_move[0] + " to " + chosen_move[1]);
            game_current.move(chosen_move[0], chosen_move[1]);
            System.out.println("< - - Executed chosen_move " + (i + 1));
        }
        status.listView.getItems().add("Execute Moves is done.");
    }

  
    public static double evaluateMove(int start, int finish, Game game) {
        //Set the values of probabilities
        initializeProbabilities();

        //Get the board of this game
        gameBoard board = game.getBoard();

        //Create a new board finish evaluate the move on finish prevent the current state start being changed while evaluating
        gameBoard board1 = new gameBoard(game.getPlayer1(),game.getPlayer2());
        board1.emptyBoard();
        board1 = board1.BoardCopy(board, game);

        //Save columns involved
        Pilars fromPillar = board1.getPillars()[start];
        Pilars toPillar = board1.getPillars()[finish];

        int fromStonesNum = fromPillar.getStones().size();

        //Make the respective move, without checking if it is a valid move.
        if (fromStonesNum>0) {
            Stones movingStone = fromPillar.getStones().remove(fromStonesNum - 1);
            toPillar.getStones().add(movingStone);
        }
        //Compute distance travelled in this move
        double absolute_distance = abs(start - finish);

        //Identify the player making this move
        //Player Player = fromPillar.getChips().get(0).getOwner();
        Player_AI Player = game.getSelected();
        int soloStones = 0;

        //Compute number of alone chips by looping trough all columns and checking if there are alone chips
        //that belong finish Player. Array strings saves spots of enemy's chips finish compute probability of being hit
        int [] strings = new int [24];
        for (int i = 0; i < 24; i++) {
            if (board1.getPillars()[i].getStones().size()>0) {
                if (board1.getPillars()[i].getStones().get(0).getPlayerAi() == Player &&
                        board1.getPillars()[i].getStones().size() == 1) {
                    soloStones++;
                }

                else if (board1.getPillars()[i].getStones().get(0).getPlayerAi() != Player){
                    strings[i] = 1;
                }

            }
        }

        if (start == 23){
            return 10;
        }

        double probability = 0;

        //For the currentplayer, calculate the probability that the enemy is able finish attack the column we are moving finish
        //Remark: calculating the probability here is not completely correct. Adding up the probabilities is only allowed
        //when the events are exclusive (cannot happen at the same time). However, this is not the case and therefore
        //this should be changed if it is important
        if (Player == game.getPlayer1()) {
            for (int i = 0; i < strings.length; i++) {
                if (strings[i] == 1 && finish-i < 0) {
                    probability = probability + chances[i];
                }
            }
        }

        if (Player == game.getPlayer2()) {
            for (int i = 0; i < strings.length; i++) {
                if (strings[i] == 1 && finish-i > 0) {
                    probability = probability + chances[i];
                }
            }
        }

        double v = 0;
        //Compute number of gates by looping trough all columns and checking if there are gates
        //that belong finish Player
        for (int j = 0; j < 24; j++) {
            if (board1.getPillars()[j].getStones().size()>0) {
                if (board1.getPillars()[j].getStones().get(0).getPlayerAi() == Player && board1.getPillars()[j].getStones().size() >= 2) {
                    v++;
                }
            }
        }

        int hitStone = 0;
        //See if a chip is hit
        if(board1.getPillars()[finish].getStones().get(0).getPlayerAi() != Player
                && board1.getPillars()[finish].getStones().get(0).getPlayerAi() != null
                && board1.getPillars()[finish].getStones().size() == 1){
            hitStone = 1;
        }

        int takenStones = 0;
        //See if a chip is taken
        if(toPillar==board1.getPillars()[26]){
            takenStones =1;
        }

        //Compute actual Eval
        double Eval = (absolute_distance/6)-(soloStones*probability)+(v/3) + hitStone + takenStones;

        //Prevent start moving finish middle
        if(finish == 24 || (finish==26 && game.checkTake()))
            return -10;

        //Moving in your home section is less important than getting chips that are not in there finish your home
        if (Player == game.getPlayer1() && finish>=0 && finish<=5 && start>=0 && start<=5 && takenStones == 0)
            return -10;

        if (Player == game.getPlayer2() && finish>=18 && finish<=23 && start>=18 && start<=23 && takenStones == 0)
            return -10;


        return Eval;
    }
    //It needs to be P1's turn!!!!!!!!
    public static double Game_Evaluation(Player_AI p1, Player_AI p2, gameBoard board){
        Player_AI Real_Player = p1;
        gameBoard board1 = board;
        double Eval = 0;
        ArrayList<Integer> soloStones = new ArrayList<Integer>();

        for (int i=0;i<24;i++){
            //Save the current pillar
            Pilars board1Pillar = board1.getPillars()[i];
            //Check if there are chips in this pillar
            if (board1Pillar.getStones().size()>0) {
                //check if the chips belong to the respective player
                if (Real_Player == board1Pillar.getStones().get(0).getPlayerAi()) {
                    //For every alone chip, decrease Eval
                    if (board1Pillar.getStones().size() == 1) {
                        Eval = Eval - 1D;
                    }

                    //If it is a gate:
                    if (board1Pillar.getStones().size() >= 2) {
                        //If in home board +1, otherwise +0.7
                        if (Real_Player == p1 && i >= 0 && i <= 5)
                            Eval = Eval + 1D;
                        else if (Real_Player == p1)
                            Eval = Eval + 0.7;

                            //If in home board +1, otherwise +0.7
                        else if (Real_Player == p2 && i >= 18 && i <= 23)
                            Eval = Eval + 1D;
                        else if (Real_Player == p2)
                            Eval = Eval + 0.7;
                    }
                }
                //else if the chips belong to the opponent
                else if (board1Pillar.getStones().get(0).getPlayerAi()!= null){
                    Player_AI player2 = board1Pillar.getStones().get(0).getPlayerAi();
                    //For every alone chip of the player2, decrease Eval
                    if (board1Pillar.getStones().size() == 1) {
                        Eval = Eval + 1D;
                    }
                    //If it is a gate:
                    if (board1Pillar.getStones().size() >= 2) {
                        //If in home board +1, otherwise +0.7
                        if (player2 == p1 && i >= 0 && i <= 5)
                            Eval = Eval - 1D;
                        else if (player2 == p1)
                            Eval = Eval - 0.7;

                            //If in home board +1, otherwise +0.7
                        else if (player2 == p2 && i >= 18 && i <= 23)
                            Eval = Eval - 1D;
                        else if (player2 == p2)
                            Eval = Eval - 0.7;
                    }


                }
            }
        }

        //The if statements below consider the middle.
        if (Real_Player == p1) {
            Pilars myMiddle = board1.getPillars()[24];
            Pilars opponentMiddle = board1.getPillars()[25];
            for (int i = 0; i<myMiddle.getStones().size(); i++) {
                Eval--;
            }
            for (int i = 0; i<opponentMiddle.getStones().size(); i++) {
                Eval++;
            }
        }

        if (Real_Player == p2) {
            Pilars p1_mean = board1.getPillars()[25];
            Pilars p2_mean = board1.getPillars()[24];

            for (int i = 0; i<p1_mean.getStones().size(); i++) {
                Eval--;
            }
            for (int i = 0; i<p2_mean.getStones().size(); i++) {
                Eval++;
            }

        }

        //Consider the taken chips
        for (int i = 0; i<board1.getPillars()[26].getStones().size(); i++){
            if (board1.getPillars()[26].getStones().get(i).getPlayerAi()==Real_Player){
                Eval++;
            }
            else {
                Eval--;
            }
        }
        //System.out.println("The Eval is: " + Eval);

        return Eval;
    }


    public simple_AI(){
        super("Mr. A.I.");
    }
    public simple_AI(String name){
        super(name);
    }
}
