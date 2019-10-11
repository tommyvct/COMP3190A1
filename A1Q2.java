/**
 * A1Q2
 */
public class A1Q2 {

    
}

class Board 
{
    private int[][] board;
    private int[] start;
    private int[] end;
    private int[] agent;

    public Board(int row, int column, String maze)
    {
        this.board = new int[row][column];
        this.start = new int[2];
        this.end = new int[2];
        this.agent = new int[2];

        int counter = 0;
        char[] mazeCharArray = maze.replaceAll("\n", "").toCharArray();

        for (int i = 0; i < row; i++) 
        {
            for (int j = 0; j < column; j++) 
            {
                this.board[i][j] = mazeCharArray[counter];
            }
        }


    }


}