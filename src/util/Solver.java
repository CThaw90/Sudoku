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
	
	/**
	 * @description constructor loads a SudokuBoard to solve
	 * @param board */
	public Solver(SudokuBoard board) {
		solved = board.isSolved();
		this.board = board;
	}
	
	/**
	 * @description method loads a SudokuBoard
	 * @param board */
	public void loadSudokuPuzzle(SudokuBoard board) {
		solved = board.isSolved();
		this.board = board;
	}
	
	// Attempts to solves the loaded Sudoku Board
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
		
		// The Sudoku Solvers iterate with proportion to the board size
		for (int i=0; i < board.size; i++) {
			solved = (solved) ? solved : nakedSingleSolver();
			solved = (solved) ? solved : nakedPairSolver();
			solved = (solved) ? solved : bruteForceSolver(0);
			solved = (solved) ? solved : nakedTripleSolver(new LinkedList<NakedCandidates>(), new LinkedList<Integer>(), 0);
		}
		
		
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
	
	/**
	 * @description the nakedSingle solver looks for spaces that only 
	 * have one possible value to be placed
	 * @return true if the puzzle is solved after the method exits */
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
	
	/**
	 * @description the nakedPairSolver looks for two different coordinates
	 * with the same pair of possible values in the same row, column or section
	 * @return true if the puzzle is solved after the method exits */
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
	
	private boolean nakedTripleSolver(LinkedList<NakedCandidates> nakedTriples, LinkedList<Integer> values, int index) {
		System.out.println("______NAKED TRIPLE SOLVER_______");
		boolean solvable = false;
		
		for (/* index */; index < nakeds.size() && !solvable; index++) {
			NakedCandidates current = nakeds.get(index);
			System.out.println("Evaluating at Index " + index);
			if ((current.values.size() == 2 || current.values.size() == 3) && !passOver(values, index) && nakedTriples.size() == 0) {
				displayValues(new String("Candidates at ("+current.x+", "+current.y+")"), current.values);
				System.out.println("Match the initial set NakedTriple Profile");
				nakedTriples.add(current);
				System.out.println("Adding to nakedTriples. Current Capacity: " + nakedTriples.size());
				values.add(index);
				solvable = nakedTripleSolver(nakedTriples, values, index+1);
			}
			
			else if ((current.values.size() == 2 || current.values.size() == 3) && !passOver(values, index)) {
				NakedCandidates comparator = nakedTriples.get(0);
				int x = comparator.x, y = comparator.y;
				if (comparator.x == current.x || comparator.y == current.y || sameSection(nakedTriples.get(0), current)) {
					
					if (checkForNakedTriples(comparator, current)) {
						System.out.println("Matched the NakedTriple Profle set by candidates at ("+x+", "+y+")");
						displayValues(new String("Candidates at ("+current.x+", "+current.y+")"), current.values);
						displayValues(new String("Comparator Candidates at ("+x+", "+y+"):"), comparator.values);
						nakedTriples.add(current);
						System.out.println("Adding to nakedTriples. Current Capacity: " + nakedTriples.size());
						solvable = nakedTripleSolver(nakedTriples, values, index+1);
					}
					else {
						displayValues(new String("Candidates at ("+current.x+", "+current.y+")"), current.values);
						System.out.println("Does not match the set NakedTriple Profile");
						displayValues(new String("Comparator Candidates"), comparator.values);
					}
				}
				else {
					displayValues(new String("Candidates at ("+current.x+", "+current.y+")"), current.values);
					System.out.println("Does not match the set NakedTriple Profile at ("+comparator.x+", "+comparator.y+")");
					displayValues(new String("Comparator Candidates at ("+comparator.x+", "+comparator.y+")"), comparator.values);
				}
			}
			
			else if (nakedTriples.size() == 3) {
				boolean sameRow = nakedTriples.get(0).x == nakedTriples.get(1).x && 
							      nakedTriples.get(1).x == nakedTriples.get(2).x;
				
				boolean sameColumn = nakedTriples.get(0).y == nakedTriples.get(1).y &&
									 nakedTriples.get(1).y == nakedTriples.get(2).y;
				
				boolean sameSection = sameSection(nakedTriples.get(0), nakedTriples.get(1)) &&
									  sameSection(nakedTriples.get(1), nakedTriples.get(2));
				
				if (sameRow || sameColumn || sameSection) {
					System.out.println("THE NAKED TRIPLES");
					for (int j=0; j < nakedTriples.size(); j++) {
						NakedCandidates display = nakedTriples.get(j);
						displayValues(new String("Candidates at ("+display.x+", "+display.y+")"), display.values);
					}
					if (confirmNakedTriples(nakedTriples)) {
						System.out.println("NAKED TRIPLES ARE CONFIRMED!!!!!");
						NakedCandidates mockCandidate = new NakedCandidates(board.size*board.size);
						mockCandidate.x = nakedTriples.get(0).x;
						mockCandidate.y = nakedTriples.get(0).y;
						
						for (int i=0; i < nakedTriples.size(); i++) {
							for (int j=0; j < nakedTriples.get(i).values.size(); j++) {
								if (!passOver(mockCandidate.values, nakedTriples.get(i).values.get(j))) {
									mockCandidate.values.add(nakedTriples.get(i).values.get(j));
								}
							}
						}
					
						displayValues("MockCandidateValues", mockCandidate.values);
						removeAffectedCandidates(mockCandidate, sameRow, sameColumn, sameSection);
						return true;
					}
				}
			}
			
			else if (nakedTriples.size() > 0) {
				NakedCandidates comparator = nakedTriples.get(0);
				displayValues(new String("Candidates at ("+current.x+", "+current.y+")"), current.values);
				System.out.println("Does not match the NakedTriple Profile");
				displayValues(new String("Comparator Candidates at ("+comparator.x+", "+comparator.y+")"), comparator.values);
			}
		}
		
		if (nakedTriples.size() == 0) {
			System.out.println("==========Triple Candidates===========");
			for (int i=0; i < nakedTriples.size(); i++) {
				NakedCandidates triple = nakedTriples.get(i);
				int x = triple.x, y = triple.y;
				this.displayValues(new String("Naked Triple Values at ("+x+", "+y+")"), triple.values); 
			}
		}
		
		else {
			System.out.println("UNWINDING RECURSION");
			int i=nakedTriples.size() -1;
			System.out.println("Removing Naked Candidate at (" + nakedTriples.get(i).x +", " + nakedTriples.get(i).y+") ");
			nakedTriples.remove(nakedTriples.size()-1);
		}
		
		return nakedPairSolver();
	}
	
	private boolean checkForNakedTriples(NakedCandidates c1, NakedCandidates c2) {
		
		boolean compare3_2=false, compare3_3=false, compare2_2=false, compare2_3=false, matches=false;
		System.out.println("Comparing Candidates at coordinates ("+c1.x+", "+c1.y+") AND ("+c2.x+", "+c2.y+")");
		compare2_3 = (c1.values.size() == 2 && c2.values.size() == 3);
		compare3_2 = (c1.values.size() == 3 && c2.values.size() == 2);
		compare3_3 = (c1.values.size() == 3 && c2.values.size() == 3);
		compare2_2 = (c1.values.size() == 2 && c2.values.size() == 2);
		
		if (compare2_3) {
			matches = (c1.values.get(0).equals(c2.values.get(0)) && c1.values.get(1).equals(c2.values.get(1)))
					||(c1.values.get(0).equals(c2.values.get(1)) && c1.values.get(1).equals(c2.values.get(2))); 
			
			System.out.println("Comparing " + c1.values.get(0) + " <---> " + c2.values.get(0));
			System.out.println("Comparing " + c1.values.get(1) + " <---> " + c2.values.get(1));
			System.out.println("-------------------------- OR ------------------------------");
			System.out.println("Comparing " + c1.values.get(0) + " <---> " + c2.values.get(1));
			System.out.println("Comparing " + c1.values.get(1) + " <---> " + c2.values.get(2));
		}
		else if (compare3_2) {
			matches = (c1.values.get(0).equals(c2.values.get(0)) && c1.values.get(1).equals(c2.values.get(1)))
					||(c1.values.get(1).equals(c2.values.get(0)) && c1.values.get(2).equals(c2.values.get(1)));
			
			System.out.println("Comparing " + c1.values.get(0) + " <---> " + c2.values.get(0));
			System.out.println("Comparing " + c1.values.get(1) + " <---> " + c2.values.get(1));
			System.out.println("-------------------------- OR ------------------------------");
			System.out.println("Comparing " + c1.values.get(1) + " <---> " + c2.values.get(0));
			System.out.println("Comparing " + c1.values.get(2) + " <---> " + c2.values.get(1));
		}
		else if (compare3_3) {
			matches = (c1.values.get(0).equals(c2.values.get(0)) && 
					   c1.values.get(1).equals(c2.values.get(1)) &&
					   c1.values.get(2).equals(c2.values.get(2)) ); 
			
			System.out.println("Comparing " + c1.values.get(0) + " <---> " + c2.values.get(0));
			System.out.println("Comparing " + c1.values.get(1) + " <---> " + c2.values.get(1));
			System.out.println("Comparing " + c1.values.get(2) + " <---> " + c2.values.get(2));
			
		}
		else if (compare2_2) {
			matches = (c1.values.get(0).equals(c2.values.get(0)) || 
					   c1.values.get(1).equals(c2.values.get(1)) ||
					   c1.values.get(1).equals(c2.values.get(0)) );
			
			System.out.println("Comparing " + c1.values.get(0) + " <---> " + c2.values.get(0));
			System.out.println("-------------------------- OR ------------------------------");
			System.out.println("Comparing " + c1.values.get(1) + " <---> " + c2.values.get(1));
			System.out.println("-------------------------- OR ------------------------------");
			System.out.println("Comparing " + c1.values.get(1) + " <---> " + c2.values.get(0));
		}
		
		return matches;
	}
	
	/**
	 * @description ensures all potential NakedTriples match the given mandates to be valid
	 * @param nakedTriples the naked triples being compared
	 * @return true if the nakedTriples adheres to a NakedTriple rules *
	 * (123) (123) (123) - {3/3/3} (in terms of candidates per cell)
	 * (123) (123) (12) - {3/3/2} (or some combination thereof)
	 * (123) (12) (23) - {3/2/2/}
	 * (12) (23) (13) - {2/2/2} /* */
	private boolean confirmNakedTriples(LinkedList<NakedCandidates> nakedTriples) {
		boolean aNakedTriple=false;
		
		NakedCandidates c1 = nakedTriples.get(0);
		NakedCandidates c2 = nakedTriples.get(1);
		NakedCandidates c3 = nakedTriples.get(2);
		
		boolean compare3_3_3 = (c1.values.size() == 3 && c2.values.size() == 3 && c3.values.size() == 3);
		boolean compare3_3_2 = (c1.values.size() == 3 && c2.values.size() == 3 && c3.values.size() == 2);
		boolean compare3_2_3 = (c1.values.size() == 3 && c2.values.size() == 2 && c3.values.size() == 3);
		boolean compare2_3_3 = (c1.values.size() == 2 && c2.values.size() == 3 && c3.values.size() == 3);
		boolean compare3_2_2 = (c1.values.size() == 3 && c2.values.size() == 2 && c3.values.size() == 2);
		boolean compare2_3_2 = (c1.values.size() == 2 && c2.values.size() == 3 && c3.values.size() == 2);
		boolean compare2_2_3 = (c1.values.size() == 2 && c2.values.size() == 2 && c3.values.size() == 3);
		boolean compare2_2_2 = (c1.values.size() == 2 && c2.values.size() == 2 && c3.values.size() == 2);
		
		if (compare3_3_3) {
			/* Implementation of Naked Triple Match (123) (123) (123) - {3/3/3} (in terms of candidates per cell) */
			aNakedTriple =(c1.values.get(0).equals(c2.values.get(0)) && c2.values.get(0).equals(c3.values.get(0)) &&
						   c1.values.get(1).equals(c2.values.get(1)) && c2.values.get(1).equals(c3.values.get(1)) &&
						   c1.values.get(2).equals(c2.values.get(2)) && c2.values.get(2).equals(c3.values.get(2)) );
		}
		
		if (compare3_3_2) {
			/* Implementation of Naked Triple Match (123) (123) (12) OR (123) (123) (23) {3/3/2} */
			aNakedTriple =(c1.values.get(0).equals(c2.values.get(0)) && 
						   c1.values.get(1).equals(c2.values.get(1)) &&
						   c1.values.get(2).equals(c2.values.get(2)) 
						   
						   &&
						   
						  (c1.values.get(0).equals(c3.values.get(0)) &&
						   c1.values.get(1).equals(c3.values.get(1))
						   
						   ||
						   
						   c1.values.get(1).equals(c3.values.get(0)) &&
						   c1.values.get(2).equals(c3.values.get(2)) ));
		}
		
		if (compare3_2_3) {
			/* Implementation of Naked Triple Match (123) (12) (123) OR (123) (23) (123) {3/2/3} */
			aNakedTriple = (c1.values.get(0).equals(c3.values.get(0)) && 
					        c1.values.get(1).equals(c3.values.get(1)) &&
					        c1.values.get(2).equals(c3.values.get(2))
					        
					        &&
					        
					       (c1.values.get(0).equals(c2.values.get(0)) &&
					        c1.values.get(1).equals(c2.values.get(1))
					        
					        ||
					        
					    	c1.values.get(1).equals(c2.values.get(0)) &&
					    	c1.values.get(2).equals(c2.values.get(1)) ));					
		}
		
		if (compare2_3_3) {
			/* Implementation of Naked Triple Match (12) (123) (123) OR (23) (123) (123) {2/3/3} */
			aNakedTriple = (c2.values.get(0).equals(c3.values.get(0)) &&
						    c2.values.get(1).equals(c3.values.get(1)) &&
						    c2.values.get(2).equals(c3.values.get(2)) 
						    
							&&
							
						   (c1.values.get(0).equals(c2.values.get(0)) &&
							c1.values.get(1).equals(c2.values.get(1)) 
							
							|| 
							
							c1.values.get(0).equals(c2.values.get(1)) &&
							c1.values.get(0).equals(c2.values.get(2)) ));
		}
		
		if (compare3_2_2) {
			/* Implementation of Naked Triple Match   (123) (12) (23) OR (123) (23) (12) {3/2/2} */
			aNakedTriple = (c1.values.get(0).equals(c2.values.get(0)) &&
							c1.values.get(1).equals(c2.values.get(1)) &&
							c1.values.get(1).equals(c3.values.get(0)) &&
							c1.values.get(2).equals(c3.values.get(1)) 
							
							||
							
						   (c1.values.get(0).equals(c3.values.get(0)) &&
							c1.values.get(1).equals(c3.values.get(1)) &&
							c1.values.get(1).equals(c2.values.get(0)) &&
							c1.values.get(2).equals(c2.values.get(1)) ));
		}
		
		if (compare2_3_2) {
			/* Implementation of Naked Triple Match   (12) (123) (23) OR (23) (123) (12) {2/3/2} */
			aNakedTriple = (c1.values.get(0).equals(c2.values.get(0)) &&
						    c1.values.get(1).equals(c2.values.get(1)) &&
						    c1.values.get(1).equals(c3.values.get(0)) &&
						    c2.values.get(2).equals(c3.values.get(1)) 
					
						    ||
					
						   (c1.values.get(0).equals(c2.values.get(1)) &&
						    c1.values.get(1).equals(c2.values.get(2)) &&
						    c2.values.get(0).equals(c3.values.get(0)) &&
						    c2.values.get(1).equals(c3.values.get(1)) ));
		}
		
		if (compare2_2_3) {
			/* Implementation of Naked Triple Match   (12) (23) (123) OR (23) (12) (123) {2/2/3} */
			aNakedTriple = (c1.values.get(0).equals(c3.values.get(0)) &&
						    c1.values.get(1).equals(c2.values.get(0)) &&
						    c1.values.get(1).equals(c3.values.get(0)) &&
						    c1.values.get(2).equals(c3.values.get(1)) 
					
						    ||
					
						   (c1.values.get(0).equals(c3.values.get(0)) &&
					        c1.values.get(1).equals(c3.values.get(1)) &&
					        c1.values.get(1).equals(c2.values.get(0)) &&
					        c1.values.get(2).equals(c2.values.get(1)) ));
		}
		
		// (12) (23) (13) - {2/2/2}
		if (compare2_2_2) {
			/* Implementation of Naked Triple Match (12) (23) (13) OR (23) (12) (13) ( {2/2/2} */
			aNakedTriple = (c1.values.get(0).equals(c3.values.get(0)) &&
							c1.values.get(1).equals(c2.values.get(0)) &&
							c2.values.get(1).equals(c3.values.get(1)) 
							
							||
							
						   (c1.values.get(0).equals(c2.values.get(1)) &&
							c1.values.get(1).equals(c3.values.get(1)) &&
							c2.values.get(0).equals(c3.values.get(0)) ));
		}
	
		return aNakedTriple;
	}
	
	/**
	 * @description tries all combinations of possible values until the puzzle
	 * is solved or is determined unsolvable
	 * @param index keeps track of the NakedCandidate index in the LinkedList
	 * @return true if the puzzle is solvable else returns false */
	private boolean bruteForceSolver(int index) {
		
		boolean solvable = false;
		NakedCandidates candidate = nakeds.get(index);
		for (int i=0; i < candidate.values.size() && !solvable; i++) {
			
			if (!board.duplicateEntryColumn(candidate.y, candidate.values.get(i)) 
					&& !board.duplicateEntryRow(candidate.x, candidate.values.get(i))
					&& !board.duplicateEntrySection(candidate.x, candidate.y, candidate.values.get(i))) {
				
				System.out.println("Setting down value " + candidate.values.get(i) + " at coordinate ("+candidate.x+", "+candidate.y+")");
				board.setValue(candidate.x, candidate.y, candidate.values.get(i));
				solvable = (index < nakeds.size()-1 ? bruteForceSolver(index+1) : board.isSolved());
			}
			
			else {
			 	int x = candidate.x;
			 	int y = candidate.y;
			 	String v = candidate.values.get(i);
			 	System.out.println("Duplicate Value in " + v + " at ("+x+", "+y+").");
			}
		}
		
		if (!solvable) { board.setValue(candidate.x, candidate.y, new String("*")); }
		return solvable;
	}
	
	/**
	 * @description Removes all candidates that have been affected 
	 * by an updated value to the SudokuBoard
	 * @param  */
	private void removeAffectedCandidates(NakedCandidates candidate) {
		
	//	System.out.println("Removing candidate at coordinate ("+candidate.x+", "+candidate.y+") with value " + candidate.values.get(0));
		
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
	
	private void removeAffectedCandidates(NakedCandidates candidate, boolean sameRow, boolean sameColumn, boolean sameSection) {
		
		int index=0;
		while (index < nakeds.size()) {
			NakedCandidates operator = nakeds.get(index);
			
			if (sameRow && candidate.x == operator.x) {
				System.out.println("Potential match values in the same row at ("+operator.x+", "+operator.y+")");
				displayValues("Candidate Values", candidate.values);
				displayValues("Operator Values", operator.values);
				deleteMatchingValues(candidate, operator);
				displayValues("Result Values", operator.values);
			}
			
			else if (sameColumn && candidate.y == operator.y) {
				System.out.println("Potential match values in the same column at ("+operator.x+", "+operator.y+")");
				displayValues("Candidate Values", candidate.values);
				displayValues("Operator Values", operator.values);
				deleteMatchingValues(candidate, operator);
				displayValues("Result Values", operator.values);
			}
			
			if (sameSection && sameSection(candidate, operator)) {
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
	
	/**
	 * @description passes over all coordinates that have already
	 * been evaluated
	 * @param values coordinates already evaluated 
	 * @param value coordinate to be evaluated
	 * @return true if the coordinate to be evaluated exists in the
	 * coordinates that have already been evaluated */
	private boolean passOver(LinkedList<Integer> values, int value) {
		
		boolean passOver = false;
		for (int index=0; index < values.size() && !passOver; index++) {
			passOver = (values.get(index) == value ? true : false);
		}
		return passOver;
	}
	
	private boolean passOver(LinkedList<String> values, String value) {
		
		boolean passOver = false;
		for (int index=0; index < values.size() && !passOver; index++) {
			passOver = (values.get(index).equals(value));
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
	
	/**
	 * @description checks for two pairs of possible values in
	 * the same row, column or section
	 * @param x coordinate
	 * @param y coordinate
	 * @param i index of the NakedCandidate in the LinkedList
	 * @return index of the matching NakedCandidate */
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
	
	// Determines whether two Naked Candidates are in the same section or box
	private boolean sameSection(NakedCandidates c1, NakedCandidates c2) {
		int s = board.size;
		return (c1.x/s)*s == (c2.x/s)*s && (c1.y/s)*s == (c2.y/s)*s;
	}
	/**
	 * @description deletes all matching values in a NakedCandidate object
	 * @param comparator candidate object values are compared to
	 * @param operator candidate object values are deleted from
	 * @return true if a deletion operation occured */
	private boolean deleteMatchingValues(NakedCandidates comparator, NakedCandidates operator) {
		
		boolean del = false;
		for (int i=0; i < comparator.values.size(); i++) {
			int j=0;
			while (j < operator.values.size()) {
				
				if (comparator.values.get(i).equals(operator.values.get(j))) {
					operator.values.remove(j);
					del = true;
				}
				else {j++;}
			}
		}
		
		return del;
	}
	
	/**
	 * Extracts all the possible values available for an empty Sudoku cell 
	 * and stores them in a NakedCandidate object dedicated to a coordinate*/
	private void extractCandidates() {
	//	if (nakeds != null)
	//		System.out.println("Extracting Candidates from " + nakeds.size() + " coords");
		
		nakeds = new LinkedList<NakedCandidates>();
		int length = board.size*board.size;
		for (int x=0; x < length; x++) {
			for (int y=0; y < length; y++) {

				if (board.getStringValue(x, y).equals(new String("*"))) {
					
	//				System.out.println("Entered Eval at coordinate x="+x+" y="+y);
					candidate = new NakedCandidates(board.size*board.size);
					candidate.x = x;
					candidate.y = y;
					
					for (int i=1; i <= length; i++) {

						if (!board.duplicateEntryColumn(y, String.valueOf(i)) 
								&& !board.duplicateEntryRow(x, String.valueOf(i))
								&& !board.duplicateEntrySection(x, y, String.valueOf(i))) {
							
							candidate.values.add(String.valueOf(i));
	//						System.out.print((i)+ " ");
						}
					}
					
					nakeds.add(candidate);
	//				System.out.println();
				}
			}
		}
	}
	
	public void groupCandidates() {
		
		/* Group Candidates by Row */
		System.out.println("=====Grouping By Row=====");
		for (int i=0; i < board.size*board.size; i++) {
			System.out.println("=========ROW " + (i+1)+ "===========");
			for (int j=0; j < nakeds.size(); j++) {
				if (nakeds.get(j).x == i) {
					displayValues(new String("Evaluating Coordinate at ("+nakeds.get(j).x+", "+nakeds.get(j).y+")"), nakeds.get(j).values);
				}
			}
		}
		
		/* Group Candidates by Column */
		System.out.println("=====Grouping By Column=====");
		for (int i=0; i < board.size*board.size; i++) {
			System.out.println("=========COLUMN " + (i+1)+ "=========");
			for (int j=0; j < nakeds.size(); j++) {
				if (nakeds.get(j).y == i) {
					displayValues(new String("Evaluating Coordinate at ("+nakeds.get(j).x+", "+nakeds.get(j).y+")"), nakeds.get(j).values);
				}
			}
		}
		// FIGURE OUT HOW TO GROUP CANDIDATES BY SECTION
		/* Group Candidates by Section * */
		System.out.println("====Grouping By Section=====");
		int count=0,startx=0,starty=0;
		while (count < board.size*board.size) {
			System.out.println("=======SECTION " + (count+1)+"=========");
			for (int x=startx; x < startx+board.size; x++) {
				for (int y=starty; y < starty+board.size; y++) {
					for (int j=0; j < nakeds.size(); j++) {
						if (nakeds.get(j).x == x && nakeds.get(j).y == y) {
							displayValues(new String("Evaluating Coordinate at ("+nakeds.get(j).x+", "+nakeds.get(j).y+")"), nakeds.get(j).values);
						}
						
					}
				}
			}
			
			count++;
			starty= starty<(board.size*board.size)-board.size ? starty+board.size : 0;
			startx=(count/board.size)*board.size;
		}
	}
}
