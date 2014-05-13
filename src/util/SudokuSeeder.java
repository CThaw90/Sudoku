package util;

import objects.SudokuBoard;

public class SudokuSeeder {

	public static void Seed(SudokuBoard board) {
		
		switch (board.size) {
		case 2:
			Seedby2(board);
			break;
		case 3:
			Seedby3(board);
			break;
		case 4:
			Seedby4(board);
			break;
		case 5:
			Seedby5(board);
			break;
		default:
			System.out.println("Improper program path");
			System.exit(0);
			break;
		}
	}
	
	private static void Seedby2(SudokuBoard board) {
		
	}
	
	private static void Seedby3(SudokuBoard board) {
		
		board.setValue(0, 2, String.valueOf(7));
		board.setValue(0, 3, String.valueOf(9));
		board.setValue(0, 5, String.valueOf(6));
		board.setValue(0, 6, String.valueOf(3));
		board.setValue(1, 0, String.valueOf(3));
		board.setValue(1, 8, String.valueOf(4));
		board.setValue(2, 1, String.valueOf(9));
		board.setValue(2, 3, String.valueOf(3));
		board.setValue(2, 5, String.valueOf(4));
		board.setValue(2, 7, String.valueOf(2));
		board.setValue(3, 0, String.valueOf(9));
		board.setValue(3, 2, String.valueOf(5));
		board.setValue(3, 6, String.valueOf(8));
		board.setValue(3, 8, String.valueOf(2));
		board.setValue(4, 0, String.valueOf(6));
		board.setValue(4, 1, String.valueOf(2));
		board.setValue(4, 7, String.valueOf(5));
		board.setValue(4, 8, String.valueOf(7));
		board.setValue(5, 0, String.valueOf(8));
		board.setValue(5, 2, String.valueOf(1));
		board.setValue(5, 6, String.valueOf(4));
		board.setValue(5, 8, String.valueOf(3));
		board.setValue(6, 1, String.valueOf(6));
		board.setValue(6, 3, String.valueOf(5));
		board.setValue(6, 5, String.valueOf(3));
		board.setValue(6, 7, String.valueOf(4));
		board.setValue(7, 0, String.valueOf(4));
		board.setValue(7, 8, String.valueOf(5));
		board.setValue(8, 2, String.valueOf(2));
		board.setValue(8, 3, String.valueOf(6));
		board.setValue(8, 5, String.valueOf(1));
		board.setValue(8, 6, String.valueOf(7));
	}
	
	private static void Seedby4(SudokuBoard board) {
		
	}
	
	private static void Seedby5(SudokuBoard board) {
		
	}	
}
