package AIvsAI_TESTS;

import Game_Logic.gameStatus;
import Game_Logic.*;
import javafx.scene.control.ListView;

public class AIvsAItest {
    public static void main (String[] args) throws Exception {
        double startTime = System.nanoTime();
        //Player_AI p1 = new MCST__UCT();
        //Player_AI p1 = new simple_ai_2();
        //Player_AI p1 = new Mini_Max_AI();
        Player_AI p1 = new Randomness_AI();
        // Player_AI p2 = new simple_ai_2();
        //Player_AI p2 = new Mini_Max_AI();
        Player_AI p2 = new Randomness_AI();

        Game game = new Game(p1, p2);
        //((Mini_Max_AI) p1).setCurrent_Game_Instance(game);
        //((simple_ai_2) p1).setCurrent_Game_Instance(game);
        ((Randomness_AI) p1).setCurrent_Game_Instance(game);
        //((MCST__UCT) p1).setCurrent_Game_Instance(game);
        //((simple_ai_2) p2).setCurrent_Game_Instance(game);
        //((Mini_Max_AI) p2).setCurrent_Game_Instance(game);
        ((Randomness_AI) p2).setCurrent_Game_Instance(game);

        System.out.println(p1.getPlayer_name() + " vs " + p2.getPlayer_name());

        game.ROll_ALL_DICES();
        int d1 = game.getNum_Dice()[0];
        int d2 = game.getNum_Dice()[1];

        System.out.println("Rolling dices to choose starting player...");
        System.out.println(p1.getPlayer_name() + " rolled a " + d1);
        System.out.println(p2.getPlayer_name() + " rolled a " + d2);

        if (d1 > d2) {
            System.out.println(p1.getPlayer_name() + "'s turn");
            game.selected = p1;
        } else {
            System.out.println(p2.getPlayer_name() + "'s turn");
            game.selected = p2;
        }

        gameStatus state = gameStatus.getInstance();
        state.initGameState(game);
        //init fx
        com.sun.javafx.application.PlatformImpl.startup(() -> {
        });

        //set log box as a new listview instance.
        state.listView = new ListView();
        int round = 0;
        int MAXround = 50;
        boolean a =Analyser.game_finished(game.getBoard());
        while (!a) {
            // System.out.println(game.getBoard().toString());
            if(round > MAXround) a = true;
                try {
                    System.out.println(game.selected.getPlayer_name() + "'s move (round " + round++ + ")");
                    ((simple_AI) game.selected).PlayOutMoves();
                } catch (Exception e) {
                    for (int i = 0; i < state.listView.getChildrenUnmodifiable().size(); i++) {
                        System.out.println("Ouch!");
                        System.out.println(state.listView.getChildrenUnmodifiable().get(i).toString());
                    }
                }
                System.out.println(game.getBoard().toString());
                game.changePLayer();
            }

            //kill fx
            com.sun.javafx.application.PlatformImpl.exit();
            double endTime = System.nanoTime();
            double totalTime = (endTime - startTime) / 1000000000;

            System.out.println(" ");
       
            System.out.println(" ");
            System.out.println("runtime:" + totalTime + " seconds");

        }
    }


