package Game_Logic;

public class MSCTTest {
    public static void main(String[]args){
        Player_AI monteCarlo = new MCST__UCT();
        Player_AI p2 = new Player_AI();
        Game game = new Game(monteCarlo, p2);

        monteCarlo = (MCST__UCT)monteCarlo;

        ((MCST__UCT) monteCarlo).choice__maker(game);
    }
}
