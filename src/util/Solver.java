package util;

import java.util.LinkedList;

import objects.NakedCandidates;
import objects.SudokuBoard;

public class Solver {
	
	static String UNSOLVED = new String("Puzzle could not be Solved!");
	static String SOLVED = new String("Puzzle Solved!");
	public LinkedList<NakedCandidates> nakeds;
	public NakedCandidates candidate;
	String[][] sudokuGrid;
	int[][] boardGrid;
	SudokuBoard board;
	boolean solved;
	
	public Solver() {}
	
	public Solver(SudokuBoard board) {
		sudokuGrid = board.currentGridState();
		boardGrid = board.currentBoardState();
		this.board = board;
		solved = false;
	}
	
	public void loadSudokuPuzzle(SudokuBoard board) {
		sudokuGrid = board.currentGridState();
		boardGrid = board.currentBoardState();
		this.board = board;
		solved = false;
	}
	
	public void solve() {
		
		if (solved) {
			System.out.println("Sudoku Puzzle already Solved!");
			return;
		} 
		
		if (board == null) {
			System.out.println("No Puzzle Loaded");
			return;
		}

		
		extractCandidates();
		
		solved = (solved) ? solved : nakedSingleSolver();
		
		System.out.println(solved ? SOLVED : UNSOLVED);
	}
	
	public void displayAllCandidates() {
		
		if (nakeds == null) {
			System.out.println("No Candidates Stored");
			return;
		}
		
		for (int i=0; i < nakeds.size(); i++) {
			NakedCandidates current = nakeds.get(i);
			System.out.println("Eval at Coordinate x="+current.x+", y="+current.y);
			for (int j=0; j < current.values.size(); j++) {
				System.out.print(current.values.get(j) + " ");
			}
			
			System.out.println();
		}
	}
	
	private boolean nakedSingleSolver() {
		
		boolean solvable = true;
		
		while (nakeds.size() > 0 && solvable) {
			
			solvable = false;
			int i=0;
			
			while (i < nakeds.size()) {
				NakedCandidates candidate = nakeds.get(i);
				if (candidate.values.size() == 1) {

					board.setValue(candidate.x, candidate.y, candidate.values.get(0));
					extractCandidates();
					solvable = true;
				//	nakeds.remove();
				} 
				else { i++; }
			}
		}
		
		return solvable;
	}
	
	private void extractCandidates() {
		if (nakeds != null)
			System.out.println("Extracting Candidates from " + nakeds.size() + " coords");
		
		nakeds = new LinkedList<NakedCandidates>();
		int length = board.size*board.size;
		for (int x=0; x < length; x++) {
			for (int y=0; y < length; y++) {

				if (board.getStringValue(x, y).equals(new String("*"))) {
					
					System.out.println("Entered Eval at coordinate x="+x+" y="+y);
					candidate = new NakedCandidates(board.size*board.size);
					candidate.x = x;
					candidate.y = y;
					
					for (int i=1; i <= length; i++) {

						if (!board.duplicateEntryColumn(y, String.valueOf(i)) 
								&& !board.duplicateEntryRow(x, String.valueOf(i))
								&& !board.duplicateEntrySection(x, y, String.valueOf(i))) {
							
							candidate.values.add(String.valueOf(i));
							System.out.print((i)+ " ");
						}
					}
					
					nakeds.add(candidate);
					System.out.println();
				}
			}
		}
	}
}
