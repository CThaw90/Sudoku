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
	
	private FileIO inFile = null;
	
	public Util(String[] args) {
		
		if (args.length > 0) parseArguments(args);
		
		filePath = (filePath != null ? filePath : null);
		format = (format != null ? format : Arguments.FORMAT_CSV);
		sudokuSize = (sudokuSize != null ? sudokuSize : Arguments.SIZE_9x9);
		folderPath = (folderPath != null ? folderPath : new String("SudokuBoards"));
		
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
		
		inFile = new FileIO((filePath != null ? filePath : folderPath));
		String[] rawData = inFile.loadAllBoardData();
		SudokuBoard[] boards = null;
		
		
		if (rawData != null) {
		//	for (int i=0; i < rawData.length; i++)
		//		System.out.println("Board Data " + i + ": " + rawData[i]);
			
			boards = new SudokuBoard[rawData.length];
			
			for (int i=0; i < boards.length; i++) {
				boards[i] = constructSudokuBoard(rawData[i]);
			}
		}
		return boards;
	}
	
	private SudokuBoard constructSudokuBoard(String boardData) {
		
		SudokuBoard newBoard = null;
		
		String[] parsedData = boardData.split(":");
		if (parsedData.length == 2 && parsedData[0].matches("[\\d+][x][\\d+]")) {
		//	System.out.println("Size Parameter: " + parsedData[0]);
			newBoard = getBoardSize(parsedData[0].trim().toLowerCase());
			int coordCnt = newBoard.size*newBoard.size*newBoard.size*newBoard.size;
			int coordRem = coordCnt;
			
			parsedData = parsedData[1].split("-");

			for (int i=0; i < parsedData.length; i++) {
				
				if (!parsedData[i].matches("\\s+") && !parsedData[i].isEmpty()) {
				//	System.out.println("ParsedData line " + (i+1)+ " " + parsedData[i]);
					String[] readData = parsedData[i].split(",");
					int x = (coordCnt-coordRem)/(newBoard.size*newBoard.size);
					int y = (coordRem)%(newBoard.size*newBoard.size);
					
					for (int j=0; j < readData.length; j++) {
						if (readData[j].trim().equals(new String("*")) || isDigit(readData[j].trim())) {
							if (coordRem == 0 && j < readData.length-1) {
								System.out.println("Error on line " + (i+1) + " too many declared coordinates");
								return null;
							}

							newBoard.setValue(x, y, readData[j].trim());
							y++;
							
							x += (y > 8) ? 1 : 0;
							y = (y <= 8) ? y : 0;
							coordRem--;

						}
						else {
							System.out.println("Format error on line " + (i+1) + " unexpected symbol '" + readData[j] + "'");
							return null;
						}
					}
				}
			}
		}
		else {
			// Fail Safe Parse Logic needs to be a bit cleaner
			String[] grabHeader = parsedData[0].substring(0, 6).split("[\\d+][x][\\d+]");
			System.out.println("Bad Format at " + grabHeader[(grabHeader.length-1)].substring(0,1)); 
			System.out.println("Loading default 9x9 seeded board");
			newBoard = new SudokuBoard();
			SudokuSeeder.Seed(newBoard);
		}
		
		return newBoard;
	}
	
	public SudokuBoard getBoardSize(String sizeParameter) {
		
		if (sizeParameter.equals(Arguments.SIZE_4x4)) {return new SudokuBoard(SudokuBoard.SMALL); } 
		
		if (sizeParameter.equals(Arguments.SIZE_9x9)) {return new SudokuBoard(SudokuBoard.REGULAR); }
			
		if (sizeParameter.equals(Arguments.SIZE_16x16)) {return new SudokuBoard(SudokuBoard.LARGE); }
			
		if (sizeParameter.equals(Arguments.SIZE_25x25)) {return new SudokuBoard(SudokuBoard.EXTRA_LARGE); }
		
		System.out.println(sizeParameter + " is an invalid option. Setting default size of 9x9");
		return new SudokuBoard(SudokuBoard.REGULAR);
	}
	
	private boolean isDigit(String s) {
		
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
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
