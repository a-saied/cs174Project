import java.sql.*;
import java.util.*;

public class ATMFunctions {

	public ATMFunctions() {}

	public void deposit(double amount, String accountID) {
		String query = "UPDATE Accounts A SET A.balance = A.balance+" + amount + " WHERE A.aid=" + accountID;
		try {
			int rs = MainApp.stmt.executeUpdate(query);
			System.out.println(rs);
			//rs.close();
		} catch(SQLException se) { se.printStackTrace(); }
	}

	public void topUp(double amount, String pocketAccountID) {
		String addQuery = "UPDATE Accounts A SET A.balance = A.balance+" + amount + " WHERE A.aid=" + pocketAccountID;
		String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=(SELECT associatedID FROM Pocket P WHERE aid=" + pocketAccountID + ")";
		try {
			ResultSet rs1 = MainApp.stmt.executeQuery(addQuery);
			rs1.close();
		} catch(SQLException se) { se.printStackTrace(); }

		try {
			ResultSet rs2 = MainApp.stmt.executeQuery(subQuery);
			rs2.close();
		} catch(SQLException se)  { se.printStackTrace(); }

	}

	public void withdraw(double amount, String accountID) {
		System.out.println(amount);
		String query = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + accountID;
		try {
			ResultSet rs = MainApp.stmt.executeQuery(query);
			rs.close();
		} catch(SQLException se) { se.printStackTrace(); }
	}

	//Subtract money from the pocket account balance
	public void purchase(double amount, String pocketAccountID) {
		String query = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + pocketAccountID;
		try {
			ResultSet rs = MainApp.stmt.executeQuery(query);
			rs.close();
		} catch(SQLException se) { se.printStackTrace(); }
	}

	public void transfer() {}

	public void collect(double amount, String pocketAccountID) {
		//subtract from pocket account
		String subQuery = "UPDATE Accounts A SET A.balance = A.balance+" + amount + " WHERE A.aid=" + pocketAccountID;
		try {
			ResultSet rs = MainApp.stmt.executeQuery(subQuery);
			rs.close();
		} catch(SQLException se) { se.printStackTrace(); }

		//add to linked account
		String addQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + "WHERE A.aid = (SELECT associatedID FROM Pocket P WHERE aid=" + pocketAccountID + ")";
		try {
			ResultSet rs = MainApp.stmt.executeQuery(addQuery);
			rs.close();
		} catch(SQLException se) { se.printStackTrace(); }
	}

	public void wire() {}

	public void payFriend(double amount, String pocketAccountID, String toPocketID) {
		purchase(amount, pocketAccountID);
		deposit(amount, toPocketID);
	}
}