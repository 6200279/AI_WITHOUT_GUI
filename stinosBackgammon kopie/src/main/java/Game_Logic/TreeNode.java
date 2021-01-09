


// each node consists of a singele move
// with a tree of nodes we can
// - back track the game
// - undo moves
// - save game
// - predict the future moves

package Game_Logic;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

// instance fields


    //Player instance
    Player_AI player;

    private int die1;
    private int die2;


    //Evaluated score
    public double score;
    //From-to column ID's
    int from;
    int to;

    private boolean isRoot = false;
    // stores the score assigned by the method evaluateMove
    double moveScore;

    //If the chip was used to hit an opponent chip- mark.
    boolean hitChip;

    int visitCoun;

    public List<TreeNode> children = new ArrayList<>();

    private ArrayList<TreeNode> allLeafs = new ArrayList<>();

    private ArrayList<TreeNode> secondLayer = new ArrayList<>();

    private ArrayList<TreeNode> firstLayer = new ArrayList<>();

    private TreeNode parent = null;

    private double prob;

    private gameBoard board;

    private int[][] move;

    private static int counter;

    private int id = 0;

    private int n=0;
    private int N = 0;
    private double UCTValue = 0;


// constructors


    //chance Tree node
    public TreeNode(int[] diceCombination, gameBoard aBoard, Double prob){


        this.die1 = diceCombination[0];
        this.die2 = diceCombination[1];

        this.board = aBoard;
        this.prob = prob;
        this.id = counter;
        counter++;
    }


    public TreeNode(int[][] move, gameBoard aBoard) {


        this.board  = aBoard;
        this.move = move;
        this.id = counter;
        counter++;
        System.out.println("[Move 1 From: " + move[0][0] + "] [Move 1 To: " + move[0][1] + "] [Move 2 From: " + move[1][0] + "] [Move 2 To: " + move[1][1] + "]");


    }
    public TreeNode(int from, int to, gameBoard aBoard){

        this.from = from;
        this.to = to;
        this.board = aBoard;
        this.id = counter;
        counter++;
    }

    // constructor that is called when root is created
    public TreeNode(boolean isRoot, gameBoard aBoard) {
        this.isRoot = isRoot;
        this.board = aBoard;
        player = gameStatus.plauer_1;
        this.id = counter;
        counter++;
    }

// mutator methods

    // creating lists of every tree node in every level (except root)
    public void addLeaf(TreeNode leaf){
        allLeafs.add(leaf);
    }
    public void addLayer_Two(TreeNode secondLayerNode){
        secondLayer.add(secondLayerNode);
    }
    public void addLayer_ONE(TreeNode firstLayerNode){
        firstLayer.add(firstLayerNode);
    }

    //basic method of adding moves
    public TreeNode addChild(TreeNode child){

        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public Player_AI getPlayer() {
        return player;
    }

    public int[][] getMove(){
        return move;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public List<TreeNode> getChildNode() { return children; }

    public void setMoveScore(double moveScore){
        this.moveScore  = moveScore;
    }

    public double getMoveScore(){ return moveScore; }

    // some basic method we might need
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode getParentNode() {
        return parent;
    }

    public ArrayList<TreeNode> getAllLeafs(){ return allLeafs; }
    public ArrayList<TreeNode> getSecondLayer(){ return secondLayer; }
    public ArrayList<TreeNode> getLayer_One(){ return firstLayer; }

    public Double getProb(){ return prob; }

    public void setProb(double prob){
        this.prob = prob;
        System.out.println("probability: " + prob);
    }

    public gameBoard getBoard(){ return board; }

    public void setBoard(gameBoard aBoard){ this.board = aBoard; }

    public int getId(){ return id; }


    // returns depth of the tree
    public int depth() {
        int counter = 0;
        if (counter > 0) {
            while (parent.getChildNode().size() > 0) {
                counter++;
                parent = parent.getChildNode().get(0);
            }
            return counter;
        } else {
            return 0;
        }
    }

    public int getn(){
        return n;
    }
    public int getN(){
        if (!this.isRoot && this.getParentNode()!= null) {
            return this.getParentNode().getn();
        }
        return 0;
    }

    public void visited(){
        n++;
    //    if(!this.isRoot)
      //   this.getParent().visited();
    }



    public double getUCTValue(){
        return UCTValue;
    }
    public void setUCTValue(double UCT){
        UCTValue = UCT;
    }

    public void setScoreMCTS(double score){
        this.moveScore  = score;
        if (!this.isRoot) {
            this.getParentNode().setMoveScore(this.getParentNode().getMoveScore() + score);
            this.getParentNode().setScoreMCTS(score);
        }
    }


    public String toString(){
        String line = "";
        int counter = 0;

        line += "Depth: " + depth();
        if(counter > 0) {
            while (parent.getChildNode().size() > 0) {

                line += "[Move: " + counter + ", Player: " + parent.player.getPlayer_name() + "]";

            }

            line += "[Parent: " + this.parent + ", From: " + this.from + ", To: " + this.to + "]";

            return line;
        }
        else{
            return "[Root: " + this.parent + "]";
        }

    }
}
