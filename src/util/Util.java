package util;

import objects.SudokuBoard;

public class Util {
	
	/**
	 * @param args the command line arguments
	 * @description Util constructor responsible for invoking the parser and
	 * parsing through the command line arguments if necessary.  */
	
	private String sudokuSize = null;
	private String folderPath = null;
	private String filePath = null;
	private String format = null;
	
	public Util(String[] args) {
		
		if (args.length > 0) parseArguments(args);
		
		filePath = (filePath != null ? filePath : "sudokuTest.sudoku");
		format = (format != null ? format : Arguments.FORMAT_CSV);
		sudokuSize = (sudokuSize != null ? sudokuSize : "9x9");
		folderPath = (folderPath != null ? folderPath : null);
		
		System.out.println("Sudoku Size: " + sudokuSize);
		System.out.println("FolderPath: " + folderPath);
		System.out.println("FilePath: " + filePath);
		System.out.println("Format: " + format);
		
	}
	
	private void parseArguments(String[] args) {
		int i=0;
		while (i < args.length) {
			if (args[i].equals(Arguments.FOLDER)) {
				folderPath = args[++i];
			}
			
			else if (args[i].equals(Arguments.FILE)) {
				filePath = args[++i];
			}
			
			else if (args[i].equals(Arguments.SIZE)) {
				i++;
				if (args[i].equals(Arguments.SIZE_4x4) || args[i].equals(Arguments.SIZE_9x9) 
						|| args[i].equals(Arguments.SIZE_16x16) || args[i].equals(Arguments.SIZE_25x25)) {
					sudokuSize = args[i];
				}
				else {
					System.out.println("Invalid Size Option. Using default 9x9 size");
					sudokuSize = Arguments.SIZE_9x9;
				}
			}
			
			else if (args[i].equals(Arguments.FORMAT)) {
				i++;
				if (args[i].equals(Arguments.FORMAT_CSV) || args[i].equals(Arguments.FORMAT_GUI)) {
					format = args[i];
				}
				else {
					System.out.println("Invalid Format Option. Using default CSV format!");
					format = Arguments.FORMAT_CSV;
				}
			}

			i++;
		}
	}
	
	public SudokuBoard[] createSudokuBoards() {
		
		SudokuBoard[] boards = null;
		
		return boards;
	}
	
	private static class Arguments {
		
		static String FOLDER = new String("--folder");
		static String FILE = (new String("--file"));
		
		static String SIZE = (new String("--size"));
		static String SIZE_4x4 = (new String("4x4"));
		static String SIZE_9x9 = (new String("9x9"));
		static String SIZE_16x16 = (new String("16x16"));
		static String SIZE_25x25 = (new String("25x25"));
		
		static String FORMAT = (new String("--format"));
		static String FORMAT_CSV = (new String("CSV"));
		static String FORMAT_GUI = (new String("GUI"));
	}
}
