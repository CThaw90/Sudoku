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
    	Util sudoku = new Util(args);
    	SudokuBoard[] board = sudoku.createSudokuBoards();
    	Solver solver = null;
    	if (board != null) {
    		for (int i=0; i < board.length; i++) {
    			if (board[i] != null) {
    				board[i].displayGrid();
    				
    				solver = new Solver(board[i]);
    				startTime = System.currentTimeMillis();
    				solver.solve();
    				elapsedTime = System.currentTimeMillis() - startTime;
    			}
    		}
    		
    		for (int i=0; i < board.length; i++) {
    			if (board[i] != null) {
    				board[i].displayGrid();
    			}
    		}
    		
    		if (solver != null) {
    		//	solver.groupCandidates();
    		}
    		
    		
    		System.out.println("Solver took " + elapsedTime + "ms to solve.");
    	}
    	else {
    		System.out.println("All boards are set to null.");
    	}
    //	System.out.println(System.getProperty("user.dir"));
    //	FileIO io = new FileIO(args[0]);
    //	System.out.println("Sending arg " + args[0]);
    //	io.loadPathLocation(args[0]);
    //	String[] data = io.loadAllBoardData();
    	
    //	for (int i=0; i<data.length; i++)
    //		System.out.println("Board Data " + i + ": " + data[i]);
       // Solver solver = new Solver(board);
       // board.displayGrid();
      //  board.assertUnique(0);
        
      //  solver.solve();
     //   board.displayGrid();
    //    solver.displayAllCandidates();
    }
}
