package AIvsAI_TESTS;

import Game_Logic.gameStatus;
import Game_Logic.*;
import javafx.scene.control.ListView;

import java.util.ArrayList;


public class allCombinations {
    public static void main(String[] args) {
        Player_AI a = new Player_AI("Stijn");
        Player_AI b = new Player_AI("Adrianne");

        //init fx so we can still use LogBox without further manipulating the code. hacky. dont judge me.
        com.sun.javafx.application.PlatformImpl.startup(()->{});

        gameStatus state = gameStatus.getInstance();
        state.listView = new ListView();

        /**
         * Notes:
         * The test case checks for all dice combinations allCombinations() method.
         * The test case checks only for the initial game state.
         * This can be improved by randomly placing checkers onto the game board and re-iterating.
         *
         * The test case uses the already implemented move methods (so it relies on the fact that they're working) found on Game class.
         *
         * The test case reports anytime an impossible move is found.
         */
        Player_AI turn = a;
        System.out.println("Testing all cases for player " + turn.getID());

        for (int i = 0; i < Analyser.DICE_POSSIBILITIES.length; i++) {
            System.out.println("Testing for dices " + Analyser.DICE_POSSIBILITIES[i][0] + " and " + Analyser.DICE_POSSIBILITIES[i][1] + ".");
            Game oG = new Game(a, b);
            state.initGameState(oG);
            oG.getBoard().take_init(oG.getBoard().getPillars(), a, b);
            gameBoard root = oG.getBoard();
            oG.setALL_DICES(Analyser.DICE_POSSIBILITIES[i][0], Analyser.DICE_POSSIBILITIES[i][1]);

            ArrayList<int[][]> aC = Analyser.allCombos(root, oG.getList_moves(), a);
            for (int m = 0; m < aC.size(); m++) {
                System.out.printf("Checking combination {");
                for (int j = 0; j < aC.get(m).length; j++) {
                    System.out.printf("[" + aC.get(m)[j][0] + ", " + aC.get(m)[j][1] + "]");
                }
                System.out.printf("}: ");

                //update moves list and the turn back
                oG.setALL_DICES(Analyser.DICE_POSSIBILITIES[i][0], Analyser.DICE_POSSIBILITIES[i][1]);
                oG.selected = turn;

                try {
                    for (int j = 0; j < aC.get(m).length; j++) {
                        oG.move(aC.get(m)[j][0], aC.get(m)[j][1]);
                    }
                    System.out.printf("\tvalid.\n");
                }
                catch (Exception e) {
                    System.out.printf("\tINVALID!\n");
                    e.printStackTrace();
                }
                //reset the board
                oG.setBoard(new gameBoard(a, b));
            }
        }

        //kill fx
        com.sun.javafx.application.PlatformImpl.exit();
    }

    //TODO
    private gameBoard randomBoard(Player_AI a, Player_AI b) {
        gameBoard res = new gameBoard(a, b);

        res.getPillars();
        return res;
    }
}
