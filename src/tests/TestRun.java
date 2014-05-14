/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tests;

import objects.*;
import util.*;

/**
 *
 * @author cthaw
 */
public class TestRun {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SudokuBoard board = new SudokuBoard();
        Solver solver = new Solver(board);
        board.displayGrid();
        board.assertUnique(0);
        
        solver.solve();
        board.displayGrid();
        solver.displayAllCandidates();
    //    System.out.println(nakeds.values.get(2));
    }
    
}
