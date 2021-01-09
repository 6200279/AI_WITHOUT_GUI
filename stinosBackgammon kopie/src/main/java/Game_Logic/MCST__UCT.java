package Game_Logic;


import java.util.ArrayList;

/**
 *      Monte-Carlo Search Tree w UCT
 *
 */

public class MCST__UCT extends simple_AI {
  static double aDouble = 0.4;
    TreeNode Chosen_Node;
    TreeNode Root_Node;
    @Override
    public void initialization(Game g) {
        //some initialization which is called once it's our turn.
        Root_Node = Mini_Max_AI.buildTree(g);
        Chosen_Node = select(Root_Node, g);
        gameBoard b= g.getBoard();

    }

    @Override
    int[] choice__maker(Game g) {
        Root_Node = Mini_Max_AI.buildTree(g);
        Chosen_Node = select(Root_Node, g);

        if(Chosen_Node.getChildNode().size()==0){
            if(Chosen_Node.getn()==0){
                treeTransversal(Chosen_Node, g);
            }
            else{
                return choice__maker(g);
            }
        }
        backTracker(Root_Node, g);

        return new int[0];
    }

    public TreeNode treeTransversal(TreeNode treeNode, Game game) {

        TreeNode current_leaf = treeNode;
        Player_AI P1 = this;
        Player_AI P2 = game.getPlayer2();
        if (P2 == P1) P2 = game.getPlayer1();

        int runtime = 0;
        while (!Analyser.game_finished(game.getBoard())) {
            System.out.println("runtime: " + runtime++);
            int diceInterger = (int) Math.round((Analyser.DICE_POSSIBILITIES.length-1) * Math.random());
            int[] Current_Dices = Analyser.DICE_POSSIBILITIES[diceInterger];
            ArrayList<Integer> list_moves = new ArrayList<>();
            list_moves.add(Current_Dices[0]);
            list_moves.add(Current_Dices[1]);

            if (Current_Dices[0] == Current_Dices[1]) {
                list_moves.add(Current_Dices[0]);
                list_moves.add(Current_Dices[0]);
            }

            ArrayList<int[][]> allCombos = Analyser.allCombos(treeNode.getBoard(), list_moves, P1);
            gameBoard Ideal_Board = null;
            double Ideal_Score = Double.NEGATIVE_INFINITY;
            int[][] Ideal_move = null;

            gameBoard[] playoutGenerator = new gameBoard[allCombos.size()];
            //Generate playout board instances to then evaluate
            for (int i = 0; i < playoutGenerator.length; i++) {
                gameBoard temp_B = game.getBoard().BoardCopy(current_leaf.getBoard(), game);

                int[][] moves_available = allCombos.get(i);
                Pilars[] pillars = temp_B.getPillars();
                for (int j = 0; j < moves_available.length; j++) {
                    Pilars FromPillar = pillars[moves_available[j][0]];
                    Pilars ToPillar = pillars[moves_available[j][1]];

                    if(FromPillar.getStones().size() > 0) {
                        Stones StoneOnHand = FromPillar.getStones().get(0);
                        FromPillar.getStones().remove(StoneOnHand);

                        ToPillar.getStones().add(StoneOnHand);
                    }
                }
                playoutGenerator[i] = temp_B;
                double game_evaluation = simple_AI.Game_Evaluation(P1, P2, temp_B);
                if (game_evaluation > Ideal_Score) {
                    Ideal_Board = temp_B;
                    Ideal_Score = game_evaluation;
                    Ideal_move = moves_available;
                }
            }
            System.out.println("the for loop ends up...");
          if (Analyser.compares(Ideal_Board, current_leaf.getBoard())) System.out.println("it's the same board, man!");
            TreeNode newLeafNode = new TreeNode(Ideal_move, Ideal_Board);
            newLeafNode.setParent(current_leaf);
            current_leaf.addChild(newLeafNode);

            //finally, done with the move.
            current_leaf = newLeafNode;

            //change P1 so that we compute for P2
                Player_AI temp = P1;
                P1 = P2;
                P2 = temp;
        }

        return treeNode;
    }

    public TreeNode select(TreeNode rootNode, Game game) {

        ArrayList<TreeNode> allLeafs = rootNode.getAllLeafs();

        TreeNode node=allLeafs.get(0);
        double maxUCT = node.getUCTValue();
        for (int i=1; i<allLeafs.size();i++) {
            allLeafs.get(i).setUCTValue(UCT(allLeafs.get(i).getMoveScore(), aDouble, allLeafs.get(i).getn(), allLeafs.get(i).getN()));
            if (allLeafs.get(i).getUCTValue() > maxUCT) {
                maxUCT = allLeafs.get(i).getUCTValue();
                node = allLeafs.get(i);
            }
        }
        return node;
    }


    public void backTracker(TreeNode node, Game game) {
        node.setScoreMCTS(simple_AI.Game_Evaluation(this, game.getPlayer2(), node.getBoard()));
        node.visited();

        if (!node.isRoot()) backTracker(node.getParentNode(), game);
    }


    public double UCT(double v, double C, int n, int N){
        double UCT = (v+(C*(Math.sqrt(Math.log(N)/n))));
        if(n==0)
            UCT = 999999999;
        return UCT;
    }

    public MCST__UCT() {
        super("MonteCarlo");
    }
}
