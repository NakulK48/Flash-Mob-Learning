package uk.ac.cam.grpproj.lima.flashmoblearning;

public enum DocumentType {
	
	PLAINTEXT(false) {

		@Override
		public Editor createEditor(Document d, boolean runOnly) {
			// TODO Auto-generated method stub
			return null;
		}
		
	},
	SKULPT(true) {

		@Override
		public Editor createEditor(Document d, boolean runOnly) {
			// TODO Auto-generated method stub
			return null;
		}
		
	},
	PI(true) {

		@Override
		public Editor createEditor(Document d, boolean runOnly) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
	DocumentType(final boolean r) {
		runnable = r;
	}

	abstract public Editor createEditor(Document d, boolean runOnly);
	
	public final boolean runnable;
	
}
