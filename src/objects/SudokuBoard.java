package objects;

import util.SudokuSeeder;

public class SudokuBoard {
	
	public static BoardSize SMALL = new BoardSize(2);
	public static BoardSize REGULAR = new BoardSize(3);
	public static BoardSize LARGE = new BoardSize(4);
	public static BoardSize EXTRA_LARGE = new BoardSize(5);
	
	// Corner Head +---------------
	static String C_HEAD = new String("+-");
	
	// Corner Tail ---------------+
	static String C_TAIL = new String("+");
	
	// Border Head |-----
	static String B_HEAD = new String("|");
	
	// Border Tail -----|
	static String B_TAIL = new String("-|");
	
	String[][] sudokuGrid;
	int[][] boardGrid;
	public int size; 
	
	// Constructor creates Sudoku board with options of 4 static sizes
	public SudokuBoard(BoardSize size) {
		this.size = size.dimen;
		boardGrid = new int[this.size*this.size][this.size*this.size];
		sudokuGrid = new String[this.size*this.size][this.size*this.size];

		initializeGrid();
	}
	
	// Constructor creates normal 9x9 Sudoku Board
	public SudokuBoard() {
		sudokuGrid = new String[9][9];
		boardGrid = new int[9][9];
		size = 3;
		
		initializeGrid();
		
		SudokuSeeder.Seed(this);
	}
	
	// Places a value at the corresponding board coordinate
	public void setValue(int x, int y, int value) {
		boardGrid[x][y] = value;
	}
	
	public void setValue(int x, int y, String value) {
		sudokuGrid[x][y] = value;
	}
	
	// Returns the value at the corresponding board coordinate
	public int getValue(int x, int y) {
		return boardGrid[x][y];
	}
	
	public String getStringValue(int x, int y) {
		return sudokuGrid[x][y];
	}
	/**
	 * @description Method determines whether a new entry exists in current row
	 * @param x row of the Sudoku board to be checked
	 * @param entry value of the entry to be entered
	 * @return duplicate true if the current value already exists */
	public boolean duplicateEntryRow(int x, int entry) {
		
		boolean duplicate = false;
		for (int i=0; i < boardGrid.length && !duplicate; i++) {
			duplicate = (boardGrid[x][i] == entry ? true : false);
		}
		return duplicate;
	}
	
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
	public boolean duplicateEntryColumn(int y, int entry) {
		
		boolean duplicate = false;
		for (int i=0; i < boardGrid.length && !duplicate; i++) {
			duplicate = (boardGrid[i][y] == entry ? true : false);
		}
		return duplicate;
	}
	
	public boolean duplicateEntryColumn(int y, String entry) {
		
		boolean duplicate = false;
		for (int i=0; i < sudokuGrid.length && !duplicate; i++) {
			duplicate = (sudokuGrid[i][y].equals(entry) ? true : false);
		}
		return duplicate;
	}
	
	public boolean duplicateEntrySection(int x, int y, int entry) {
		
		boolean duplicate = false;
		
		int sx = (x/size)*size, sy = (y/size)*size;
		for (int i=sx; i < sx+size; i++) {
			for (int j=sy; j < sy+size; j++) {
				duplicate = (boardGrid[i][j] == entry ? true : false);
			}
		}
		return duplicate;
	}
	
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
	
	public boolean asertUnique(String t) {
		
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
	
	public boolean assertUnique(int t) {
		
		boolean unique = true, rCs;
		int length = size*size*2;
		int x=0, y=0;
		int[] array;
		rCs = true;
		
		for (int i=0; i < length && unique; i++) {
			
			array = new int[boardGrid.length];
			
			while (x < array.length && y < array.length && unique) {
				int index = boardGrid[x][y] - 1;
				if (index >= 0 && array[index] != 0) {
					System.out.print(boardGrid[x][y] + " == " + array[index]);
					System.out.println(" at x="+x+", y="+y);
					unique = false;
				} else if (index >= 0) {
					array[index] = boardGrid[x][y];
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
	
	private void initializeGrid() {
		
		for (int x=0; x < sudokuGrid.length; x++) {
			for (int y=0; y < sudokuGrid.length; y++) {
				sudokuGrid[x][y] = new String("*");
			}
		}
	}
	
	public void displayGrid() {
		displayEndBorder();
		
		for (int x=0; x < sudokuGrid.length; x++) {
			
			if (x%size == 0 && x != 0) {displayMiddleBorder();}
			for (int y=0; y < boardGrid[x].length; y++) {
				System.out.print((y==0 || y%this.size==0 ? "| " : ""));
				System.out.print(sudokuGrid[x][y] + " ");
				System.out.print((y==sudokuGrid[x].length-1 ? "|\n" : ""));
			}
		}
		
		displayEndBorder();
	}
	
	/**
	 * Method responsible for drawing the status of the Sudoku Board */
	public void displayBoard() {
		
		displayEndBorder();
		
		for (int x=0; x < boardGrid.length; x++) {
			
			if (x%size == 0 && x != 0) { displayMiddleBorder(); }
			
			for (int y=0; y < boardGrid[x].length; y++) {
				System.out.print((y==0 || y%this.size==0 ? "| " : ""));
				System.out.print(boardGrid[x][y] + " ");
				System.out.print((y==boardGrid[x].length-1 ? "|\n" : ""));
			}
		}
		
		displayEndBorder();
	}
	
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
	 * Method draws the top and bottom border of the Sudoku board */
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
	
	public int[][] currentBoardState() {
		return boardGrid;
	}
	
	public String[][] currentGridState() {
		return sudokuGrid;
	}
	
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