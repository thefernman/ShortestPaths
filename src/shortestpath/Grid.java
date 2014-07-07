/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestpath;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fernando
 */
public class Grid
{

    public static final int INFINITY = Integer.MAX_VALUE / 3;
    public final Square UPPER_LEFT;
    public final Square LOWER_LEFT;
    public final Square UPPER_RIGHT;
    private Square[][] squares;
    private int[][] matrix;
    private int numRows;
    private int numCols;

    class Square
    {

        int row;
        int col;
        int distance;
        Square prev;

        public Square( int r, int c )
        {
            row = r;
            col = c;
            distance = INFINITY;
            prev = null;
        }

        public List<Square> getAdjacents()
        {
            int lowRow = ( row == 0 ) ? 0 : ( row - 1 );
            int lowCol = ( col == 0 ) ? 0 : ( col - 1 );
            int highRow = ( row == numRows - 1 ) ? row : ( row + 1 );
            int highCol = ( col == numCols - 1 ) ? col : ( col + 1 );

            List<Square> result = new ArrayList<>();

            for ( int r = lowRow; r <= highRow; ++r )
            {
                for ( int c = lowCol; c <= highCol; ++c )
                {
                    if ( r != row || c != col )
                        result.add( new Square( r, c ) );
                }
            }
            return result;
        }

        public String toString()
        {
            return "(" + row + "," + col + ") cell is " + matrix[ row ][ col ];
        }

        public Square getPrev()
        {
            return prev;
        }

        public void setDistance( int newDistance, Square prevSquare )
        {
            distance = newDistance;
            prev = prevSquare;
        }

        public int getDistance()
        {
            return distance;
        }
        
        public int getCost()
        {
            return matrix[row][col];
        }
        
        public void setCost(int newCost)
        {
            matrix[row][col] = newCost;
        }
        
    }

    public Grid()
    {
        
    }

}
