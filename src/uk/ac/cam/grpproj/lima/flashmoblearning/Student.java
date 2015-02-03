package uk.ac.cam.grpproj.lima.flashmoblearning;

/** A Student, i.e. an ordinary user. Just a marker class for now, User implements everything
 * important. */
public class Student extends User {

	/** Only called by LoginManager. */
	public Student(long id, String name, String epass) {
		super(id, name, epass);
	}

}
