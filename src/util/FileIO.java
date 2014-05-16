package util;

import java.io.*;

public class FileIO {

	private FileReader fileReader;
	private BufferedReader buffer;
	private File file;

	private static String hyphen = new String("-");
	private StringBuilder dataBuilder;
	private String pathLocation;
	
	// Default Constructor
	public FileIO() {
		pathLocation = null;
		fileReader = null;
		buffer = null;
		file = null;
	}
	
	// Constructor Loads the path to the file or folder
	// Checks if the given pathLocation exists. If not loads the default SudokuBoards/ Folder
	public FileIO(String pathLocation) {
		
		file = new File(pathLocation);
		if (!file.exists()) {
			System.out.println("File or Directory not found. Loading default Folder SudokuBoards/");
			loadPathLocation("SudokuBoards/");
		}
		else {
			this.pathLocation = pathLocation;
		}
	}
	
	// Method Loads the path to the file or folder
	// Checks if the given pathLocation exists. If not loads the default SudokuBoards/ Folder
	public void loadPathLocation(String pathLocation) {
		
		file = new File(pathLocation);
		if (!file.exists()) {
			System.out.println("File or Directory not found. Path not Loaded");
			try {
				System.out.println("Put boards in " + new java.io.File(".").getCanonicalPath() + "/SudokuBoards/");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Loaded Path Location " + pathLocation);
			this.pathLocation = pathLocation;
		}
	}
	
	/**
	 * @description method loads all the files that match the given pathLocation
	 * Each file is stored in a separate String index in the array
	 * @return the String array with all files loaded in memory */
	public String[] loadAllBoardData() {
		
		if (pathLocation == null) {
			System.out.println("No loaded Path Location");
			return new String[1];
		}
		
		// Ensures the filename matches the sudoku format //
		String[] loadedData = null;
		String[] parseFiles = pathLocation.split("\\.");
		
		if (parseFiles.length == 2 && parseFiles[1].equals(new String("sudoku"))) {
			loadedData = new String[1];
			parseFiles = new String[1];
			parseFiles[0] = pathLocation.split("/")[pathLocation.split("/").length-1];
			loadedData[0] = rawBoardData();
		}
		
		// Checks if the pathLocation is a Folder and 
		// loads all files that match the .sudoku format
		else {
			File[] files = file.listFiles();
			parseFiles = new String[files.length];
			int validFiles = 0;
			for (int i=0; i < files.length; i++) {
				String filename = files[i].getPath();
				parseFiles[i] = (filename.split("\\.")[1].equals(new String("sudoku")) ? filename : null);
				validFiles+= (parseFiles[i] != null ? 1 : 0);
			}

			loadedData = new String[validFiles];

			int j=0;
			for (int i=0; i < parseFiles.length; i++) {
				if (parseFiles[i] != null) {
					loadedData[j] = rawBoardData(parseFiles[i]);
					j++;
				}
				
			}
		}

		return loadedData;
	}
	
	// Using the default file location
	private String rawBoardData() {
		return rawBoardData(pathLocation);
	}
	
	/**
	 * @description retrieves the raw data stored in a .sudoku file
	 * @param pathname path to the given file
	 * @return the raw data from a given file */
	private String rawBoardData(String pathname) {
		dataBuilder = new StringBuilder();
		
		try {
			fileReader = new FileReader(pathname);
			buffer = new BufferedReader(fileReader);
			String dataline = new String();
			
			while ((dataline = buffer.readLine()) != null) {
				dataBuilder.append(dataline + hyphen);
			}
		} catch (IOException e) {
			System.out.println("Fatal System Error!");
			e.printStackTrace();
		}
		
		return dataBuilder.toString();
	}
}
