package objects;

public class HashTable {
	
	String[] stringArray;
	int[] intArray;
	
	public HashTable(String[] stringArray) {
		this.stringArray = stringArray;
	}
	
	public HashTable(int[] intArray) {
		this.intArray = intArray;
	}
	
	public HashTable() {}
	
	public boolean checkIntegerDuplicates(int[] intArray) {
		
		boolean duplicates = false;
		int iterations = intArray.length;
		int[] checkArray = new int[intArray.length];
		int i=0;
		while (i < iterations && !duplicates) {
			
			if (intArray[intArray[i]] != 0) {
				duplicates = (!duplicates);
			}
			i++;
		}
		
		return duplicates;
	}
}
