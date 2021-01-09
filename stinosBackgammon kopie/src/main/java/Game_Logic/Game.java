/**
 * Game class that holds the current games properties, sets the board up and directs inputs.
 * <p>
 * This class will be used to
 *
 * - hold player instances
 * - determine who's turn it is
 * - hold the board instance
 * - hold the dice instances
 *
 * @author pietro99, rdadrl
 */
package Game_Logic;

import java.util.ArrayList;

public class Game {
 // without temporay d-bugg
    //players
    private Player_AI player1;
    private Player_AI player2;
    public Player_AI selected;

    //Game Tree
    private TreeNode gametree;
    Game_Logic.gameStatus gameStatus = Game_Logic.gameStatus.getInstance();


    //moves
    private ArrayList<Integer> list_moves = new ArrayList<Integer>();



    //board
    private gameBoard board;

    //dices
    private Dices first_dice;
    private Dices second_dice;


    /**
     * Default constructor for the game
     * Also handles board initialization
     */
    public Game(Player_AI player1, Player_AI player2){
        this.player1 = player1;
        this.player2 = player2;
        selected = player1;

        board = new gameBoard(player1, player2);

        first_dice = new Dices();
        second_dice = new Dices();

        gametree = new TreeNode(true, board);
    }



    /**
     * Tries finish move start Column A finish Column B
     * <p>
     *  BEWARE! This method does not check whether this move is possible in terms of dices
     *  but rather the columns follow the same players chips!
     *
     *  This functionality is meant as a way for random sampling for AI finish just decide upon the expected dices.
     * </p>
     *
     * @param start  Starting Column index
     * @param finish    Final Column index
     *
     * @return <code>null</code>
     * @throws IllegalAccessError if the selected chip is not available finish move in that direction
     */
    public void move(int start, int finish) throws Exception {

        Player_AI FromPillar;
        Player_AI ToPillar;


        //get columns
        Pilars fromPillars = getBoard().getPillars()[start];
        Pilars toPillars = getBoard().getPillars()[finish];


        //get chip's number
        int fromStonesNum = fromPillars.getStones().size();
        int toStonesNum = toPillars.getStones().size();

        //start column cannot be empty.
        if (fromStonesNum <= 0) {
            throw new IllegalArgumentException();
        }

        //get the owners of columns
        //if there is no chip in "finish" column consider the column as owned by the player who made the move
        FromPillar = fromPillars.getStones().get(0).getPlayerAi();
        if (toStonesNum <= 0)
            ToPillar = getSelected();
        else
            ToPillar = toPillars.getStones().get(0).getPlayerAi();




        //this check must return false for every valid move (move, take, hit)
        if(Checkvalidations(FromPillar, ToPillar, fromPillars, toPillars, fromStonesNum, toStonesNum)){
            throw new IllegalArgumentException();
        }


        //normal moves check
        if(finish>=0 && finish<=23){
           if(checkLegality_moves(FromPillar,ToPillar, toStonesNum, start, finish, fromPillars, toPillars)) {
               throw new IllegalArgumentException();
           }
        }
        //take moves check
        else if(finish==26){
            if(LegalityTake(start)) {
                throw new IllegalArgumentException();
            }
        }



            if (list_moves.size() == 0)
                changePLayer();
        //finally, move the chip
        Stones movingStones = fromPillars.getStones().remove(fromStonesNum - 1);
        toPillars.getStones().add(movingStones);

        //Valid move, add move finish the game tree.
        if (gametree.getParentNode() != null) {
            TreeNode leafNode = new TreeNode(start, finish, this.getBoard());
         //  System.out.println("This is the move score: " + simple_AI.evaluateMove(start, finish, this));

            gametree.getChildNode().add(leafNode);
           // System.out.println(gametree.getChildNode().size());
         //   System.out.println("Added a new child finish tree.");

        }
       // System.out.println(gametree.toString());
        System.out.println("Move success");
     }


