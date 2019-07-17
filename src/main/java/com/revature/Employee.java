package com.revature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.Scanner;

public class Employee {
	private Connection connection;

	public Employee(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public void displayMenu() {
		employeeDisplay();
		
		int selection = 0;
		while(selection != 3) {
			try {
				Scanner scanner = new Scanner(System.in);
				selection = scanner.nextInt();
				switch(selection) {
				case 1:
					System.out.println("Viewing Customer's Account Information");
					displayAccountInformation();
					break;
				case 2:
					System.out.println("Viewing Regular Applications");
					reviewApplications();
					break;
				case 3:
					System.out.println("Logging Out...");
					MainMenu menu = new MainMenu(connection);
					menu.displayMenu();
					break;
				}
			}catch(Exception e) {
				System.out.println("This is not a valid input. Please try again.");
				employeeDisplay();
			}
		}
	}
	
	public void employeeDisplay() {
		System.out.println();
		System.out.println("EMPLOYEE MENU");
		System.out.println();
		System.out.println("1. View Customer's Account Information\n2. View Applications\n3. Log Out");
	}
	
	
	public void displayAccountInformation() {
		UserDao userDao = new UserDao(connection);
		userDao.getAllAccounts();
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
	
	
	
	

}
