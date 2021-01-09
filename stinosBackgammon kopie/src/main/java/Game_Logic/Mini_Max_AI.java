package Game_Logic;
import java.util.ArrayList;



public class Mini_Max_AI extends simple_AI {


    // 21 unique dice combinations
    private static final int[][] DICE_COMBINATIONS = {
            {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6},
            {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6},
            {1, 3}, {2, 4}, {3, 5}, {4, 6},
            {1, 4}, {2, 5}, {3, 6},
            {1, 5}, {2, 6},
            {1, 6},
    };

    public static void main(String[] args) {

        Player_AI player1 = new Player_AI();
        Player_AI player2 = new Player_AI();
        Game game = new Game(player1, player2);

        game.ROll_ALL_DICES();
        minimax_expectation(game);
      buildTree(game);

    }

    int[][] list_moves = null;
    @Override
    public int[] choice__maker(Game game) {

        gameBoard b = game.getBoard();
        Player_AI p = game.getPlayer1();
        int[] DICES = game.getNum_Dice();

        // if a chip of the AI is taken
        /*
        if(b.getColumns()[24].getChips().size() > 0) {

            System.out.println("Test if our if statement fucks up");

            int[] temp = new int[2];

            if ((b.getColumns()[24 - DICES[0]].getChips().size() > 0 && (b.getColumns()[24 - DICES[0]].getChips().get(0).getOwner() == p)) ||
                    (b.getColumns()[24 - DICES[0]].getChips().size() == 0)) {

                int from = 24;
                int to = 24 - DICES[0];

                temp[0] = from;
                temp[1] = to;

            } else if ((b.getColumns()[24 - DICES[1]].getChips().size() > 0 && (b.getColumns()[24 - DICES[1]].getChips().get(0).getOwner() == p)) ||
                    (b.getColumns()[24 - DICES[1]].getChips().size() == 0)) {

                int from = 24;
                int to = 24 - DICES[1];
                temp[0] = from;
                temp[1] = to;

            }
            return temp;
        }
*/
        if (list_moves == null) list_moves = minimax_expectation(game);
        else {
            boolean list_moves_fresh = true;
            for (int i = 0; i < list_moves.length; i++){
                if (list_moves[i][0] != -1) {
                    list_moves_fresh = false;
                }
            }
            if (list_moves_fresh) list_moves = minimax_expectation(game);
        }
        for (int i = 0; i < list_moves.length; i++){
            if (list_moves[i][0] != -1) {
                int[] res = {list_moves[i][0], list_moves[i][1]};
                list_moves[i][0] = -1;
                return res;
            }
        }
        return null; //if this happens we're doomed....
    }

    // constructor - inherits from AI (Player constructor)
    public Mini_Max_AI() {
        super("MiniMax");
    }

    public static boolean Double_DICES(int[] ints ){

        int[] rolleddices = ints;

        if(rolleddices[0] == rolleddices[1]){
            return true;
        }

        return false;
    }

    private static Analyser boardAnalysis;
    private static ArrayList<int[][]> possibleMoveCombinations = new ArrayList<>();

