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
    	Util sudoku = new Util(args);
    	SudokuBoard[] board = sudoku.createSudokuBoards();

    	if (board != null) {
    		for (int i=0; i < board.length; i++) {
    			if (board[i] != null) {
    				board[i].displayGrid();
    				new Solver(board[i]).solve();
    			}
    		}
    		
    		for (int i=0; i < board.length; i++) {
    			if (board[i] != null) {
    				board[i].displayGrid();
    			}
    		}
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
