
public enum DocumentType {
	
	PLAINTEXT {

		@Override
		public Editor createEditor(Document d, boolean runOnly) {
			// TODO Auto-generated method stub
			return null;
		}
		
	},
	SKULPT {

		@Override
		public Editor createEditor(Document d, boolean runOnly) {
			// TODO Auto-generated method stub
			return null;
		}
		
	},
	PI {

		@Override
		public Editor createEditor(Document d, boolean runOnly) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};

	abstract public Editor createEditor(Document d, boolean runOnly);
	
	public final boolean runnable;
	
}
