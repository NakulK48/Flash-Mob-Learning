package uk.ac.cam.grpproj.lima.flashmoblearning;

/** A tag was found but has been banned */
public class BannedTagException extends Exception {

	final String t;
	
	public BannedTagException(String name) {
		super("Banned: "+name);
		t = name;
	}

}
