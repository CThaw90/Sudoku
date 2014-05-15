package util;

import java.util.LinkedList;

import objects.NakedCandidates;
import objects.SudokuBoard;

public class Solver {
	
	static String UNSOLVED = new String("No Solution for this Puzzle!");
	static String SOLVED = new String("Puzzle Solved!");
	public LinkedList<NakedCandidates> nakeds;
	public NakedCandidates candidate;
	SudokuBoard board;
	boolean solved;
	
	public Solver() {}
	
	public Solver(SudokuBoard board) {
		this.board = board;
		solved = false;
	}
	
	public void loadSudokuPuzzle(SudokuBoard board) {
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
	/* *
		int iteration=0;
		while (!solved && iteration < 2) {
			solved = (solved) ? solved : nakedSingleSolver();
			solved = (solved) ? solved : nakedPairSolver();
			solved = (solved) ? solved : nakedTripleSolver();
			iteration++;
		}
	/* */	
		solved = (solved) ? solved : bruteForceSolver(0);
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
					nakeds.remove(i);
					board.setValue(candidate.x, candidate.y, candidate.values.get(0));
					System.out.println("Setting Value " + candidate.values.getFirst() + " at " + candidate.x + ", " + candidate.y);
					removeAffectedCandidates(candidate);
					solvable = true;
				} 
				else { i++; }
			}
		}
		
		return solvable;
	}
	
	private boolean nakedPairSolver() {
		LinkedList<Integer> values = new LinkedList<Integer>();
		for (int index=0; index < nakeds.size(); index++) {
			NakedCandidates current = nakeds.get(index);
			if (current.values.size() == 2 && !passOver(values, index)) {
				System.out.println("Possible Pair Candidate found at " + current.x + ", " + current.y);
				displayValues("Current Values", current.values);
				int j = checkForNakedPairs(current.x, current.y, index);
				values.add(j);
			}
		}
		
		return nakedSingleSolver();
	}
	
	private boolean nakedTripleSolver() {
		return nakedPairSolver();
	}
	
	private boolean bruteForceSolver(int index) {
		
		boolean solvable = false;
		NakedCandidates candidate = nakeds.get(index);
		for (int i=0; i < candidate.values.size() && !solvable; i++) {
			
			if (!board.duplicateEntryColumn(candidate.y, candidate.values.get(i)) 
					&& !board.duplicateEntryRow(candidate.x, candidate.values.get(i))
					&& !board.duplicateEntrySection(candidate.x, candidate.y, candidate.values.get(i))) {
				
	//			System.out.println("Setting down value " + candidate.values.get(i) + " at coordinate ("+candidate.x+", "+candidate.y+")");
				board.setValue(candidate.x, candidate.y, candidate.values.get(i));
				solvable = (index < nakeds.size()-1 ? bruteForceSolver(index+1) : board.isSolved());
			}
	//		else {
	//			int x = candidate.x;
	//			int y = candidate.y;
	//			String v = candidate.values.get(i);
		//		System.out.println("Duplicate Value in " + v + " at ("+x+", "+y+").");
	//		}
		}
		
//		System.out.println((index > 0 ? "UNWINDING ONE LEVEL" : "BRUTE FORCE SOLVER ENDING"));
		if (!solvable) { board.setValue(candidate.x, candidate.y, new String("*")); }
		return solvable;
	}
	
	private void removeAffectedCandidates(NakedCandidates candidate) {
		
		System.out.println("Removing candidate at coordinate ("+candidate.x+", "+candidate.y+") with value " + candidate.values.get(0));
		
		int index=0;
		while (index < nakeds.size()) {
			NakedCandidates operator = nakeds.get(index);
			
			if (candidate.x == operator.x) {
				System.out.println("Potential match values in the same row at ("+operator.x+", "+operator.y+")");
				displayValues("Candidate Values", candidate.values);
				displayValues("Operator Values", operator.values);
				deleteMatchingValues(candidate, operator);
				displayValues("Result Values", operator.values);
			}
			
			else if (candidate.y == operator.y) {
				System.out.println("Potential match values in the same column at ("+operator.x+", "+operator.y+")");
				displayValues("Candidate Values", candidate.values);
				displayValues("Operator Values", operator.values);
				deleteMatchingValues(candidate, operator);
				displayValues("Result Values", operator.values);
			}
			
			if (sameSection(candidate, operator)) {
				System.out.println("Potential match values in the same section at ("+operator.x+", "+operator.y+")");
				displayValues("Candidate Values", candidate.values);
				displayValues("Operator Values", operator.values);
				deleteMatchingValues(candidate, operator);
				displayValues("Result Values", operator.values);
			}
			
			if (operator.values.size() == 0) {
				nakeds.remove(index);
			}
			else { index++; }
		}
	}
	
	private boolean passOver(LinkedList<Integer> values, int value) {
		
		boolean passOver = false;
		for (int index=0; index < values.size() && !passOver; index++) {
			passOver = (values.get(index) == value ? true : false);
		}
		return passOver;
	}
	
	private void displayValues(String title, LinkedList<String> values) {
		
		System.out.print(title + ":");
		for (int i=0; i < values.size(); i++) {
			System.out.print(" " + values.get(i));
		}
		System.out.println();
	}
	
	private int checkForNakedPairs(int x, int y, int i) {
		
		int pairIndex = -1;
		
		LinkedList<String> comparator = nakeds.get(i).values;
		for (int index=i; index < nakeds.size() && pairIndex < 0; index++) {
			NakedCandidates current = nakeds.get(index);
			if (current.values.size() == 2 && index != i) {
				System.out.println("Possible Match for ("+x+", "+y+") at ("+current.x+", "+current.y+")");
				LinkedList<String> candidate = current.values;
				displayValues("Current Values", comparator);
				displayValues("Compare Values", candidate);
				pairIndex = (comparator.get(0).equals(candidate.get(0)) &&
							 comparator.get(1).equals(candidate.get(1)) ? index : -1);
				
				boolean removed = (pairIndex > -1) ? removeCandidatePairs(nakeds.get(i), current) : false;
				pairIndex = (removed ? pairIndex : -1);
			}
		}
		
		return pairIndex;
	}
	
	private boolean removeCandidatePairs(NakedCandidates current, NakedCandidates comparator) {

		boolean cp = (current.y == comparator.y ? true : false);
		boolean rp = (current.x == comparator.x ? true : false);
		boolean sp = sameSection(current, comparator);
		boolean removed = false;
		
		System.out.println("Match Found for ("+current.x+", "+current.y+") at ("+comparator.x+", "+comparator.y+")");
		for (int index=0; index < nakeds.size() && (sp || cp || rp); index++) {
			
			if (!nakeds.get(index).equals(current) && !nakeds.get(index).equals(comparator)) {
				NakedCandidates naked = nakeds.get(index);
				boolean d = false;
				
				if (rp && current.x == naked.x) {
					System.out.println("Potential match values in the same row at ("+naked.x+", "+naked.y+")");
					displayValues("Current Values", current.values);
					displayValues("Naked Values", naked.values);
					d = deleteMatchingValues(current, naked);
					displayValues("Result Values", naked.values);
					removed = (removed ? removed : d);
				}
				
				if (cp && current.y == naked.y) {
					System.out.println("Potential match values in the same column at ("+naked.x+", "+naked.y+")");
					displayValues("Current Values", current.values);
					displayValues("Naked Values", naked.values);
					d = deleteMatchingValues(current, naked);
					displayValues("Result Values", naked.values);
					removed = (removed ? removed : d);
				}
				
				if (sp && sameSection(current, naked)) {
					System.out.println("Potential match values in the same section at ("+naked.x+", "+naked.y+")");
					displayValues("Current Values", current.values);
					displayValues("Naked Values", naked.values);
					d = deleteMatchingValues(current, naked);
					displayValues("Result Values", naked.values);
					removed = (removed ? removed : d);
				}
			}
		}
		
		return removed;
	}
	
	private boolean sameSection(NakedCandidates c1, NakedCandidates c2) {
		int s = board.size;
	//	return Math.abs(c1.x-c2.x) < board.size && Math.abs(c1.y-c2.y) < board.size ? true : false;
		return (c1.x/s)*s == (c2.x/s)*s && (c1.y/s)*s == (c2.y/s)*s;
	}
	
	private boolean deleteMatchingValues(NakedCandidates comparator, NakedCandidates operator) {
		
		boolean del = false;
		for (int i=0; i < comparator.values.size(); i++) {
			int j=0;
			while (j < operator.values.size()) {
				
				if (comparator.values.get(i).equals(operator.values.get(j))) {
					System.out.println("Removing value " + operator.values.get(j) + " at coordinate (" + operator.x + ", " + operator.y +")");
					operator.values.remove(j);
					del = true;
				}
				else {j++;}
			}
		}
		
		return del;
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
	
	public void groupCandidates() {
		
		/* Group Candidates by Row */
		System.out.println("=====Grouping By Row=====");
		for (int i=0; i < board.size*board.size; i++) {
			for (int j=0; j < nakeds.size(); j++) {
				if (nakeds.get(j).x == i) {
					displayValues(new String("Evaluating Coordinate at ("+nakeds.get(j).x+", "+nakeds.get(j).y+")"), nakeds.get(j).values);
				}
			}
		}
		
		/* Group Candidates by Column */
		System.out.println("=====Grouping By Column=====");
		for (int i=0; i < board.size*board.size; i++) {
			for (int j=0; j < nakeds.size(); j++) {
				if (nakeds.get(j).y == i) {
					displayValues(new String("Evaluating Coordinate at ("+nakeds.get(j).x+", "+nakeds.get(j).y+")"), nakeds.get(j).values);
				}
			}
		}
		// FIGURE OUT HOW TO GROUP CANDIDATES BY SECTION
		/* Group Candidates by Section * *
		System.out.println("====Grouping By Section=====");
		int count=0;
		while (count < board.size*board.size) {
			for (int x=0; x < board.size*board.size; x++) {
				for (int y=0; y < board.size*board.size; y+=(board.size)) {
					for (int j=0; j < nakeds.size(); j++) {
						if (nakeds.get(j).x == x && nakeds.get(j).y == y) {
							displayValues(new String("Evaluating Coordinate at ("+nakeds.get(j).x+", "+nakeds.get(j).y+")"), nakeds.get(j).values);
						}
					}
				}
			}
		}
		/* */
	}
/*
	private NakedCandidates getNaked(int x, int y) {
		
		NakedCandidates candidates = null;
		
		if (nakeds == null) {
			System.out.println("No Candidates Stored!");
			return candidate;
		}
		
		for (int i=0; i < nakeds.size() && candidate == null; i++) {
			NakedCandidates c = nakeds.get(i);
			if (c.x == x && c.y == y) {
				candidates = c;
			}
		}
		
		return candidates;
	}
*/
}
