package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.AbstractAccount;
import models.AbstractUser;
import models.UserAccount;
import util.ConnectionUtil;

public class UserAccountDAO implements IUserAccountDAO {

	@Override
	public int insert(int userId, int accountId) {
		// Adds a user/account pair of IDs into our table. 
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the user object for neat SQL implementation
			// The below updates all fields
			String sql = "INSERT INTO USERS_ACCOUNTS (USER_ID, ACCOUNT_ID) VALUES (?, ?)";
			
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, userId);
			stmnt.setInt(2, accountId);
			
			result = stmnt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public List<UserAccount> findAccountsByUser(int userId) {
		// Gets a list of all accounts associated with the user
		List<UserAccount> accountsByUser = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.

			String sql = "SELECT ACCOUNT_ID FROM USERS_ACCOUNTS WHERE USER_ID = ?"; // gets all accounts that are paired to the user ID 

			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, userId); // Fetches the account ID to compare to.

			ResultSet rs = stmnt.executeQuery(); // Right as this is executed, the query runs to the database and grabs the info

			while(rs.next()) { // For each entry in the result set
				int accountId = rs.getInt("ACCOUNT_ID"); // Grab the account id
				UserAccount ua = new UserAccount(userId,accountId); // make user-account object
				accountsByUser.add(ua); // add user-account object to the list
			}

		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<UserAccount>(); // If something goes wrong, return an empty list.
		}
		return accountsByUser; // Successful return
	}

	@Override
	public List<UserAccount> findUsersByAccount(AbstractAccount a) {
		// Get all users associated with an account id
		List<UserAccount> usersByAccount = new ArrayList<>();
		int accountId = a.getAccountId();

		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.

			String sql = "SELECT USER_ID FROM USERS_ACCOUNTS WHERE ACCOUNT_ID = ?"; // gets all users 

			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, accountId); // Fetches the account ID to compare to.

			ResultSet rs = stmnt.executeQuery(); // Right as this is executed, the query runs to the database and grabs the info

			while(rs.next()) { // For each entry in the result set
				int userId = rs.getInt("USER_ID"); // Grab the user id
				UserAccount ua = new UserAccount(userId,accountId); // make user-account object
				usersByAccount.add(ua); // add user-account object to the list
			}

		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<UserAccount>(); // If something goes wrong, return an empty list.
		}
		return usersByAccount; // Successful return
	}

	@Override
	public List<UserAccount> findAll() {
		List<UserAccount> usersByAccount = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection()) {// This is a 'try with resources' block. 
			//Allows us to instantiate some variable, and at the end of try it will auto-close 
			//to prevent memory leaks, even if exception is thrown.

			String sql = "SELECT * FROM USERS_ACCOUNTS"; // gets all users 

			PreparedStatement stmnt = conn.prepareStatement(sql);

			ResultSet rs = stmnt.executeQuery(); // Right as this is executed, the query runs to the database and grabs the info

			while(rs.next()) { // For each entry in the result set
				int userId = rs.getInt("USER_ID"); // Grab the user id
				int accountId = rs.getInt("ACCOUNT_ID");
				UserAccount ua = new UserAccount(userId,accountId); // make user-account object
				usersByAccount.add(ua); // add user-account object to the list
			}

		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<UserAccount>(); // If something goes wrong, return an empty list.
		}
		return usersByAccount; // Successful return
	}

	@Override
	public int deleteByUser(AbstractUser u) {
		// Adds a user/account pair of IDs into our table. 
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the user object for neat SQL implementation
			int userId = u.getUserId();

			// The below updates all fields
			String sql = "DELETE FROM USERS_ACCOUNTS WHERE USER_ID = ?";

			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, userId);

			result = stmnt.executeUpdate(); // Run the delete statement
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

	@Override
	public int deleteByAccount(AbstractAccount a) {
		// Removes records that contain the appropriate account ID 
		int result = 0;
		try (Connection conn = ConnectionUtil.getConnection()) {
			// The below 'unpacks' all the information in the user object for neat SQL implementation
			int accountId = a.getAccountId();

			// The below updates all fields
			String sql = "DELETE FROM USERS_ACCOUNTS WHERE ACCOUNT_ID = ?";

			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, accountId);

			result = stmnt.executeUpdate(); // Run the delete statement
		} catch(SQLException e) {
			e.printStackTrace();
			return result; // If something goes wrong, return 0 for '0 changed rows'.
		}
		return result;
	}

}