    public boolean LegalityTake(int x){
        //check if you can start taking chips
        if(checkTake()){

            if(Take_Dice_Legality(x))
                return false;
        }
        return true;
    }


    private boolean Take_Dice_Legality(int x){
        Pilars[] pilars = new Pilars[6];

        if(getSelected()== player1) {
            for (int i = 0; i < pilars.length; i++) {
                pilars[i] = board.getPillars()[i];
            }

            for(int i = 0; i< getList_moves().size(); i++) {

                if (x+1 == getList_moves().get(i)) {
                    list_moves.remove(i);
                    pilars[x].getStones().get(0).take();
                    return true;
                }
                else if(x+1 < getList_moves().get(i)){
                    int sumStones = 0;
                    for(int c = x+1; c< getList_moves().get(i); c++){
                       sumStones += pilars[c].getStones().size();
                    }
                    if(sumStones==0){
                        list_moves.remove(i);
                        pilars[x].getStones().get(0).take();
                        return true;
                    }
                }
            }
        }

       else if(getSelected()== player2) {
            int j = 0;
            for (int i = 18; i <= 23; i++) {
                pilars[j] = board.getPillars()[i];
                j++;
            }
            for(int i = 0; i< getList_moves().size(); i++) {
                if (24-x == getList_moves().get(i)) {
                    list_moves.remove(i);
                    pilars[x].getStones().get(0).take();
                    return true;
                }
                else if(24-x < getList_moves().get(i)){
                    int sumStones = 0;
                    for(int c = 24-x; c< getList_moves().get(i); c++){
                        sumStones += pilars[c].getStones().size();
                    }
                    if(sumStones==0){
                        list_moves.remove(i);
                        pilars[x].getStones().get(0).take();
                        return true;
                    }
                }
            }
        }

        return false;

    }

    private boolean Dice_Legality(ArrayList<Integer> list_moves, int steps) {

        if(list_moves.size()==0)
            return false;
        if(list_moves.size() == 4) {
            if (steps % list_moves.get(0) == 0 && steps / list_moves.get(0) <= 4) {
                int Used_turns = steps / list_moves.get(0);
                for (int i = 0; i < Used_turns; i++) {
                    list_moves.remove(list_moves.size() - 1);
                }
                return true;
            }
        }
        else if(list_moves.contains(steps)){
            for(int i=0; i<list_moves.size(); i++) {
                if(list_moves.get(i)==steps) {
                    list_moves.remove(i);
                    return true;
                }
            }
        }

        return false;

    }


    private boolean checkLegality_moves(Player_AI FromPillar, Player_AI toPillar, int toStonesNum, int from, int to, Pilars fPillar, Pilars tPillar){
        /**      the owner of the "from" column must be          the owner of the "to" column must be the turn player
         * /      the turn player                                 unless it has only one chip*/
        if ((!FromPillar.equals(getSelected()) || !toPillar.equals(getSelected()) && toStonesNum >= 2)) {
            gameStatus.listView.getItems().add("turn is not yours");
            return true;
        }
        if (getSelected() == player1 && from - to <= 0) {
            gameStatus.listView.getItems().add("can't go backward");
            throw new IllegalArgumentException();
        }
        if (getSelected() == player2 && from - to >= 0) {
            if (fPillar != board.getMiddlePillars()[1]) {
                gameStatus.listView.getItems().add("cant't go backward 1");
                return true;
            } else if (to >= 6) {
                gameStatus.listView.getItems().add("cant't go backward 2");
                return true;
            }
        }
        //If there is one chip, get this chip and hit it
        if (toStonesNum == 1 && tPillar.getStones().get(0).getPlayerAi() != selected)
            hitStone(tPillar);
        if (fPillar == board.getMiddlePillars()[1])
            from = -1;
        if (!Dice_Legality(list_moves, Math.abs(to - from))) {
            gameStatus.listView.getItems().add("dice Illegality accured");
            return true;
        }

        return false;
    }


