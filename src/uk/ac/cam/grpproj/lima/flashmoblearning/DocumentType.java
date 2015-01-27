package uk.ac.cam.grpproj.lima.flashmoblearning;

public enum DocumentType {
	
	PLAINTEXT(false),
	SKULPT(true),
	PI(true);
	
	DocumentType(final boolean r) {
		runnable = r;
	}

	public final boolean runnable;
	
}
