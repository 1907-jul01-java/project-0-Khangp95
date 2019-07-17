package com.revature;

public class App {
    public static void main(String[] args) {
    	ConnectionUtil connection = new ConnectionUtil();
        MainMenu mainMenu = new MainMenu(connection.getConnection()); 
        mainMenu.displayMenu();
        connection.close();
    }
}
