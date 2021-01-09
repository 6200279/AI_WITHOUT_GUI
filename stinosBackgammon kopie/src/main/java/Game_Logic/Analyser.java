package Game_Logic;

import java.util.ArrayList;
import java.util.Arrays;

public class Analyser {
    public static final int[][] DICE_POSSIBILITIES = {
            {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5},  {6, 6},
            {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6},
            {1, 3}, {2, 4}, {3, 5}, {4, 6},
            {1, 4}, {2, 5}, {3, 6},
            {1, 5}, {2, 6},
            {1, 6}
    };

    public static ArrayList<int[]>[] available_Moves(gameBoard board, ArrayList<Integer> list, Player_AI p) {
        int pID = p.getID();
        ArrayList<int[]>[] possiblePillars = new ArrayList[list.size()];
        for (int i = 0; i < possiblePillars.length; i++) possiblePillars[i] = new ArrayList<int[]>();

        
        Pilars midPillars = board.getMiddlePillars()[0];
        if (pID == 2) midPillars = board.getMiddlePillars()[1];

        //p1 [18:23], p2 [0:5]
        for (int i = 0; i < midPillars.getStones().size(); i++) { //for every hit chip, get it out!
            if (list.size() > 0) {
                //if p1 test columns [0:5]
                if (pID == 2) {
                    for (int j = 0; j < list.size(); j++) {
                        Pilars move_to = board.getPillars()[list.get(j) - 1];
                        if (move_to.getStones().size() < 2) {
                            int[] current_move = {25, list.get(j)};
                            possiblePillars[j].add(current_move);
                        }
                    }
                } else {
                    for (int j = 0; j < list.size(); j++) {
                        Pilars move = board.getPillars()[list.get(j) - 1];
                        if (move.getStones().size() < 2) {
                            int[] current_Move = {24, 24 - list.get(j)};
                            possiblePillars[j].add(current_Move);
                        }
                    }
                }
            }
        }
        int Move_SUM = 0;
        for (int i = 0; i < possiblePillars.length; i++) Move_SUM += possiblePillars[i].size();

        //Basecases:
        //AI still has hit chips
        if (midPillars.getStones().size() > Move_SUM) return possiblePillars;

        //We spent all our list getting hit chips out
        if (midPillars.getStones().size() >= list.size()) return possiblePillars;

        //We were able to get out only with a single dice value
        if (midPillars.getStones().size() > 0) {
     
            boolean[] booleans = new boolean[list.size()];
            int b = 0;
            for (int i = 0; i < possiblePillars.length; i++) {
                if (possiblePillars[i].size() > 0) {
                    b++;
                    booleans[i] = true;
                }
            }
            if (b == 1) {
                for (int i = 0; i < booleans.length; i++) {
                    if (booleans[i]) list.remove(i);
                }
            }
        }
        //End Basecases

        /**
         *
         * so we have already chosen the indexes for the players during initialization such that P1 plays from (18:23] on S|
         * P2 [0:6) on S|.
         *
         */
        boolean p1_Take = true;
        boolean p2_Take = true;

        //Calculate list available from all columns AI has a chip on
        for (int i = 0; i < 24; i++) {
            Pilars pillar = board.getPillars()[i];
            if (!pillar.empty()) { //check if unempty col
                //update takeable status
                if (i > 5 && pillar.getStones().get(0).getPlayerAi().getID() == 1) p1_Take = false;
                if (i < 18 && pillar.getStones().get(0).getPlayerAi().getID() == 2) p2_Take = false;

                Player_AI playerAi = board.getPillars()[i].getStones().get(0).getPlayerAi();
                if (playerAi.equals(p)) { //if AI owns the chips
                    for (int j = 0; j < list.size(); j++) {
                        int destination = i + list.get(j);
                        if (pID == 1) destination = i - list.get(j);

                        if (destination < 24 && destination > -1) { //check if move is attempted in legal boundaries [0:23]
                            if (board.getPillars()[destination].getStones().size() >= 2) { //if has more than 2 chips- check if it's ours
                                if (board.getPillars()[destination].getStones().get(0).getPlayerAi().equals(p)) {
                                    int[] possible_moves = {i, destination};
                                    possiblePillars[j].add(possible_moves);
                                }
                            }
                            else {
                                int[] possible_move = {i, destination};
                                possiblePillars[j].add(possible_move);
                            }
                        }
                    }
                }
            }
        }

        //add in p1 take list
        if (p2_Take && p.getID() == 2) {
            System.out.println("P2 TAKEABLE");
            for (int i = 0; i < list.size(); i++) {
                int SmallPillar = -1;
                for (int j = list.get(i); j > 0; j--) {
                    boolean leftPillar_empty = true;
                    for (int l = j; l <= 5; l++) { //check left side
                        if (board.getPillars()[l].getStones().size() > 0) { //ouch... we found a left chip! only we can take from j
                            if (board.getPillars()[l].getStones().get(0).getPlayerAi().getID() == 2) {
                                leftPillar_empty = false;
                                break;
                            }
                        }
                    }
                    if (leftPillar_empty && board.getPillars()[j].getStones().size() > 0) { //first takeable column
                        if (board.getPillars()[j].getStones().get(0).getPlayerAi().getID() == 2) { //if p1 own:
                            int[] current_moves = {j, 26};
                            System.out.println(" we can take from column" + j);
                            System.out.println(board.toString());
                            possiblePillars[i].add(current_moves);
                            SmallPillar = j;
                            break; //this is the only time we can take!
                        }
                    }
                }

                //try taking from the rolled dice directly
                if (list.get(i) != SmallPillar) {
                    if (board.getPillars()[list.get(i) - 1].getStones().size() > 0) {
                        if (board.getPillars()[list.get(i) - 1].getStones().get(0).getPlayerAi().getID() == 2) {
                            int[] current_move = {list.get(i) - 1, 26};
                            possiblePillars[i].add(current_move);
                        }
                    }
                }
            }
        }

        //add in p2 take list
        if (p1_Take && p.getID() == 1) {
            System.out.println("P1 TAKEABLE!!!!!!!!");
            for (int i = 0; i < list.size(); i++) {
                int SmallerPillar = -1;
                for (int j = 24 - list.get(i); j <= 23; j++) {
                    boolean leftPillarEmpty = true;
                    for (int l = j; l >= 18; l--) { //check left side
                        if (board.getPillars()[l].getStones().size() > 0) { //ouch... we found a left chip! only we can take from j
                            if (board.getPillars()[l].getStones().get(0).getPlayerAi().getID() == 1) {
                                leftPillarEmpty = false;
                                break;
                            }
                        }
                    }
                    if (leftPillarEmpty && board.getPillars()[j].getStones().size() > 0) { //first takeable column
                        if (board.getPillars()[j].getStones().get(0).getPlayerAi().getID() == 1) { //if p1 own:
                            int[] current_move = {j, 26};
                            possiblePillars[i].add(current_move);
                            SmallerPillar = j;
                            break; //this is the only time we can take!
                        }
                    }
                }

                //try taking from the rolled dice directly
                if (list.get(i) != SmallerPillar) {
                    if (board.getPillars()[24 - list.get(i)].getStones().size() > 0) {
                        if (board.getPillars()[24 - list.get(i)].getStones().get(0).getPlayerAi().getID() == 1) {
                            int[] current_move = {24 - list.get(i), 26};
                            possiblePillars[i].add(current_move);
                        }
                    }
                }
            }
        }

        return possiblePillars;
    }

