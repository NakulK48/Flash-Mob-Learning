package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import uk.ac.cam.grpproj.lima.flashmoblearning.Student;
import uk.ac.cam.grpproj.lima.flashmoblearning.Teacher;
import uk.ac.cam.grpproj.lima.flashmoblearning.User;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateEntryException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The login manager provides read and write access to users and the login banner within
 * the database. All search, create and listing functionality is implemented here as well.
 */
public class LoginManager {

	private Database m_Database;

	/**
	 * Creates a new LoginManager instance with given database.
	 * @param database an initialised database.
	 */
	protected LoginManager(Database database) {
		m_Database = database;
	}

	/**
	 * Obtain the static LoginManager instance.
	 * @return The static LoginManager instance.
	 * @throws NotInitializedException the static LoginManager instance isn't initialised.
	 */
	public static LoginManager getInstance() throws NotInitializedException {
		return Database.getInstance().getLoginManager();
	}
	
	/**
	 * Retrieve a user by username from the database.
	 * @param username username to lookup.
	 * @return The user object for the given username.
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException the user was not found.
	 */
	public User getUser(String username) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM users WHERE username = ?");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();

		if(!rs.next()) throw new NoSuchObjectException("user " + username);
		return getUserFromResultSet(rs);
	}

	/**
	 * Retrieve a user by user id from the database.
	 * @param id id to lookup.
	 * @return The user object for the given user id.
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException the user was not found.
	 */
	public User getUser(long id) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM users WHERE id = ?");
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();

		if(!rs.next()) throw new NoSuchObjectException("user " + id);
		return getUserFromResultSet(rs);
	}

    /**
     * Returns a list of users from the result set obtained from a user query.
     * @param rs ResultSet of users.
     * @return The list of users in the result set.
     * @throws SQLException an error has occurred in the database.
     */
	private List<User> getUsersFromResultSet(ResultSet rs) throws SQLException {
		List<User> ret = new ArrayList<User>();

		while(rs.next())
			ret.add(getUserFromResultSet(rs));

		return ret;
	}

    /**
     * Return a user from the result set obtained from a user query.
     * @param rs ResultSet of users.
     * @return The user in the result set.
     * @throws SQLException an error has occurred in the database.
     */
	private User getUserFromResultSet(ResultSet rs) throws SQLException {
		if(rs.getBoolean("teacher_flag"))
			return new Teacher(rs.getLong("id"), rs.getString("username"), rs.getString("password"));
		else
			return new Student(rs.getLong("id"), rs.getString("username"), rs.getString("password"));
	}

	/**
	 * Deletes a user (and all its' documents, etc.) from the database.
	 * @param user specified user to delete.
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException the user was not found.
	 */
	public void deleteUser(User user) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM users WHERE id = ?");
		ps.setLong(1, user.getID());

		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("user " + user.getID());
	}
	
	/**
	 * Creates a new user in the database.
	 * @param username username of new user.
	 * @param saltedPassword salted password of new user.
	 * @param teacher boolean to indicate if new user is a teacher/admin.
	 * @return The user object after creation.
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException the user was not found.
	 * @throws uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateEntryException the username already exists.
	 */
	public User createUser(String username, String saltedPassword, boolean teacher) throws SQLException, DuplicateEntryException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("INSERT INTO users (username, password, teacher_flag) VALUES (?, ?, ?)");
		ps.setString(1, username);
		ps.setString(2, saltedPassword);
		ps.setBoolean(3, teacher);

		// Catch any duplicate name exceptions and throw our own.
		try {
			ps.executeUpdate();
		} catch (SQLException e) {
			if(e.getMessage().toLowerCase().contains("duplicate")) throw new DuplicateEntryException();
			else throw e;
		}

		try {
			return getUser(username);
		} catch (NoSuchObjectException e) {
			// clearly, the creation failed!
			throw new SQLException("Failed to retrieve newly-created user - database error!");
		}
	}
	
	/** Modify a user, index by userId. */
	/**
	 * Modifies a user, indexed by the user id (name changes permitted).
	 * @param user user to modify (by user id)
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException the user was not found.
	 * @throws uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateEntryException the username already exists.
	 */
	public void modifyUser(User user) throws SQLException, NoSuchObjectException, DuplicateEntryException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("UPDATE users SET username = ?, password = ?, teacher_flag = ? WHERE id = ?");
		ps.setString(1, user.getName());
		ps.setString(2, user.getEncryptedPassword());
		ps.setBoolean(3, user instanceof Teacher); // TODO
		ps.setLong(4, user.getID());

		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("user " + user.getID());
	}

	
	/**
	 * Retrieves the login banner from the database.
	 * @return The login banner.
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException the banner was not found - database integrity failure.
	 */
	public String getLoginBanner() throws SQLException, NoSuchObjectException {
		ResultSet rs = m_Database.getStatement().executeQuery("SELECT * FROM settings WHERE setting_name = 'login_banner'");
		if(!rs.next()) throw new SQLException("Login banner does not exist - database corrupt.");

		return rs.getString("setting_value");
	}

	/**
	 * Sets/updates the login banner in the database.
	 * @param banner the new login banner.
	 * @throws SQLException an error has occurred in the database.
	 */
	public void setLoginBanner(String banner) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("UPDATE settings SET setting_value = ? WHERE setting_name = 'login_banner'");
		ps.setString(1, banner);
		ps.executeUpdate();
	}

	/**
	 * Retrieves all users from the database.
	 * @param param query parameter to filter/sort the results.
	 * @return A list of users matching the request.
	 * @throws SQLException an error has occurred in the database.
	 */
	public List<User> getAllUsers(QueryParam param) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM users"));
		ResultSet rs = ps.executeQuery();

		return getUsersFromResultSet(rs);
	}
}
