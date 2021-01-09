package Game_Logic;

import java.util.ArrayList;



public class simple_ai_2 extends simple_AI {


    int[] choice__maker(Game g) {
        ArrayList<int[]> possiblePillars = new ArrayList<>();
        gameBoard board = g.getBoard();
        int[] list_moves=new int[4];
        System.out.println("AI is choosing best move.");

        if (board.getMiddlePillars()[1].getStones().size() > 0) {
            list_moves[0] = 25;
            Pilars fromPillar = board.getMiddlePillars()[1];
            Pilars middlePillar = board.getMiddlePillars()[0];
            int fromStonesNum = fromPillar.getStones().size();
            if ((board.getPillars()[g.getList_moves().get(0) - 1].getStones().size() > 1 && board.getPillars()[g.getList_moves().get(0) - 1].getStones().get(0).getPlayerAi() == g.getPlayer2())
                    || (board.getPillars()[g.getList_moves().get(0) - 1].getStones().size() == 0)) {
                Pilars toPillar = board.getPillars()[g.getList_moves().get(0) - 1];
                Stones movingStone = fromPillar.getStones().remove(fromStonesNum - 1);
                toPillar.getStones().add(movingStone);
                list_moves[1] = g.getList_moves().get(0);
            } else if (board.getPillars()[g.getList_moves().get(1) - 1].getStones().size() > 1 && board.getPillars()[g.getList_moves().get(1) - 1].getStones().get(1).getPlayerAi() == g.getPlayer2()
                    || (board.getPillars()[g.getList_moves().get(1) - 1].getStones().size() == 0)) {
                Pilars toPillar = board.getPillars()[g.getList_moves().get(1) - 1];
                Stones movingStone = fromPillar.getStones().remove(fromStonesNum - 1);
                toPillar.getStones().add(movingStone);
                list_moves[1] = g.getList_moves().get(1);
            } else if (board.getPillars()[g.getList_moves().get(0) - 1].getStones().size() == 1 && board.getPillars()[g.getList_moves().get(0) - 1].getStones().get(0).getPlayerAi() == g.getPlayer1()) {
                Pilars toPillar = board.getPillars()[g.getList_moves().get(0) - 1];
                Stones movingstone = fromPillar.getStones().remove(fromStonesNum - 1);
                Stones takenstone = toPillar.getStones().remove(0);
                toPillar.getStones().add(movingstone);
                middlePillar.getStones().add(takenstone);
                list_moves[1] = g.getList_moves().get(0);
            } else if (board.getPillars()[g.getList_moves().get(1) - 1].getStones().size() == 1 && board.getPillars()[g.getList_moves().get(1) - 1].getStones().get(1).getPlayerAi() == g.getPlayer1()) {
                Pilars toPillar = board.getPillars()[g.getList_moves().get(1) - 1];
                Stones movingStone = fromPillar.getStones().remove(fromStonesNum - 1);
                Stones takenStone = toPillar.getStones().remove(0);
                toPillar.getStones().add(movingStone);
                middlePillar.getStones().add(takenStone);
                list_moves[1] = g.getList_moves().get(1);
            } else if (board.getPillars()[g.getList_moves().get(1) - 1].getStones().size() > 1 && board.getPillars()[g.getList_moves().get(0) - 1].getStones().get(0).getPlayerAi() == g.getPlayer1()) {
                list_moves[1] = 0;
                Pilars toPillar = board.getMiddlePillars()[1];
                Stones movingStone = fromPillar.getStones().remove(fromStonesNum - 1);
                toPillar.getStones().add(movingStone);

            } else if (board.getPillars()[g.getList_moves().get(0) - 1].getStones().size() > 1 && board.getPillars()[g.getList_moves().get(0) - 1].getStones().get(0).getPlayerAi() == g.getPlayer2()) {
                list_moves[1] = 0;
                Pilars toPillar = board.getMiddlePillars()[1];
                Stones movingStone = fromPillar.getStones().remove(fromStonesNum - 1);
                toPillar.getStones().add(movingStone);
            }


        }

      //  else if (b.getColumns()[23].getChips().size() + b.getColumns()[22].getChips().size() + b.getColumns()[21].getChips().size() +b.getColumns()[20].getChips().size() + b.getColumns()[19].getChips().size() + b.getColumns()[18].getChips().size()== 15){
            else if (g.checkTake())  {
            Pilars fromPillar = board.getPillars()[24-g.getList_moves().get(0)];
            Pilars fromPillar2 = board.getPillars()[24-g.getList_moves().get(1)];
            int fromStonesNum = fromPillar.getStones().size();
            int fromStonesNum2 = fromPillar2.getStones().size();
            if (fromStonesNum>0){
            Stones moving= fromPillar.getStones().remove(fromStonesNum-1);
            board.getPillars()[26].getStones().add(moving);}
            g.getPlayer2().addNewTakenStones();
            if (fromStonesNum2>0){
                Stones moving= fromPillar2.getStones().remove(fromStonesNum2-1);
                board.getPillars()[26].getStones().add(moving);}
                g.getPlayer2().addNewTakenStones();
            }

        else if (board.getMiddlePillars()[1].getStones().size() == 0) {

            for (int i = 0; i < 24; i++) {
                if (board.getPillars()[i].getStones().size() > 0) { //check if unempty col
                    if (board.getPillars()[i].getStones().get(0).getPlayerAi() == g.getPlayer2()) { //if AI owns the chips

                        for (int j = 0; j < g.getList_moves().size(); j++) {
                            if (i + g.getList_moves().get(j) < 24) { //if valid move in terms of moving to "to" col
                                if (board.getPillars()[i + g.getList_moves().get(j)].getStones().size() > 0) {//full column, check owner
                                    if (board.getPillars()[i + g.getList_moves().get(j)].getStones().get(0).getPlayerAi() == g.getPlayer2() || board.getPillars()[i + g.getList_moves().get(j)].getStones().size() == 1) {
                                        int[] move = {i, i + g.getList_moves().get(j)};
                                        possiblePillars.add(move);
                                    }
                                } else { //if empty column
                                    int[] move = {i, i + g.getList_moves().get(j)};
                                    possiblePillars.add(move);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("There exists " + possiblePillars.size() + " possible moves.");
        if (possiblePillars.size() > 0) {
            int[] Ideal_move = possiblePillars.get(0);
            //possiblePillars should be filled
            for (int i = 0; i < possiblePillars.size(); i++) {
              //  System.out.println("Evaulating move from " + possiblePillars.get(i)[0] + " to " + possiblePillars.get(i)[1] + ".");
                int[] current_Move = possiblePillars.get(i);
                double evaluate_best = evaluateMove(Ideal_move[0], Ideal_move[1], g);
                double evaluateCurrent = evaluateMove(current_Move[0], current_Move[1], g);
             //   System.out.println("Best score: " + evaluate_best + ", candidate score: " + evaluateCurrent);
                if (evaluateCurrent > evaluate_best) {//better move found
                    Ideal_move = current_Move;
                }
            }
          //  System.out.println("Best move is from " + Ideal_move[0] + " to " + Ideal_move[1] + ".");
            return Ideal_move;
        }
        else {
            //  aState.LOG_BOX.getItems().add("There exists no possible moves for AI!");
            g.getList_moves().clear();
            g.selected = g.getPlayer1();
            if (list_moves.length>0)
                return list_moves;
        }
        return new int[2]; // there's no legal move to make- still crashes man!
    }


    public simple_ai_2() {
        super("Best-Evaluation");
    }
}
