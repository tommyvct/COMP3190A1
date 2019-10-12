import java.util.ArrayList;

/**
 * A1Q2
 */
public class A1Q2 {
    public static void main(String[] args) {
        
    }
}

public enum BoardNode
{
    COST1 = 1;
    COST2 = 2;
    COST3 = 3;
    COST4 = 4;
    COST5 = 5;
    WALL = 99;
}

public enum Direction
{
    N = {}
}

/**
 * Board
 */
public class Board 
{
    private BoardNode[][] board;
    private int[] agentLocation;
    private ArrayList<int[]> bombLocation;


    
}