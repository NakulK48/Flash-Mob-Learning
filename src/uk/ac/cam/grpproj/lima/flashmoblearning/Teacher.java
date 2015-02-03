package uk.ac.cam.grpproj.lima.flashmoblearning;

/** Administrative account. (Almost) everything is implemented in User, so this is a marker. */
public class Teacher extends User {

	/** Only called by LoginManager. */
	public Teacher(long id, String name, String epass) {
		super(id, name, epass);
	}

}
