package uk.ac.cam.grpproj.lima.flashmoblearning;

/** A document type */
public enum DocumentType {
	
	/** A plain text document */
	PLAINTEXT(false, 0),
	/** A Skulpt program */
	SKULPT(true, 1),
	/** A Sonic Pi program */
	PI(true, 2);
	
	DocumentType(final boolean r, int i) {
		runnable = r;
		id = i;
	}

	/** Is this a program that can be run, previewed etc? */
	public final boolean runnable;
	public final int id;

	public int getId() { return id; }

	private static DocumentType[] values = null;
	public static DocumentType getValue(int id) {
		if(DocumentType.values == null) {
			DocumentType.values = DocumentType.values();
		}
		for(int i = 0; i < values.length; i++)
		{
			if(values[i].getId() == id)
				return values[i];
		}
		return PLAINTEXT;
	}

}