    public static TreeNode buildTree(Game game) {

        gameBoard gameBoard = game.getBoard();

        TreeNode rootNode = new TreeNode(true, gameBoard);

        int[] current_dices = game.getNum_Dice();

        ArrayList<Integer> Current_Dices = new ArrayList<>();
        for (int i = 0; i < current_dices.length; i++) {
            Current_Dices.add(current_dices[i]);

        }

        int[][] layer_ONE_Moves;
        int[][] Layer_TWO_Moves = new int[2][2];


        // if we rolled a double then we make 4 moves
        if (Double_DICES(current_dices)) {
            layer_ONE_Moves = new int[4][2];
            Current_Dices.add(current_dices[0]);
            Current_Dices.add(current_dices[1]);


        } else {
            layer_ONE_Moves = new int[2][2];
        }
        /**
         * Note that player one is MAX and with that the AI;
         * If we change that we also have to change the AI to player 2
         */
        // chance nodes
        // iterate over all possible moves of the current game
        possibleMoveCombinations = boardAnalysis.allCombos(game.getBoard(), Current_Dices, game.getPlayer1());

        System.out.println("Size of rolled current_dices " + Current_Dices);

        // iterate over all move combinations
        for (int i = 0; i < possibleMoveCombinations.size(); i++) {
            // create a copy of each gameBoard
            gameBoard gameBoard1 = gameBoard.BoardCopy(gameBoard, game);

            // iterate over all tuples
                for (int q = 0; q < possibleMoveCombinations.get(i).length; q++) {

                    // store the list_moves
                    int[] list_moves = possibleMoveCombinations.get(i)[q];
                    playMove(list_moves, gameBoard1);

                    // store all tuples
                    layer_ONE_Moves[q] = list_moves;

                }

                // adding a new node to the tree
                // this gameBoard describes one possible move
                TreeNode Layer_ONE = new TreeNode(layer_ONE_Moves, gameBoard1);
                rootNode.addChild(Layer_ONE);
                // adding to a list that stores all nodes of that level
                rootNode.addLayer_ONE(Layer_ONE);
                Layer_ONE.setParent(rootNode);
            }


            // min nodes
            // iterate over all tree nodes (except the rootNode)
            for (int i = 0; i < rootNode.getChildNode().size(); i++) {

                TreeNode tempNode = rootNode.getChildNode().get(i);
                // iterating over 21 distinct current_dices combinations
                for (int j = 0; j < 21; j++) {

                    // create a new gameBoard
                    gameBoard copy = tempNode.getBoard().BoardCopy(tempNode.getBoard(), game);

                    // storing a possible current_dices combination
                    current_dices = DICE_COMBINATIONS[j];

                    // array list of all possible combinations
                    ArrayList<Integer> diceCombos = new ArrayList<>(DICE_COMBINATIONS.length);

                    for (int p : DICE_COMBINATIONS[j]) {
                        diceCombos.add(p);
                        //  System.out.println("Dice comb 2 size: " + diceCombos.size());

                    }
                    double probability;

                    // setting the probability for each current_dices combination
                    if (Double_DICES(current_dices)) {
                        probability = 1 / 36D;
                        diceCombos.add(current_dices[0]);
                        diceCombos.add(current_dices[0]);
                        Layer_TWO_Moves = new int[4][2];

                    } else {
                        probability = 1 / 18D;
                        Layer_TWO_Moves = new int[2][2];
                    }
                    // get all possible moves
                    ArrayList<int[][]> possibleCombos = boardAnalysis.allCombos(copy, diceCombos, game.getPlayer2());;

                    // create a new node, which stores a distinct current_dices combination and the current gameBoard
                    TreeNode NewNode = new TreeNode(DICE_COMBINATIONS[j], copy, probability);
                    // adding the NewNode
                    tempNode.addChild(NewNode);
                    rootNode.addLayer_Two(NewNode);
                    NewNode.setParent(tempNode);


                    // chance nodes || terminal leaf nodes
                    // iterate again over the current amount of possible moves
                    for (int k = 0; k < possibleCombos.size(); k++) {

                        // copy the current gameBoard
                        gameBoard copyBoard = gameBoard.BoardCopy(copy, game);

                        for (int q = 0; q < possibleCombos.get(k).length; q++) {


                                int[] move = possibleCombos.get(k)[q];
                                playMove(move, copyBoard);
                                Layer_TWO_Moves[q] = move;


                        }
                        // add the node which describes a possible move to the tree
                        TreeNode leaf = new TreeNode(Layer_TWO_Moves, copyBoard);
                        NewNode.addChild(leaf);
                        rootNode.addLeaf(leaf);
                        leaf.setParent(NewNode);

                    }
                }

            }

        return rootNode;
    }


    /**
     * we assign a score to all leaf nodes
     * <p>
     * MIN we find the minimum score of the leaf nodes and assign the parent find that score
     * CHANCE then we average and add probability
     * MAX find the max out of all children ---> we reached the root
     *
     * @return exactly an integer matrix that holds the best move-combination
     */

