package com.revature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Admin{
	
	private Connection connection;
	
	public Admin(Connection connection) {
		super();
		this.connection = connection;
	}

	public void displayMenu() {
		adminDisplay();
		int selection = 0;
		while (selection != 7) {
			try {
				Scanner scanner = new Scanner(System.in);
				selection = scanner.nextInt();
				switch(selection) {
				case 1:
					System.out.println("1. Deposit");
					deposit();
					break;
				case 2:
					System.out.println("2. Withdraw");
					withdraw();
					break;
				case 3:
					System.out.println("3. Transfer");
					transfer();
					break;
				case 4:
					System.out.println("Viewing Regular Applications");
					reviewApplications();
					break;
				case 5:
					System.out.println("Viewing Customer's Account Information...");
					displayAccountInformation();
					break;
				case 6:
					System.out.println("Deleting Account");
					deleteAccount();
					break;
				case 7:
					System.out.println("Logging Out...");
					MainMenu menu = new MainMenu(connection);
					menu.displayMenu();
					break;
				}
			}catch(Exception e) {
				System.out.println("This is not a valid input. Please try again.");
				adminDisplay();
			}
			
		}
		
	}
	
	public void adminDisplay() {
		System.out.println();
		System.out.println("ADMINISTRATOR MENU");
		System.out.println();
		System.out.println("1. Deposit\n2. Withdraw\n3. Transfer\n4. View Applications\n5. View Customer's Account Information\n6. Delete Account\n7. Log Out");
	}

	public void deposit() {
		UserAccount account = new UserAccount();
		List<UserAccount> accounts = new ArrayList<>();
		UserDao userDao = new UserDao(connection);
		System.out.println("Which account do you want to deposit?");
		try {
			PreparedStatement pStatement = connection.prepareStatement("select * from account");
			ResultSet resultSet = pStatement.executeQuery();
			
			while(resultSet.next()) {
				account = new UserAccount();
				account.setAccountNumber(resultSet.getInt("number"));
				account.setBalance(resultSet.getInt("balance"));
				accounts.add(account);
			}
			if(accounts.size() == 0) {
				System.out.println("You don't have any open accounts");
				displayMenu();
			}
			for(UserAccount temp : accounts) {
				int accountNumber = temp.getAccountNumber();
				int balance = temp.getBalance();
				System.out.println("Account: "+accountNumber+" Balance: "+balance);
			}
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter the desired account number.");
			int accountSelected = scanner.nextInt();
			System.out.println("Enter deposit amount.");
			int amount = scanner.nextInt();
			if (amount < 0) {
				System.out.println("Deposit input is invalid. Please try again.");
				displayMenu();
			}
			int count = 0;
			for(UserAccount temp : accounts) {
				if(temp.getAccountNumber() == accountSelected) {
					count++;
					int newAmount = temp.getBalance() + amount;
					userDao.updateBalance(accountSelected, newAmount);
					System.out.println("Deposited "+amount+". New balance is "+newAmount);
					break;
				}
			}
			if(count == 0) {
				System.out.println("Account number input is invalid. Please try again.");
				displayMenu();
			}
			displayMenu();
		}catch(Exception e) {
			System.out.println("Invalid input. Please try again.");
			displayMenu();
			}
	displayMenu();
}
	
/**
 * A method that withdraws from any account.
 */
public void withdraw() {
	UserAccount account = new UserAccount();
	List<UserAccount> accounts = new ArrayList<>();
	UserDao userDao = new UserDao(connection);
	System.out.println("Which account do you want to deposit?");
	try {
		PreparedStatement pStatement = connection.prepareStatement("select * from account");
		ResultSet resultSet = pStatement.executeQuery();
		
		while(resultSet.next()) {				
			account = new UserAccount();
			account.setAccountNumber(resultSet.getInt("number"));
			account.setBalance(resultSet.getInt("balance"));
			accounts.add(account);
		}
		if(accounts.size() == 0) {
			System.out.println("You don't have any open accounts");
			displayMenu();
		}
		for(UserAccount temp : accounts) {
			int accountNumber = temp.getAccountNumber();
			int balance = temp.getBalance();
			System.out.println("Account: "+accountNumber+" Balance: "+balance);
		}
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the desired account number.");
		int accountSelected = scanner.nextInt();
		System.out.println("Enter withdraw amount.");
		int withdraw = scanner.nextInt();
		if (withdraw < 0) {
			System.out.println("Withdraw input is invalid. Please try again.");
			displayMenu();
		}
		int count = 0;
		for(UserAccount temp : accounts) {
			if(temp.getAccountNumber() == accountSelected) {
				count++;
				int newAmount = temp.getBalance() - withdraw;
				if(newAmount < 0) {
					System.out.println("Insufficient funds.");
					break;
				}
				userDao.updateBalance(accountSelected, newAmount);
				System.out.println("Withdrawed "+withdraw+". New balance is "+newAmount);
				break;
			}
		}
		if(count == 0) {
			System.out.println("Account number input is invalid. Please try again.");
			displayMenu();
		}
		displayMenu();
		}catch(Exception e) {
			System.out.println("Invalid input. Please try again.");
		displayMenu();
			}
		displayMenu();
	}


public void transfer() {
	UserAccount account = new UserAccount();
	List<UserAccount> accounts = new ArrayList<>();
	UserDao userDao = new UserDao(connection);
	System.out.println("Which account do you wish to transfer funds from?");
	try {
		PreparedStatement pStatement = connection.prepareStatement("Select * from account");
		ResultSet resultSet = pStatement.executeQuery();
		while(resultSet.next()) {
			account = new UserAccount();
			account.setAccountNumber(resultSet.getInt("number"));
			account.setBalance(resultSet.getInt("balance"));
			accounts.add(account);
		}
		if(accounts.size() == 0) {
			System.out.println("You don't have any open accounts");
			displayMenu();
		}
		for(UserAccount temp : accounts) {
			int accountNumber = temp.getAccountNumber();
			int balance = temp.getBalance();
			System.out.println("Account: "+accountNumber+" Balance: "+balance);
		}
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the account number to transfer from.");
		int accountFrom = scanner.nextInt();
		System.out.println("Enter transfer amount.");
		int amount = scanner.nextInt();
		System.out.println("Enter account number to transfer to.");
		int accountTo = scanner.nextInt();
		if (amount < 0) {
			System.out.println("Input amount is invalid. Please try again.");
			displayMenu();
		}
		int count = 0;
		for(UserAccount temp : accounts) {
			if(temp.getAccountNumber() == accountFrom) {
				count++;
				PreparedStatement pState = connection.prepareStatement("Select number,balance from account where number=?");
				pState.setInt(1, accountTo);
				resultSet = pState.executeQuery();
				if(resultSet.next() == false) {
					System.out.println("Account number to transfer to doesn't exist. Please try again.");
					displayMenu();
				}else {
					int newAmount = temp.getBalance() - amount;
					if(newAmount < 0) {
						System.out.println("Insufficient funds.");
						break;
					}
					userDao.updateBalance(accountFrom, newAmount);
					System.out.println("Transfered "+amount+". New balance is "+newAmount);
					do {
						account = new UserAccount();
						account.setAccountNumber(resultSet.getInt("number"));
						account.setBalance(resultSet.getInt("balance"));
					}while(resultSet.next());
					newAmount = account.getBalance() + amount;
					userDao.updateBalance(accountTo, newAmount);
					System.out.println("Transfer complete.");
					displayMenu();
				}
				break;
			}
		}
		if(count == 0) {
			System.out.println("Account tranferring from doesn't exist. Please try again.");
		}
		displayMenu();
	} catch(Exception e) {
		System.out.println("Invalid input. Please try again.");
		displayMenu();
	}
	displayMenu();
}


/**
 * Deletes account number based on the user's input.
 */
public void deleteAccount() {
	UserDao userDao = new UserDao(connection);
	System.out.println("Enter account number to delete.");
	Scanner scanner = new Scanner(System.in);
	int accountInput;
	try {
		try {
			accountInput = Integer.parseInt(scanner.nextLine().split(" ")[0]);
		} catch (NumberFormatException e) {
			accountInput = 0;
		}
		
		ResultSet resultSet = userDao.getAccount(accountInput);
		if(resultSet.next() == false) {
			System.out.println("Selected account doesn't exist or invalid.");
			displayMenu();
		}else {
			userDao.deleteAccount(accountInput);
			System.out.println("Successfully deleted account "+accountInput);
			displayMenu();
		}
		System.out.println("hello");
		
	}catch(Exception e) {
		System.out.println("Invalid input. Please try again.");
		displayMenu();
	}
	displayMenu();
}

public void reviewApplications() {
	try {
		UserDao userDao = new UserDao(connection);
		PreparedStatement pStatement = connection.prepareStatement("Select username from application");
		ResultSet resultSet = pStatement.executeQuery();
		int numApplication = 0;
		while(resultSet.next()) {
			numApplication++;
			System.out.println(resultSet.getString("username"));
		}
		if(numApplication == 0) {
			System.out.println("You have no open account application.");
			displayMenu();
		}
		resultSet = pStatement.executeQuery();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Select a username to accept/deny.");
		String username = scanner.nextLine();
		System.out.println("Do you accept/deny application?(y/n)");
		String input = scanner.nextLine();
		if(input.equals("y")) {
			int count = 0;
			while(resultSet.next()) {
				if(resultSet.getString("username").equals(username)) {
					count++;
					Random random = new Random();
					int randomAccount = 1000 + random.nextInt(9000);
					userDao = new UserDao(connection);
					userDao.insertUserAccount(username, randomAccount);
					userDao.insertAccount(randomAccount);
					userDao.deleteApplication(username);
					System.out.println("Accepted application for an account for "+username+" with account number "+randomAccount);
				}
			}
			if(count == 0) {
				System.out.println("Username input was invalid. Please try again.");
			}
		} else if(input.equals("n")) {
			userDao.deleteApplication(username);
			System.out.println("Application for an account for "+username+" has been denied.");
		} else {
			System.out.println("Accept/Deny input is invalid. Please try again.");
		}
		
	}catch(Exception e) {
		System.out.println("Invalid input. Please try again.");
		displayMenu();
	}
	displayMenu();
}
public void displayAccountInformation() {
	UserDao userDao = new UserDao(connection);
	userDao.getAllAccounts();
	displayMenu();
}
}
