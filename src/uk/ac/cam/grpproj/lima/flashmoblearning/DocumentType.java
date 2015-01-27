package uk.ac.cam.grpproj.lima.flashmoblearning;

/** A document type */
public enum DocumentType {
	
	/** A plain text document */
	PLAINTEXT(false),
	/** A Skulpt program */
	SKULPT(true),
	/** A Sonic Pi program */
	PI(true);
	
	DocumentType(final boolean r) {
		runnable = r;
	}

	/** Is this a program that can be run, previewed etc? */
	public final boolean runnable;
	
}