    // TODO: if we roll doubles -> move 4 times
    public static int[][] minimax_expectation(Game game) {


        TreeNode rootNode = buildTree(game);

        // chance layer
        ArrayList<TreeNode> allLeafs = rootNode.getAllLeafs();
        // min layer
        ArrayList<TreeNode> Layer_Two = rootNode.getSecondLayer();
        // chance layer
        ArrayList<TreeNode> Layer_One = rootNode.getLayer_One();

        double infinityMIN = Double.POSITIVE_INFINITY;
        double infinityMAX = Double.NEGATIVE_INFINITY;

        // iterate over all leaf nodes
        for (int i = 0; i < allLeafs.size(); i++) {

            // evaluate each board, stored in every leaf node
            gameBoard board = allLeafs.get(i).getBoard();
            // it is P1's turn
            double move_Score = simple_AI.Game_Evaluation(game.getPlayer1(), game.getPlayer2(), board);
            allLeafs.get(i).setMoveScore(move_Score);

            // covering the case when we reach the last leaf node
            if (i == allLeafs.size() - 1) {
                if (allLeafs.get(i - 1).getParentNode() == allLeafs.get(i).getParentNode()) {
                    if (allLeafs.get(i).getParentNode().getMoveScore() > move_Score) {
                        allLeafs.get(i).getParentNode().setMoveScore(move_Score);
                    }
                } else {
                    allLeafs.get(i).getParentNode().setMoveScore(move_Score);
                }

            } else if (move_Score < infinityMIN) {
                // update the lowest value
                infinityMIN = move_Score;
                // assign parent a score
                allLeafs.get(i).getParentNode().setMoveScore(move_Score);
                // if we reach a new subtree -> the chance nodes

            }
            // resetting infinityMIN for children with different parents
            if((i<allLeafs.size()-1)){
                if (allLeafs.get(i).getParentNode() != allLeafs.get(i + 1).getParentNode()) {
                infinityMIN = Double.POSITIVE_INFINITY;
                }
            }

        }

        // iterate over all chance nodes
        for (int i = 0; i < Layer_One.size(); i++) {

            double move_score = 0;
            // iterate over all children of a node
            // the move score: Sum(Prob(d_i)*minScore(i))
            for (int j = 0; j < Layer_One.get(i).getChildNode().size(); j++) {

                double dice_probs = Layer_Two.get(j).getProb();
                move_score += dice_probs * Layer_One.get(i).getChildNode().get(j).getMoveScore();

            }
            // assigning the score to the chance nodes
            Layer_One.get(i).setMoveScore(move_score);
        }
        int Ideal_move = 0;
        // iterate over all min nodes
        for (int i = 0; i < Layer_One.size(); i++) {

            double movescore = Layer_One.get(i).getMoveScore();
            if (movescore > infinityMAX) {
                // update the lowest value
                infinityMAX = movescore;
                // assign parent a score
                rootNode.setMoveScore(movescore);
                Ideal_move = i;
            }
        }
        System.out.println("the best move holds index " + Ideal_move);
      //  System.out.println("must be smaller than: " + rootNode.getLayer_One().size());

        // return the best current move
        int[][] move = rootNode.getLayer_One().get(Ideal_move).getMove();

        // the best move
        System.out.println("score of best move " + rootNode.getLayer_One().get(Ideal_move).getMoveScore());
      System.out.println("[Move 1 From: " + move[0][0] + "] [Move 1 To: " + move[0][1] + "] [Move 2 From: " + move[1][0] + "] [Move 2 To: " + move[1][1] + "]" + "[Move 3 From: " + move[2][0] + "] [Move 3 To: " + move[2][1] + "]" + "[Move 4 From: " + move[3][0] + "] [Move 4 To: " + move[3][1] + "]");

        return move;
    }

    public static void playMove(int allmoves[], gameBoard board) {

        int start = allmoves[0];
        int finish = allmoves[1];

        // doing the allmoves inside the copy board

        //Save pillars involved
        Pilars fromPillar = board.getPillars()[start];
        Pilars toPillar = board.getPillars()[finish];

        int fromStonesNum = fromPillar.getStones().size();

        //Make the respective allmoves, without checking if it is a valid allmoves.
        if (fromStonesNum > 0) {
            Stones movingChip = fromPillar.getStones().remove(fromStonesNum - 1);
            toPillar.getStones().add(movingChip);
        }
    }
}
