package util;

import java.io.*;

public class FileIO {

	private FileReader fileReader;
	private BufferedReader buffer;
	private File file;

	private static String hyphen = new String("-");
	private StringBuilder dataBuilder;
	private String pathLocation;
	
	
	public FileIO() {
		pathLocation = null;
		fileReader = null;
		buffer = null;
		file = null;
	}
	
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
	
	public String[] loadAllBoardData() {
		
		if (pathLocation == null) {
			System.out.println("No loaded Path Location");
			return new String[1];
		}
		
		String[] loadedData = null;
		String[] parseFiles = pathLocation.split("\\.");
		
		if (parseFiles.length == 2 && parseFiles[1].equals(new String("sudoku"))) {
			loadedData = new String[1];
			parseFiles = new String[1];
			parseFiles[0] = pathLocation.split("/")[pathLocation.split("/").length-1];
			loadedData[0] = rawBoardData();
		}
		
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
	
	private String rawBoardData() {
		return rawBoardData(pathLocation);
	}
	
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
