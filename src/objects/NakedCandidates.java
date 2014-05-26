package objects;

import java.util.LinkedList;

public class NakedCandidates {

	// The Actual NakedCandidates
	public LinkedList<String> values;
	
	// The coordinates of the NakedCandidates on the SudokuBoard
	public int x, y;
	
	// The unique id given to a NakedCandidates object
	public int id;
	
	// Constructor creates a NakedCandidate object with a given size
	// Size is derived from the dimensions of the Sudoku Board
	public NakedCandidates(int size) {
		values = new LinkedList<String>();
	}
	
	// Returns true if object holds no Candidates
	public boolean moreValues() {
		return (values.size() > 0);
	}
	
	// Removes a candidate from the class object
	public void remove(int x) {
		values.remove(x);
	}
}