    private boolean Checkvalidations(Player_AI FromPillar, Player_AI ToPillar, Pilars pilars, Pilars pilars1, int fromStones, int ToStones){

        if (getSelected() == player1) {
            if (board.getMiddlePillars()[0].getStones().size() != 0 && pilars != board.getMiddlePillars()[0]) {
                gameStatus.listView.getItems().add("you have hitten Stones");
                return true;
            }
        }

        if (getSelected() == player2) {
            if (board.getMiddlePillars()[1].getStones().size() != 0 && pilars != board.getMiddlePillars()[1]) {
                gameStatus.listView.getItems().add("you have hitten Stones");
                return true;
            }
        }

        return false;

    }




     public boolean checkTake(){
        int sumStones=0;
        if(selected == player1){
            for(int i=0; i<=5; i++){
                if(getBoard().getPillars()[i].getStones().size()!=0 && getBoard().getPillars()[i].getStones().get(0).getPlayerAi() == selected)
                    sumStones += getBoard().getPillars()[i].getStones().size();
            }
        }
        else if(selected == player2){
            for(int i=18; i<=23; i++){
                sumStones += getBoard().getPillars()[i].getStones().size();
            }
        }
        if(sumStones == (15- selected.getTakenStones()))
            return true;
        else {
            gameStatus.listView.getItems().add("can't take yet");
            return false;
        }

     }


    public void hitStone(Pilars c) {
        //Make boolean hit true for this chip
        c.getStones().get(0).hit();

        //Move this chip to the middle
        Pilars[] middle = getBoard().getMiddlePillars();
        if (c.getStones().get(0).getPlayerAi() == player1) {
            player1.setHitStones(true);
            middle[0].getStones().add(c.getStones().get(0));
        }
        else {
            player2.setHitStones(true);
            middle[1].getStones().add(c.getStones().get(0));
        }

        c.getStones().remove(0);
    }

   // rolles both dices
    public void ROll_ALL_DICES(){
        first_dice.roll_dice();
        second_dice.roll_dice();

        System.out.println(first_dice.getNumber());
        System.out.println(second_dice.getNumber());

        list_moves.add(first_dice.getNumber());
        list_moves.add(second_dice.getNumber());

        if(first_dice.getNumber()== second_dice.getNumber()) {
            list_moves.add(first_dice.getNumber());
            list_moves.add(second_dice.getNumber());
        }
    }

    public void setALL_DICES(int a, int b) {
        list_moves.clear();
        list_moves.add(a);
        list_moves.add(b);

        if(a == b) {
            list_moves.add(a);
            list_moves.add(a);
        }
    }

    /*......................GETTERS AND SETTERS...................................................*/

    /**
     * Getter for the @board instance
     *
     * @return Board instance
     */
    public gameBoard getBoard(){
        return board;
    }

    public void setBoard(gameBoard board){ this.board = board; }
    /**
     * Getter for the @turn instance
     *
     * @return Player that has the turn
     */
    public Player_AI getSelected(){
        return selected;
    }

    public void changePLayer(){
        if(getSelected() == player1)
            this.selected = player2;
        else
            this.selected = player1;
        list_moves.clear();
    }

    public int[] getNum_Dice(){
        int[] ints = new int[2];

        ints[0] = first_dice.getNumber();
        ints[1] = second_dice.getNumber();

        return ints;
    }

    public Dices[] getDices() {
        Dices[] dices = new Dices[2];
        dices[0] = first_dice;
        dices[1] = second_dice;
        return dices;
    }
    public void setALL_DICES(Dices[] in) {
        first_dice = in[0];
        second_dice = in[1];
    }

    public ArrayList<Integer> getList_moves(){
        return list_moves;
    }
    public Player_AI getPlayer1() { return player1; }
    public Player_AI getPlayer2() { return player2; }

    public TreeNode getGametree() {
        return gametree;
    }
}
