package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateEntryException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** Base of Student and Teacher. */
public class User {
	
	static final SecureRandom rng = new SecureRandom();
	
	/** Name of the user. Can be changed by administrator. */
	private String name;

	/** Get the user's name. */
	public String getName() {
		return name;
	}

	/** Change the user's name.
	 * @throw DuplicateEntryException There is already another user with the new name. */
	public void setName(String n) throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateEntryException {
		name = n;
		LoginManager.getInstance().modifyUser(this);
	}
	
	/** User has a fixed numerical ID, even if their name is changed. This is 
	 * set by the database when the document is first stored, and cannot be 
	 * changed after that. */
	private long id;
	/** Password, may be hashed/salted. */
	private String encryptedPassword;
	
	/** Called by database */
	public long getID() {
		return id;
	}
	
	/** Called by the database when a document is first stored */
	public void setID(long newID) throws IDAlreadySetException {
		if(this.id == -1) {
			this.id = newID;
		} else {
			if(id != newID) // OK to set to existing ID
				throw new IDAlreadySetException();
		}
	}
	
	static final int LEN_MIGRATE = 100;
	
	/** Try to log in.
	 * @return True if the password is correct. */
	public boolean checkPassword(String password) {
		if(encryptedPassword == null || encryptedPassword.equals("")) return false;
		// FIXME REMOVE BACK COMPATIBILITY
		if(encryptedPassword.length() < LEN_MIGRATE && password.equals(encryptedPassword)) {
			// Note that encrypted passwords are ~ 135 characters long...
			System.out.println("Upgrading old password to encrypted for "+name);
			try {
				setPassword(password);
			} catch (SQLException
					| NoSuchObjectException | DuplicateEntryException e) {
				// Ignore.
			}
			return true;
		}
		String[] split = encryptedPassword.split(":");
		if(split.length != 4) {
			System.err.println("ERROR: Password for "+name+" invalid");
			return false;
		}
		if(!split[0].equals("1")) {
			System.err.println("ERROR: Password for "+name+" encrypted using unknown version "+split[0]);
			return false;
		}
		int iterations;
		try {
			iterations = Integer.parseInt(split[1], 16);
			if(iterations > MAX_ITERATIONS) {
				System.err.println("ERROR: Password for "+name+" has invalid (too big) iterations "+split[1]);
				return false;
			}
		} catch (NumberFormatException e) {
			System.err.println("ERROR: Password for "+name+" has invalid iterations "+split[1]);
			return false;
		}
		byte[] salt;
		byte[] hpw;
		try {
			salt = DatatypeConverter.parseHexBinary(split[2]);
			hpw = DatatypeConverter.parseHexBinary(split[3]);
		} catch (IllegalArgumentException e) {
			System.err.println("ERROR: Password for "+name+" has invalid salt or hash");
			return false;
		}
		byte[] opwBytes;
		try {
			opwBytes = password.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Error("Impossible: Get a better JVM", e);
		}
		Mac mac;
		try {
			mac = Mac.getInstance("HmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			throw new Error("Get a better JVM", e);
		}
		SecretKeySpec keySpec = new SecretKeySpec(salt, "HmacSHA256");
		try {
			mac.init(keySpec);
		} catch (InvalidKeyException e) {
			throw new Error("Get a better JVM", e);
		}
		byte[] temp = opwBytes;
		for(int i=0;i<ITERATIONS;i++) {
			temp = mac.doFinal(temp);
		}
		return (MessageDigest.isEqual(temp, hpw));
	}
	
	static final int ITERATIONS = 1024; // Pi is slow!
	static final int MAX_ITERATIONS = ITERATIONS; // Could be different
	
	/** Set password 
	 * @throws DuplicateEntryException Should not happen unless there are 
	 * renames happening at the same time.
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public void setPassword(String newPassword) throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateEntryException {
		if(encryptedPassword.length() >= LEN_MIGRATE && checkPassword(newPassword)) return;
		byte[] pwb;
		try {
			pwb = newPassword.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			throw new Error("Impossible: Get a better JVM", e1);
		}
		byte[] salt = new byte[32];
		rng.nextBytes(salt);
		Mac mac;
		try {
			mac = Mac.getInstance("HmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			throw new Error("Get a better JVM", e);
		}
		SecretKeySpec keySpec = new SecretKeySpec(salt, "HmacSHA256");
		try {
			mac.init(keySpec);
		} catch (InvalidKeyException e) {
			throw new Error("Get a better JVM", e);
		}
		byte[] temp = pwb;
		for(int i=0;i<ITERATIONS;i++) {
			temp = mac.doFinal(temp);
		}
		encryptedPassword = "1:" // version
				+ Integer.toHexString(ITERATIONS) + ":" +
				DatatypeConverter.printHexBinary(salt) + ":" +  
				DatatypeConverter.printHexBinary(temp);
		LoginManager.getInstance().modifyUser(this);
	}

	/** Only called by database tests */
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	
	/** Only called by LoginManager. See LoginManager.createUser(). */
	public User(long id, String name, String epass) {
		this.id = id;
		this.name = name;
		this.encryptedPassword = epass;
		if(name == null) throw new NullPointerException();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return name.equals(other.name);
	}


}