    public static ArrayList<int[][]> Combos(ArrayList<Integer> m, ArrayList<int[]>[] possible, int L, int L1, ArrayList<int[][]> possiblecombos) {

        if (L>0) {
            L--;
            possiblecombos.add(new int[][]{possible[1].get(L1), possible[0].get(L)});

            return Combos(m, possible, L, L1, possiblecombos);
        }
        else if (L1> 0) {
            L = possible[0].size();
            L1--;
            return Combos(m, possible, L ,L1, possiblecombos);
        }


        return possiblecombos;
    }

    public static ArrayList<int[][]> Combosdouble(ArrayList<Integer> list, ArrayList<int[]>[] p, int L1, int L2, int L3, int L4, ArrayList<int[][]> possibleCombos, double v) {
        for(int i=0; i<p[0].size(); i++) {
            for(int j=0; j<p[1].size(); j++) {
                for(int n=0; n<p[2].size(); n++) {
                    for(int m=0; m<p[3].size(); m++) {
                        int[][] combos = new int [4][2];
                        combos[0] = p[0].get(i);

                        combos[1] = p[1].get(j);
                        combos[2] = p[2].get(n);
                        combos[3] = p[3].get(m);

                        possibleCombos.add(combos);

                    }
                }

            }

        }

        return possibleCombos;
    }

    public static ArrayList<int[][]> P_S_StoneCombos(gameBoard board, ArrayList<Integer> list_moves, Player_AI playerAi) {
        ArrayList<int[][]> result = new ArrayList<>();

        ArrayList<int[]>[] available_moves = available_Moves(board, list_moves, playerAi);
        int id = playerAi.getID();

        for (int i = 0; i < available_moves.length; i++) {//for every dice available (i is also the current dice being played!)
            for (int j = 0; j < available_moves[i].size(); j++) { //for every starting pos available
                int[][] The_ChosenONE = new int[list_moves.size()][2];
                The_ChosenONE[0] = available_moves[i].get(j);
                int possistion = 1;

                for (int m = 0; m < list_moves.size(); m++) { //for every move available
                    if (m != i) { //skip if we're trying to play the same dice again!
                        int lastToPlace = The_ChosenONE[possistion - 1][1];
                        int nextPlacement = lastToPlace - list_moves.get(m);
                        if (id == 2) nextPlacement = lastToPlace + list_moves.get(m);

                        The_ChosenONE[possistion] = new int[]{lastToPlace, nextPlacement};
                        possistion++;
                    }
                }
                result.add(The_ChosenONE);
            }
        }
        return result;
    }

