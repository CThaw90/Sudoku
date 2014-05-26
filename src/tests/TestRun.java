package tests;

import objects.*;
import util.*;

/**
 *
 * @author cthaw
 */
public class TestRun {

    /**
     * @param args the command line arguments */
    public static void main(String[] args) {
        // TODO code application logic here
    	long startTime = 0, elapsedTime = 0;
    	
    	// Parses the command line arguments and collects all data in memory
    	Util sudoku = new Util(args);
    	
    	// Constructs Sudoku Boards and stores them in an array
    	SudokuBoard[] board = sudoku.createSudokuBoards();
    	Solver solver = null;
    	
    	// Solver will solve all boards in the array one by one
    	if (board != null) {
    		for (int i=0; i < board.length; i++) {
    			if (board[i] != null) {
    				board[i].displayGrid();
    				
    				// Loads a Sudoku board in to the Solver
    				solver = new Solver(board[i]);
    				
    				// Solver Solves Board and times the performance
    				startTime = System.currentTimeMillis();
    				solver.solve();
    				elapsedTime = System.currentTimeMillis() - startTime;
    			}
    		}
    		
    		// Displays the state of all boards
    		for (int i=0; i < board.length; i++) {
    			if (board[i] != null) {
    				board[i].displayGrid();
    			}
    		}
    		
    		if (solver != null) {
    			solver.groupCandidates();
    		}
    		
    		
    		System.out.println("Solver took " + elapsedTime + "ms to solve.");
    	}
    	else {
    		System.out.println("All boards are set to null.");
    	}
    }
}
