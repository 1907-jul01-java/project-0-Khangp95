Console Banking Application

Khang Pham

This a banking app that uses Java and SQL to simulate the functions of a banking system.

# User Stories
- Users should be able login using a username and password as credentials.
- Customers of the bank will have the option of applying to open a new account 
- Once an account is open, customers will be able withdraw,deposit and transfer funds between accounts.
- Employees and Admins of the bank may both view all of their customer's info
- Employees and Admins will be able to approve or deny open applications for accounts
- Admins are able to withdraw, deposit, and transfer from all accounts
- Admins also have access to cancel accounts

# Instructions
Build Postgres postgres
Change directory into /db and run:

docker build -t postgres 

Then run a container:

docker run -d -p 5432:5432 postgres

Compile, Package, & Execute with Maven
To compile and execute, run:

mvn compile

mvn exec:java

To package an executable jar and execute, run:


mvn clean package

java -jar target/project0-1.0-SNAPSHOT.jar