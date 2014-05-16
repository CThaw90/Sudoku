package objects;

import util.SudokuSeeder;

public class SudokuBoard {
	
	// Creates a 4x4 Sudoku Grid
	public static BoardSize SMALL = new BoardSize(2);
	
	// Creates a 9x9 Sudoku Grid
	public static BoardSize REGULAR = new BoardSize(3);
	
	// Creates a 16x16 Sudoku Grid
	public static BoardSize LARGE = new BoardSize(4);
	
	// Creates a 25x25 Sudoku Grid
	public static BoardSize EXTRA_LARGE = new BoardSize(5);
	
	// Corner Head +---------------
	static String C_HEAD = new String("+-");
	
	// Corner Tail ---------------+
	static String C_TAIL = new String("+");
	
	// Border Head |-----
	static String B_HEAD = new String("|");
	
	// Border Tail -----|
	static String B_TAIL = new String("-|");
	
	// The actual Sudoku Grid Representation via 2D array
	String[][] sudokuGrid;
	
	// Perimeter length of each box in a Sudoku Grid
	public int size; 
	
	// Constructor creates Sudoku board with options of 4 static sizes
	public SudokuBoard(BoardSize size) {
		this.size = size.dimen;
		sudokuGrid = new String[this.size*this.size][this.size*this.size];

		// class SudokuBoard method
		initializeGrid();
	}
	
	// Constructor creates normal 9x9 Sudoku Board
	public SudokuBoard() {
		sudokuGrid = new String[9][9];
		size = 3;
		
		// class SudokuBoard method
		initializeGrid();
		
		SudokuSeeder.Seed(this);
	}
	
	/**
	 * @description sets a value at the given coordinate
	 * @param x coordinate
	 * @param y coordinate
	 * @param value  
	 */
	public void setValue(int x, int y, String value) {
		sudokuGrid[x][y] = value;
	}
	
	/**
	 * @description gets the value at a given coordinate
	 * @param x coordinate
	 * @param y coordinate
	 * @return
	 */
	public String getStringValue(int x, int y) {
		return sudokuGrid[x][y];
	}
	/**
	 * @description Method determines whether a new entry exists in current row
	 * @param x row of the Sudoku board to be checked
	 * @param entry value of the entry to be entered
	 * @return duplicate true if the current value already exists */
	
	public boolean duplicateEntryRow(int x, String entry) {
		
		boolean duplicate = false;
		for (int i=0; i < sudokuGrid.length && !duplicate; i++) {
			duplicate = (sudokuGrid[x][i].equals(entry) ? true : false);
		}
		return duplicate;
	}
	
	/**
	 * @description Method determines whether a new entry exists in current column
	 * @param y column of the Sudoku board to be checked
	 * @param entry value of the entry to be entered
	 * @return duplicate returns true if the current value already exists */

	public boolean duplicateEntryColumn(int y, String entry) {
		
		boolean duplicate = false;
		for (int i=0; i < sudokuGrid.length && !duplicate; i++) {
			duplicate = (sudokuGrid[i][y].equals(entry) ? true : false);
		}
		return duplicate;
	}
	/**
	 * @description Method determines whether a new entry exists in the current section
	 * @param x, y combine to determine the section to be checked
	 * @param entry value of the entry to be entered
	 * @return returns true if the current value already exists */
	public boolean duplicateEntrySection(int x, int y, String entry) {
		
		boolean duplicate = false;
		int sx = (x/size)*size, sy = (y/size)*size;
		for (int i=sx; i < sx+size && !duplicate; i++) {
			for (int j=sy; j < sy+size && !duplicate; j++) {
				duplicate = (sudokuGrid[i][j].equals(entry) ? true : false);
			}
		}
		
		return duplicate;
	}
	
	/**
	 * @description asserts that there are no duplicate entries 
	 * in a column, row or section of the SudokuBoard object
	 * @return true if all values are in the same row, column and section are unique   */
	public boolean assertUnique() {
		
		boolean unique = true, rCs;
		int length = size*size*2;
		String[] array;
		int x=0, y=0;
		rCs = true;
		
		for (int i=0; i < length && unique; i++) {
			array = new String[sudokuGrid.length];
			
			while (x < array.length && y < array.length && unique) {
				
				int index = Integer.parseInt(sudokuGrid[x][y]) - 1;
				if (index >= 0 && !array[index].equals(new String("*"))) {
					System.out.print(sudokuGrid[x][y] + " == " + array[index]);
					System.out.println(" at x="+x+", y="+y);
					unique = false;
				} else if (index >= 0) {
					array[index] = sudokuGrid[x][y];
				}

				y = (rCs ? y+1 : y);
				x = (rCs ? x : x+1);
			}

			x = (rCs ? x+1 : 0);
			y = (rCs ? 0 : y+1); 
			
			rCs = (i == (length/2) - 1) ? !rCs : rCs;
			x = (i == (length/2) - 1) ? 0 : x;
			y = (i == (length/2) - 1) ? 0 : y;
		}
		
		return unique;
	}
	
	/**
	 * @description fills a SudokuBoard with '*' values which represents an empty field */
	private void initializeGrid() {
		
		for (int x=0; x < sudokuGrid.length; x++) {
			for (int y=0; y < sudokuGrid.length; y++) {
				sudokuGrid[x][y] = new String("*");
			}
		}
	}
	
	/**
	 * Method responsible for drawing the status of the Sudoku Board */
	public void displayGrid() {
		displayEndBorder();
		
		for (int x=0; x < sudokuGrid.length; x++) {
			
			if (x%size == 0 && x != 0) {displayMiddleBorder();}
			for (int y=0; y < sudokuGrid[x].length; y++) {
				System.out.print((y==0 || y%this.size==0 ? "| " : ""));
				System.out.print(sudokuGrid[x][y] + " ");
				System.out.print((y==sudokuGrid[x].length-1 ? "|\n" : ""));
			}
		}
		
		displayEndBorder();
	}
	
	/**
	 * Method draws the middle bars of the SudokuBoard
	   |-----------+-----------+------------| */
	private void displayMiddleBorder() {
		
		System.out.print(B_HEAD);
		boolean dec = false;
		
		for (int i=0; i < this.size; i++) {
			
			int size = this.size;
			while (size > 0) {
				System.out.print("-");
				size -= (dec ? 1 : 0);
				dec=(!dec);
			}
			
			System.out.print((i < this.size-1 ? "-+" : ""));
		}
		
		System.out.println(B_TAIL);
	}
	
	/**
	 * Method draws the top and bottom border of the Sudoku board 
	 * +-------------------------+ */
	private void displayEndBorder() {
		
		System.out.print(C_HEAD);
		boolean dec = false;
		
		for (int i=0; i < this.size; i++) {
			
			int size = this.size;
			while (size > 0) {
				
				System.out.print("-");
				size -= (dec ? 1 : 0);
				dec=(!dec);
			}
			
			System.out.print((i < this.size-1 ? "--" : ""));
		}
		
		System.out.println(C_TAIL);
	}
	
	/**
	 * @description method determines if the current SudokuBoard has been solved
	 * @return true if no values
	 */
	public boolean isSolved() {
		
		boolean solved = true;
		for (int x=0; x < size*size && solved; x++) {
			for (int y=0; y < size*size && solved; y++) {
				solved = (!sudokuGrid[x][y].equals(new String("*")));
			}
		}

		return solved;
	}
	
	/**
	 * @author cthaw
	 * @description Inner class BoardSize assists to determine the size of
	 * of the Sudoku Board to be utilized */
	public static class BoardSize {
	
		int dimen;
		public BoardSize(int size) {
			dimen = size;
		}
	}
}