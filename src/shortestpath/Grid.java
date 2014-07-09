/* File: Grid.java
 I affirm that this program is entirely my own work and
 none of it is the work of any other person.

 @author Fernando Campo 1299228 COP 3530 Data Structures MWF 10:45 Summer 2014
 */
package shortestpath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Grid class to analyze the shortest path and the bottle neck for a given grid
 * file and print the path.
 *
 * @author Fernando Campo 1299228
 */
public class Grid
{

    //instance varibles
    public static final int INFINITY = Integer.MAX_VALUE / 3;
    public final Square UPPER_LEFT;
    public final Square LOWER_RIGHT;
    private Square[][] squares;
    private int[][] matrix;
    private int numRows;
    private int numCols;

    /**
     * Inner class that represents a square of on the Grid.
     */
    class Square
    {

        //instance varibles
        private int row;
        private int col;
        private int distance;
        private Square prev;

        /**
         * Square constructor.
         *
         * @param r Square object's row value.
         * @param c Square object's column value.
         */
        public Square( int r, int c )
        {
            row = r;
            col = c;
            distance = INFINITY;
            prev = null;
        }

        /**
         * Method used to return a List of adjacent neighbors.
         *
         * @return Returns a List of Square objects that are the neighbors of
         * the current Square object.
         */
        public List<Square> getAdjacents()
        {
            //Finds the rows and columns before and after it with check if its
            //at the edges of the grid.
            int lowRow = ( row == 0 ) ? 0 : ( row - 1 );
            int lowCol = ( col == 0 ) ? 0 : ( col - 1 );
            int highRow = ( row == numRows - 1 ) ? row : ( row + 1 );
            int highCol = ( col == numCols - 1 ) ? col : ( col + 1 );

            List<Square> result = new ArrayList<>();

            for ( int r = lowRow; r <= highRow; ++r )
                for ( int c = lowCol; c <= highCol; ++c )
                    if ( row != r || col != c )
                        //adds the square at r,c to list.
                        result.add( squares[r][c] );
            return result;
        }

        /**
         * Overridden toString method that prints return a string of the square.
         *
         * @return
         */
        @Override
        public String toString()
        {
            return "(" + row + "," + col + ") cell is " + matrix[ row][ col];
        }

        /**
         * Accessor method that returns the previous square.
         *
         * @return Returns the previous square.
         */
        public Square getPrev()
        {
            return prev;
        }

        /**
         * Mutator method that changes the distance in a square object and set
         * the previous square.
         *
         * @param newDistance New distance to set.
         * @param prevSquare Previous square link.
         */
        public void setDistance( int newDistance, Square prevSquare )
        {
            distance = newDistance;
            prev = prevSquare;
        }

        /**
         * Accessor method that returns the distance value.
         *
         * @return Returns the distance of a square.
         */
        public int getDistance()
        {
            return distance;
        }

        /**
         * Accessor method that returns the value or cost of a square.
         *
         * @return Returns the cost
         */
        public int getCost()
        {
            return matrix[row][col];
        }

        /**
         * Mutator method that sets the cost of the square.
         *
         * @param newCost New cost to set.
         */
        public void setCost( int newCost )
        {
            matrix[row][col] = newCost;
        }

    }//end of inner class Square

    /**
     * Grid constructor. Accepts a string file name and constructs the Grid.
     *
     * @param fileName String file name of the grid file.
     */
    public Grid( String fileName )
    {
        //calls the method used to create the grid.
        buildGrid( fileName );
        squares = new Square[ numRows ][ numCols ];

        //Populates the Square 2d array with new Square objects
        for ( int r = 0; r < numRows; ++r )
            for ( int c = 0; c < numCols; ++c )
                squares[r][c] = new Square( r, c );

        //Sets what the UPPER_LEFT and LOWER_RIGHT is.
        UPPER_LEFT = squares[0][0];
        LOWER_RIGHT = squares[numRows - 1][numCols - 1];
    }

    /**
     * Private method using to read from a file and constructs the grid. Creates
     * a StringTokenizer arraylist and adds every line of the file to it. Then
     * each token(int) is added to the matrix 2d array.
     *
     * @param fileName String name of the file for the grid.
     */
    private void buildGrid( String fileName )
    {
        List<StringTokenizer> gridLines = new ArrayList<>();
        try
        {
            Scanner fileScan = new Scanner( new File( fileName ) );
            StringTokenizer line = new StringTokenizer( fileScan.nextLine() );

            numCols = line.countTokens();
            gridLines.add( line );

            while ( fileScan.hasNext() )
            {
                line = new StringTokenizer( fileScan.nextLine() );
                //check if grid rows are equal length.
                if ( line.countTokens() != numCols )
                {
                    System.err.println( "Grid is odd shape." );
                    System.exit( 0 );
                }
                else
                    gridLines.add( line );
            }

            numRows = gridLines.size();
            matrix = new int[ numRows ][ numCols ];

            //for every StringTokenizer in the array list
            //while the line has more ints.
            for ( int i = 0; i < gridLines.size(); ++i )
                while ( gridLines.get( i ).hasMoreElements() )
                    for ( int j = 0; j < numCols; ++j )
                        matrix[i][j]
                           = Integer.parseInt( gridLines.get( i ).nextToken() );

        }
        catch ( FileNotFoundException e )
        {
            System.out.println( "No grid file found." );
        }

    }

