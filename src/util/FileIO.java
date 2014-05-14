package util;

import java.io.*;

public class FileIO {

	private FileReader fileReader;
	private BufferedReader buffer;
	private File file;

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
		System.out.println("Contains . = " + pathLocation.contains("."));
		
		if (parseFiles.length == 2 && parseFiles[1].equals(new String("sudoku"))) {
			loadedData = new String[1];
			parseFiles = new String[1];
			parseFiles[0] = pathLocation.split("/")[pathLocation.split("/").length-1];
//			System.out.println("File " + parseFiles[0] + " loaded from " + pathLocation); 
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
			
//			System.out.println("Valid File Count: " + validFiles);
			loadedData = new String[validFiles];
			
//			System.out.print("Valid Files: ");
			int j=0;
			for (int i=0; i < parseFiles.length; i++) {
				if (parseFiles[i] != null) {
//					System.out.println( (parseFiles[i] != null ? parseFiles[i] : "Invalid") + " ");
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
				dataBuilder.append(dataline);
			}
		} catch (IOException e) {
			System.out.println("Fatal System Error!");
			e.printStackTrace();
		}
		
		return dataBuilder.toString();
	}
}
