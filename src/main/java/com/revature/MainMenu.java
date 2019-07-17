package com.revature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu {
	private Connection connection;

	public void displayMenu() {
		int selection = 0;
		System.out.println("WELCOME");
		while (selection != 3) {
			System.out.println("Please make a selection");
			System.out.println("1. Log In \n2. Sign Up \n3. Exit");
			Scanner scanner = new Scanner(System.in);
			try {
				selection = scanner.nextInt();
				switch (selection) {
				case 1:
					// login as existing user
					login();

					break;
				case 2:
					// create a new account
					signUp();

					break;

				case 3:
					// exit
					scanner.close();
					System.out.println("Thank you! Have a nice day!");
					System.exit(0);

					break;
				default:
					System.out.println("Invalid option, please try again.");
				}
			} catch (Exception e) {
				System.out.println("Invalid option, please try again.");
			}
		}
	}

	public MainMenu(Connection connection) {
		this.connection = connection;
	}

	public void login() {
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.println("Username: ");
			String userName = scanner.nextLine();
			System.out.println("Password: ");
			String password = scanner.nextLine();

			boolean check = loginChecker(userName, password);
			if (check == true) {
				AccountMenu accountMenu = new AccountMenu(userName, connection);
				accountMenu.displayMenu();
			} else {
				System.out.println("User not found");
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public Boolean loginChecker(String userName, String password) {
		int count = 0;
		try {
			PreparedStatement pStatement = connection
					.prepareStatement("select * from users where username=? and password=?");
			pStatement.setString(1, userName);
			pStatement.setString(2, password);
			ResultSet resultSet = pStatement.executeQuery();
			while (resultSet.next()) {

				count++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			;
		}
		if (count == 1) {
			System.out.println("Login successful.");
			return true;
		}
		System.out.println("Your username and password do not match. Please try again.");
		return false;

	}

	public void signUp() {
		try {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);

			System.out.println("Create a username.");
			String username = scanner.nextLine();
			System.out.println("Create a password.");
			String password = scanner.nextLine();
			boolean checkUser = hasUser(username, password);

			UserDao userDao = new UserDao(connection);

			if (!checkUser) {
				userDao.insert(new User(username, password, "customer"));
				System.out.println("Account registered. You may now log in.");
			}

		} catch (Exception e) {
			System.out.println("Invalid input. Please try again.");
			displayMenu();
		}
	}

	public Boolean hasUser(String username, String password) {

		int i = 0;
		char[] usernameCharacter = username.toCharArray();
		char passwordCharacter = password.charAt(0);
		boolean valid = true;
		try {
			if (username.isEmpty()) {
				System.out.println("Username cannot be empty. Please try again.");
				return true;
			} else if (Character.isDigit(username.charAt(0))) {
				System.out.println("Usernames cannot start with a digit. Please try again.");
				return true;
			} else if (passwordCharacter == ' ') {
				System.out.println("Invalid start of a password. Please try again");
				return true;
			}
			for (char c : usernameCharacter) {
				valid = ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9'));
				if (!valid) {
					System.out.println("Doesn't contain valid characters. Please try again.");
					return true;
				}
			}
			PreparedStatement pStatement = connection.prepareStatement("Select username from users where username=?");
			pStatement.setString(1, username);
			ResultSet resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				i++;
			}
			if (i == 1) {
				System.out.println("Username is already taken. Please input a new username.");
				return true;
			}
			if (password.isEmpty()) {
				System.out.println("Password cannot be empty.");
				return true;
			}

		} catch (SQLException e) {

		}
		return false;
	}

}