package objects;

public class SudokuBoard {
	
	int[][] boardGrid;
	int size; 
	
	public SudokuBoard(int size) {
		boardGrid = new int[size*size][size*size];
		this.size = size;
	}
	
	public SudokuBoard() {
		boardGrid = new int[9][9];
		size = 9;
	}
	
	public void displayBoard() {
		
		for (int x=0; x < boardGrid.length; x++) {
			for (int y=0; y < boardGrid[x].length; y++) {
				System.out.print(boardGrid[x][y] + " ");
			}
			
			System.out.println();
		}
	}
	
	

}
