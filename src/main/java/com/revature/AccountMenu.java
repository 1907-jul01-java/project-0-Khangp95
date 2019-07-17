package com.revature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountMenu {
	private String userName;
	private Connection connection;

	public void displayMenu() {
		UserDao userDao = new UserDao(connection);
		String usertype = userDao.getUserType(userName);
		if (usertype.equals("employee")) {
			Employee employeeMenu = new Employee(connection);
			employeeMenu.displayMenu();
		} else if (usertype.equals("admin")) {
			Admin adminMenu = new Admin(connection);
			adminMenu.displayMenu();
		} else {
			accountMenu();
			int input = 0;
			while (input != 5) {
				Scanner accScanner = new Scanner(System.in);
				try {
					input = accScanner.nextInt();
					switch (input) {
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
						System.out.println("4. Apply for Regular Account");
						application();
						break;
					case 5:
						System.out.println("logging out");
						MainMenu menu = new MainMenu(connection);
						menu.displayMenu();
						break;
					default:
						System.out.println("This is not a valid menu choice. Try again");
					}

				} catch (Exception e) {
					System.out.println("This is not a valid input. Please try again.");
					accountMenu();
				}
			}
		}
		return;
	}

	public void accountMenu() {
		System.out.println();
		System.out.println("ACCOUNT MENU");
		System.out.println("------------");
		System.out.println("1. Deposit\n2. Withdraw\n3. Transfer\n4."
				+ " Apply for Regular Account\n5. Logout");
	}

	public AccountMenu(String username, Connection connection) {
		this.userName = username;
		// this.category = category;
		this.connection = connection;
	}

	public void deposit() {
		UserAccount account = new UserAccount();
		List<UserAccount> accounts = new ArrayList<>();
		UserDao userDao = new UserDao(connection);
		System.out.println("Which account do you want to deposit?");
		try {
			PreparedStatement pStatement = connection.prepareStatement(
					"select userAccounts.username as person, account.number as accnumber, account.balance as amount from userAccounts, account where userAccounts.accountnumber=account.number and userAccounts.username=?");
			pStatement.setString(1, userName);
			ResultSet resultSet = pStatement.executeQuery();

			while (resultSet.next()) {
				account = new UserAccount();
				account.setName(resultSet.getString("person"));
				account.setAccountNumber(resultSet.getInt("accnumber"));
				System.out.println(resultSet.getString("person")+" #" + resultSet.getInt("accnumber"));
				account.setBalance(resultSet.getInt("amount"));
				accounts.add(account);
			}
			if (accounts.size() == 0) {
				System.out.println("You don't have any open accounts");
				displayMenu();
			}
			for (UserAccount temp : accounts) {
				System.out.println(temp.toString());
			}
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter the desired account number.");
			int accountSelected = scanner.nextInt();
			System.out.println("Enter deposit amount.");
			int amount = scanner.nextInt();
			if (amount < 0) {
				System.out.println("Input amount is invalid. Please try again.");
				displayMenu();
			}
			int count = 0;
			for (UserAccount temp : accounts) {
				if (temp.getAccountNumber() == accountSelected) {
					count++;
					int newAmount = temp.getBalance() + amount;
					userDao.updateBalance(accountSelected, newAmount);
					System.out.println("Deposited " + amount + ". New balance is " + newAmount);
					break;
				}
			}
			if (count == 0) {
				System.out.println("Account Number doesn't exist. Please try again.");
			}
			displayMenu();

		} catch (Exception e) {
			System.out.println("Invalid input. Please try again.");
			displayMenu();
		}
	}

	public void withdraw() {
		UserAccount account = new UserAccount();
		List<UserAccount> accounts = new ArrayList<>();
		UserDao userDao = new UserDao(connection);
		System.out.println("Which account do you want to withdraw?");
		try {
			PreparedStatement pStatement = connection.prepareStatement(
					"select userAccounts.username as person, account.number as accnumber, account.balance as amount from userAccounts, account where userAccounts.accountnumber=account.number and userAccounts.username=?");
			pStatement.setString(1, userName);
			ResultSet resultSet = pStatement.executeQuery();

			while (resultSet.next()) {
				account = new UserAccount();
				account.setName(resultSet.getString("person"));
				account.setAccountNumber(resultSet.getInt("accnumber"));
				System.out.println(resultSet.getString("person")+" #" + resultSet.getInt("accnumber"));
				account.setBalance(resultSet.getInt("amount"));
				accounts.add(account);
			}
			if (accounts.size() == 0) {
				System.out.println("You don't have any open accounts");
				displayMenu();
			}
			for (UserAccount temp : accounts) {
				System.out.println(temp.toString());
			}
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter the desired account number.");
			int accountSelected = scanner.nextInt();
			System.out.println("Enter withdraw amount.");
			int amount = scanner.nextInt();
			if (amount < 0) {
				System.out.println("Input amount is invalid. Please try again.");
				displayMenu();
			}
			int count = 0;
			for (UserAccount temp : accounts) {
				if (temp.getAccountNumber() == accountSelected) {
					count++;
					int newAmount = temp.getBalance() - amount;
					if(newAmount < 0) {
						System.out.println("Insufficient funds.");
						break;
					}
					userDao.updateBalance(accountSelected, newAmount);
					System.out.println("Withdrawed " + amount + ". New balance is " + newAmount);
					break;
				}
			}
			if(count == 0) {
				System.out.println("Account doesn't exist. Please try again.");
			}
			displayMenu();
		} catch (Exception e) {
			System.out.println("Invalid input. Please try again.");
			displayMenu();
		}
	}

	public void transfer() {
		UserAccount account = new UserAccount();
		List<UserAccount> accounts = new ArrayList<>();
		UserDao userDao = new UserDao(connection);
		System.out.println("Which account do you want to tranfer money from?");
		try {
			PreparedStatement pStatement = connection.prepareStatement(
					"select userAccounts.username as person, account.number as accnumber, account.balance as amounts from userAccounts, account where userAccounts.accountnumber=account.number and userAccounts.username=?");
			pStatement.setString(1, userName);
			ResultSet resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				account = new UserAccount();
				account.setName(resultSet.getString("person"));
				account.setAccountNumber(resultSet.getInt("accnumber"));
				account.setBalance(resultSet.getInt("amounts"));
				System.out.println(resultSet.getString("person")+" #" + resultSet.getInt("accnumber") + " balance: " + resultSet.getInt("amounts"));
				accounts.add(account);
			}
			if (accounts.size() == 0) {
				System.out.println("You don't have any open accounts");
				displayMenu();
			}
			for (UserAccount temp : accounts) {
				System.out.println(temp.toString());
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
			for (UserAccount temp : accounts) {
				if (temp.getAccountNumber() == accountFrom) {
					count++;
					PreparedStatement pState = connection
							.prepareStatement("Select number,balance from account where number=?");
					pState.setInt(1, accountTo);
					resultSet = pState.executeQuery();
					if (resultSet.next() == false) {
						System.out.println("Account number to transfer to doesn't exist. Please try again.");
						displayMenu();
					} else {
						int newAmount = temp.getBalance() - amount;
						if(newAmount < 0) {
							System.out.println("Insufficient funds.");
							break;
						}
						userDao.updateBalance(accountFrom, newAmount);
						System.out.println("Transfered " + amount + ". New balance is " + newAmount);
						do {
							account = new UserAccount();
							account.setAccountNumber(resultSet.getInt("number"));
							account.setBalance(resultSet.getInt("balance"));
						} while (resultSet.next());
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
		} catch (Exception e) {
			System.out.println("Invalid input. Please try again.");
			displayMenu();
		}
	}

	public void application() {
		UserDao userDao = new UserDao(connection);
		userDao.apply(userName);
		System.out.println("Application to open account sent.");
		displayMenu();
	}
	

	
}