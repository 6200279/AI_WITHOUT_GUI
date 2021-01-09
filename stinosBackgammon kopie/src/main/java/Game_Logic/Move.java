package Game_Logic;

public class Move {
    private int CurrentNode;
    private int nextNode;
    private int distance;
    private boolean aBoolean;
    private boolean aBoolean1;

    public Move(int from, int to){
        if (from == 24 || from == 25){
            aBoolean = true;
        }
        if(to == 26){
            aBoolean1 = true;
        }
        this.CurrentNode = from;
        this.nextNode = to;
        this.distance = Math.abs(from-to);
    }

    public int getNextNode() {
        return nextNode;
    }

    public int getCurrentNode() {
        return CurrentNode;
    }

    public int getDistance() {
        return distance;
    }
}
