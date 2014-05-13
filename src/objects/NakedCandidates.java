package objects;

import java.util.LinkedList;

public class NakedCandidates {

	public LinkedList<String> values;
	public int x, y;
	
	public NakedCandidates(int size) {
		values = new LinkedList<String>();
	}
	
	public boolean moreValues() {
		return (values.size() > 0);
	}
	
	public void remove(int x) {
		values.remove(x);
	}
}
