package objects;

public class SudokuBoard {
	
	public static BoardSize SMALL = new BoardSize(2);
	public static BoardSize REGULAR = new BoardSize(3);
	public static BoardSize LARGE = new BoardSize(4);
	public static BoardSize EXTRA_LARGE = new BoardSize(5);
	
	// Border Head +---------------
	static String B_HEAD = new String("+-");
	
	// Border Tail ---------------+
	static String B_TAIL = new String("+");
	int[][] boardGrid;
	int size; 
	
	// Constructor creates Sudoku board with options of 4 static sizes
	public SudokuBoard(BoardSize size) {
		this.size = size.boardSize;
		boardGrid = new int[this.size*this.size][this.size*this.size];
	}
	
	// Constructor creates normal 9x9 Sudoku Board
	public SudokuBoard() {
		boardGrid = new int[9][9];
		size = 3;
	}
	
	public void displayBoard() {
		
		for (int x=0; x < boardGrid.length; x++) {
		//	System.out.print("| ");
			for (int y=0; y < boardGrid[x].length; y++) {
				System.out.print((y==0 ? "|" : ""));
				System.out.print(boardGrid[x][y] + " ");
				System.out.print((y==boardGrid[x].length-1 ? "|\n" : ""));
			//	System.out.print((y%size == 0 ? " | " : ""));
			}
			
		//	System.out.println(" |");
		}
	}
	
	public void displayBorder() {
		
		System.out.print(B_HEAD);
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
		
		System.out.println(B_TAIL);
	}
	
	/**
	 * @author cthaw
	 * @description Inner class BoardSize assists to determine the size of
	 * of the Sudoku Board to be utilized
	 */
	public static class BoardSize {
	
		int boardSize;
		
		public BoardSize(int size) {
			boardSize = size;
		}
	}
}