    /**
     * Computes the shortest path of the grid.
     *
     * @param start The square to start computing the path.
     */
    public void computeShortestPaths( Square start )
    {
        //initialize all distances to INFINITY.
        for ( int r = 0; r < numRows; ++r )
            for ( int c = 0; c < numCols; ++c )
                squares[r][c].setDistance( INFINITY, null );

        PriorityQueue<Square> pq = new PriorityQueue<>(
                ( lhs, rhs ) -> lhs.getDistance() - rhs.getDistance() );

        //sets the first square with a distance with 0 and add to pq.
        start.setDistance( 0, null );
        pq.add( start );

        while ( !pq.isEmpty() )
        {
            Square v = pq.remove();

            for ( Square w : v.getAdjacents() )
            {
                if ( w.getDistance() == INFINITY )
                {
                    w.setDistance( v.getDistance() + w.getCost(), v );
                    pq.add( w );
                }
            }
        }
    }

    /**
     * Computes the bottleneck of the path.
     *
     * @param start Start square.
     */
    public void computeBottleNeckPaths( Square start )
    {
        //initialize all weights to 0
        for ( int r = 0; r < numRows; ++r )
            for ( int c = 0; c < numCols; ++c )
                squares[r][c].setDistance( 0, null );

        PriorityQueue<Square> pq = new PriorityQueue<>(
                        ( lhs, rhs ) -> rhs.getDistance() - lhs.getDistance() );

        //adjust start and end square to INFINITY
        int oldLowerCost = LOWER_RIGHT.getCost();
        LOWER_RIGHT.setCost( INFINITY );
        start.setDistance( INFINITY, null );
        pq.add( start );

        while ( !pq.isEmpty() )
        {
            Square v = pq.remove();

            for ( Square w : v.getAdjacents() )
            {
                if ( w.getDistance() == 0 )
                {
                    w.setDistance( Math.min( v.getDistance(), w.getCost() ), v );
                    pq.add( w );
                }
            }
        }
        //reverts to its inital cost.
        LOWER_RIGHT.setCost( oldLowerCost );
    }//end of computeBottleNeckPaths()

    /**
     * Prints the path. If the path has more than 20 squares, print the first
     * and last 10 squares in the path.
     *
     * @param t Start square of path.
     */
    public void printPath( Square t )
    {
        List<Square> pathList = new ArrayList<>();
        pathList.add( t );
        Square prev = t.getPrev();
        while ( prev != null )
        {
            pathList.add( prev );
            prev = prev.getPrev();
        }
        if ( pathList.size() > 20 ) //path is more than 20 squares
        {
            for ( int i = pathList.size() - 1; i >= pathList.size() - 10; --i )
                System.out.println( pathList.get( i ) );
            
            System.out.println( "..." );
            
            for ( int i = 9; i >= 0; --i )
                System.out.println( pathList.get( i ) );
        }
        else //path is less than 20 squares
            for ( int i = pathList.size() - 1; i >= 0; --i )
                System.out.println( pathList.get( i ) );
    }//end of printPath()

    /**
     * Test main for Grid class. Creates grid, compute shortest and bottleneck
     * and prints paths.
     *
     * @param args Grid file.
     */
    public static void main( String[] args )
    {
        if ( args.length == 1 )
        {
            Grid g1 = new Grid( args[0] );
            
            System.out.println( "Computing Shortest Path: " );
            g1.computeShortestPaths( g1.UPPER_LEFT );
            g1.printPath( g1.LOWER_RIGHT );
            System.out.println( "Total cost is "
                    + g1.LOWER_RIGHT.getDistance() + "\n" );

            System.out.println( "Computing BottleNeck Path: " );
            g1.computeBottleNeckPaths( g1.UPPER_LEFT );
            g1.printPath( g1.LOWER_RIGHT );
            System.out.println( "All cells support "
                    + g1.LOWER_RIGHT.getDistance() );
        }
        else
            System.out.println( "Invalid args!" );
    }//end of main
}//end of Grid.java