    //TODO: Check move legality!
    public static ArrayList<int[][]> allCombos(gameBoard board1, ArrayList<Integer> list_moves, Player_AI player_) {
        ArrayList<int[]>[] possibleMoves = Analyser.available_Moves(board1, list_moves, player_);

        ArrayList<int[][]> pCombos = new ArrayList<>();
        if (list_moves.size() == 4)
            pCombos = Combosdouble(list_moves, possibleMoves, possibleMoves[0].size(), possibleMoves[1].size()-1, possibleMoves[2].size()-1, possibleMoves[3].size()-1, pCombos, Math.pow(list_moves.size(), possibleMoves[0].size()));
        else
            pCombos = Combos(list_moves, possibleMoves, possibleMoves[0].size(), possibleMoves[1].size()-1, pCombos);
        pCombos.addAll(P_S_StoneCombos(board1, list_moves, player_));
        printer(pCombos);
        ArrayList<int[][]> pC1 = legalize_IT420(board1,pCombos, player_);
        ArrayList<int[][]> pC2 = sortify(pC1);


        System.out.println("ORIGINAL--->");
        printer(pCombos);
        System.out.println("LEGALIZED--->");
        printer(pC1);
        //System.out.println("UNIQUIFIED--->");

        printer(pC2);



        return pC2;
    }

    private static void printer(ArrayList<int[][]> pCombos) {
        for(int i=0; i<pCombos.size(); i++){
            for(int j=0; j<pCombos.get(i).length; j++) {
                System.out.print(pCombos.get(i)[j][0]+"  ");
                System.out.print(pCombos.get(i)[j][1]+"      ");
            }
            System.out.println();
        }
        System.out.println("\n\n\n\n\n");
    }

    public static ArrayList<int[][]> sortify(ArrayList<int[][]> pC) {
        for (int i = 0; i < pC.size(); i++) {
            for (int j = i; j < pC.size(); j++) {
                if (i!=j && Arrays.deepEquals(pC.get(i), pC.get(j))) {
                    pC.remove(j);
                    j--;
                }
            }
        }
        return pC;
    }

    private static ArrayList<int[][]> legalize_IT420(gameBoard board, ArrayList<int[][]> pCombos, Player_AI player) {
        ArrayList<int[][]> pCombos1 = new ArrayList<int[][]>();
        int[] highlight = new int[30];
        boolean pass;


            int meanPillar;
            if(player.getID() == 1)
                meanPillar=24;
            else
                meanPillar=25;

        if(board.getPillars()[meanPillar].getStones().size()!=0){
            for(int i=0; i<pCombos.size(); i++ ){
                pass = true;
                int hitStonesNum = board.getMiddlePillars()[player.getID()-1].getStones().size();

                for(int n=0; n<hitStonesNum; n++){
                    if(!(pCombos.get(i)[n][0] == meanPillar)){
                        pass = false;
                    }
                }
                if (pass) pCombos1.add(pCombos.get(i));

            }

        }

        else{

            for (int i = 0; i < pCombos.size(); i++) {
                for (int k = 0; k < 27; k++) {
                    highlight[k] = board.getPillars()[k].getStones().size();
                }

                pass = true;
                pass = true;
                for (int j = 0; j < pCombos.get(i).length; j++) {
                    int fromPillar = pCombos.get(i)[j][0];
                    int toPillar = pCombos.get(i)[j][1];
                    if (toPillar < 0 || toPillar > 23) {
                        pass = false;}
                    else if (board.getPillars()[toPillar].getStones().size() > 0 && board.getPillars()[toPillar].getStones().get(0).getPlayerAi() != player) {
                        pass = false;
                    }
                   else if (fromPillar > -1)
                        highlight[fromPillar]--;
                }

                for (int k = 0; k < highlight.length; k++) {
                    if (highlight[k] < 0) {
                        pass = false;
                    }
                }

                if (pass) pCombos1.add(pCombos.get(i));
            }
        }

        return pCombos1;

    }


    public void printRow(int [][]  a){
       System.out.println(a);
    }

    /**
     * Checks if given boards are of the same state
     * @return true/false
     */
    public static boolean compares(gameBoard board, gameBoard board1) {
        if (board==null) return false;
            for (int i = 0; i < board.getPillars().length; i++) {
            if (board.getPillars()[i].getStones().size() != board1.getPillars()[i].getStones().size()) return false;
            if (board.getPillars()[i].getStones().size() > 0) {
                if (!board.getPillars()[i].getStones().get(0).getPlayerAi().equals(board1.getPillars()[i].getStones().get(0).getPlayerAi())) return false;
            }
        }
        return true;
    }

    public static boolean game_finished(gameBoard board) {
        ArrayList<Stones> takenStones = board.getTakenStones().getStones();
        int White = 0;
        int Black = 0;

        for (int i = 0; i < takenStones.size(); i++) {
            if (takenStones.get(i).getPlayerAi().getID() == 1) White++;
            else Black++;
        }

        if (White == 15 || Black == 15) return true;
        return false;
    }


}
