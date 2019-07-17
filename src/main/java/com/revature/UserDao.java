package com.revature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User>{
	Connection connection;
	
	
	@Override
	public void insert(User user) {
		try {
			PreparedStatement pStatement = connection.prepareStatement("insert into users(username,password,usertype) values(?, ?, ?)");
			pStatement.setString(1, user.getUserName());
			pStatement.setString(2, user.getPassword());
			pStatement.setString(3, user.getUserType());
			pStatement.executeUpdate();
		} catch (SQLException e) {
			
		}
		
	}

	@Override
	public List<User> getAll() {
		User userName;
		List<User> userNames = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from users");
			while(resultSet.next()) {
				userName = new User();
				userName.setUserName(resultSet.getString("username"));
				userName.setPassword(resultSet.getString("password"));
				userName.setUserType(resultSet.getString("usertype"));
				userNames.add(userName);
			}
			
		} catch (SQLException e) {
			
		}
		return userNames;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void delete() {
		
	}
	
	public UserDao(Connection connection) {
		this.connection = connection;
	}
	
	public void insertUserAccount(String userName, int accountNumber) {
		try {
			PreparedStatement pStatement = connection.prepareStatement("insert into UserAccounts(username,accountnumber) "
					+ "values(?,?)");
			pStatement.setString(1, userName);
			pStatement.setInt(2, accountNumber);
			pStatement.executeQuery();
		} catch(SQLException e){
			e.getMessage();
		}
	}
	
	public void insertAccount(int accountNumber) {
		try {
			PreparedStatement pStatement = connection.prepareStatement("insert into account(number,balance) "
					+"values(?,?)");
			pStatement.setInt(1, accountNumber);
			pStatement.setInt(2, 0);
			pStatement.executeQuery();
		} catch(SQLException e) {
			e.getMessage();
		}
	}
	
	public void updateBalance(int accountNumber, int balance) {
		
		try {
			PreparedStatement pStatement = connection.prepareStatement("Update account set balance=? where number=?");
			pStatement.setInt(1, balance);
			pStatement.setInt(2, accountNumber);
			pStatement.executeQuery();
		} catch(SQLException e) {
			e.getMessage();
		}
	}
	
	public ResultSet getAccount(int accountNumber) {
		ResultSet resultSet = null;
		try {
			PreparedStatement pStatement = connection.prepareStatement("Select number,balance from account where number=?");
			pStatement.setInt(1, accountNumber);
			resultSet = pStatement.executeQuery();
			return resultSet;
		} catch(SQLException e) {
			
		}
		return resultSet;
	}
	
	public void apply(String username) {
		try {
			PreparedStatement pStatement = connection.prepareStatement("insert into application(username) values(?)");
			pStatement.setString(1,username);
			pStatement.executeQuery();
		} catch(SQLException e) {
			e.getMessage();
		}
	}
	
	public String getUserType(String username) {
		String usertype = null;
		try {
			PreparedStatement pStatement = connection.prepareStatement("Select usertype from users where username=?");
			pStatement.setString(1,username);
			ResultSet resultSet = pStatement.executeQuery();
			while(resultSet.next()) {
				usertype = resultSet.getString("usertype");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return usertype;
	}
	
	public void getAllAccounts() {
		try {
			PreparedStatement pStatement = connection.prepareStatement("Select users.username,userAccounts.accountnumber,account.balance "
					+ "from users,userAccounts,account where users.username=userAccounts.username and "
					+ "userAccounts.accountnumber=account.number "+
					"group by users.username,userAccounts.accountnumber,account.balance");
			ResultSet resultSet = pStatement.executeQuery();
			int count = 0;
			while(resultSet.next()) {
				count++;
				String username = resultSet.getString(1);
				int account = resultSet.getInt(2);
				int balance = resultSet.getInt(3);
				System.out.println("Username: "+username+"  Account Number: "+account+"  Balance: "+balance);
			}
			if(count == 0) {
				System.out.println("There are no open accounts.");
			}
		} catch(SQLException e) {
			
		}
	}
	
	public void deleteApplication(String username) {
		try {
			PreparedStatement pStatement = connection.prepareStatement("Delete from application where username=?");
			pStatement.setString(1, username);
			pStatement.executeQuery();
		}catch(SQLException e) {
			e.getMessage();
		}
	}
	
	public ResultSet getUser(String username) {
		ResultSet resultSet = null;
		try {
			PreparedStatement pStatement = connection.prepareStatement("Select username from users where username=?");
			pStatement.setString(1, username);
			resultSet = pStatement.executeQuery();
			return resultSet;
		}catch(SQLException e) {
			e.getMessage();
		}
		return resultSet;
	}
	
	
	public void deleteAccount(int accountNumber) {
		try {
			PreparedStatement pStatement = connection.prepareStatement("Delete from userAccounts where userAccounts.accountnumber=?");
			pStatement.setInt(1, accountNumber);
			pStatement.executeUpdate();
			pStatement = connection.prepareStatement("Delete from account where account.number=?");
			pStatement.setInt(1, accountNumber);
			pStatement.executeUpdate();
		}catch(SQLException e) {
			e.getMessage();
		}
		
	}

}
