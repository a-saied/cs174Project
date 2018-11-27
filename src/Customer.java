import java.util.*;
import java.sql.*;

public class Customer {
	String name;
	String taxID;
	String address;
	private String pin;
	HashMap<String, Account> accounts = new HashMap<>(); 

	public Customer(String taxID, String name, String address, String pin) {
		String query = "INSERT INTO CUSTOMERS (taxID, name, address, PIN) VALUES (" + taxID + ", " + name + ", " + address + ", " + pin + ")";

		try {
			JDBC.statement = JDBC.connection.createStatement();
			ResultSet nameRS = JDBC.statement.executeQuery(query);
		} catch (SQLException e) { e.printStackTrace(); }

	}

	public boolean verifyPIN(String comparedPin) {
		return (this.pin == comparedPin);
	}

	public boolean setPIN(String oldPin, String newPin) {
		if (oldPin == this.pin) {
			pin = newPin;
		}
		else {
			System.out.println("WRONG PIN");
		}
	}

	public void purchase(int accountId, int amount) {
		accounts.get(accountId).withdraw(amount);
	}

}