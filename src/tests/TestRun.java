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
    	//Util sudoku = new Util(args);
       // SudokuBoard[] board = sudoku.createSudokuBoards();
    	FileIO io = new FileIO(args[0]);
    	System.out.println("Sending arg " + args[0]);
    //	io.loadPathLocation(args[0]);
    	String[] data = io.loadAllBoardData();
    	
    	for (int i=0; i<data.length; i++)
    		System.out.println("Board Data " + i + ": " + data[i]);
       // Solver solver = new Solver(board);
       // board.displayGrid();
      //  board.assertUnique(0);
        
      //  solver.solve();
     //   board.displayGrid();
    //    solver.displayAllCandidates();
    }
    
}
