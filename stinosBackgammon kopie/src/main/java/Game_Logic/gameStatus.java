package Game_Logic;


import javafx.scene.control.ListView;


public class gameStatus {
    private static gameStatus status = null;
    public static ListView listView;

    public static Game game;
    public static Player_AI plauer_1;
    public static Player_AI player_2;
    public static TreeNode treeNode;



    private gameStatus() {

    }

    public void initGameState(Game game1){
        game = game1;
        plauer_1 = game.getPlayer1();
        player_2 = game.getPlayer2();
        treeNode = game.getGametree();
    }


    public static gameStatus getInstance() {
        if(status == null){
            return new gameStatus();
        }
        return status;
    }

    public Game getGame(){
        return game;
    }

}
