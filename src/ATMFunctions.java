import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class ATMFunctions {

	public ATMFunctions() {}

	JFrame f = new JFrame();
	//DONE
	public void deposit(double amount, String accountID) {
		if (checkAccountAccess(accountID)) {
			if(getAccountType(accountID).equals("Pocket")) {
				JOptionPane.showMessageDialog(f, "You cannot deposit into a pocket account.");  
			}
			if (isClosed(accountID)) {
				JOptionPane.showMessageDialog(f, "You cannot deposit into a closed account.");  
			}
			else {
				String updateQuery = "UPDATE Accounts SET balance = balance+" + amount + " WHERE aid=" + accountID;
				try {
					int rs = MainApp.stmt.executeUpdate(updateQuery);
					System.out.println("Updated: " + rs);
					addTransactionSingle("Deposit",  amount, accountID);
				} catch(SQLException se) { se.printStackTrace(); }
			}
		}
		else {
			JOptionPane.showMessageDialog(f,"You cannot deposit into a pocket account.");  
		}
	}

	//DONE
	//Move money from linked acc to pocket
	public void topUp(double amount, String pocketAccountID) {
		if (checkAccountAccess(pocketAccountID)) {
			if (getAccountType(pocketAccountID).equals("Pocket")) {
				String linkedAccountID = Integer.toString(getLinkedAccountID(pocketAccountID));
				if (isClosed(linkedAccountID)) {
					JOptionPane.showMessageDialog(f, "You cannot top-up from a closed account.");  
					return;
				}
				if (hasEnoughMoney(amount, linkedAccountID)) {
					String addQuery = "UPDATE Accounts A SET A.balance = A.balance+" + amount + " WHERE A.aid=" + pocketAccountID;
					String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=(SELECT associatedID FROM Pocket P WHERE aid=" + pocketAccountID + ")";
					try {
						int rs1 = MainApp.stmt.executeUpdate(addQuery);
						System.out.println("Updated1: " + rs1);
						//addTransaction("Top-Up", amount, pocketAccountID);
					} catch(SQLException se) { se.printStackTrace(); }

					String assQuery = "SELECT associatedID FROM Pocket P WHERE aid=" + pocketAccountID;
					try {
						int rs2 = MainApp.stmt.executeUpdate(subQuery);
						System.out.println("Updated2: " + rs2);
						ResultSet rs3 = MainApp.stmt.executeQuery(subQuery);
						//addTransaction("Top-Up", amount*-1.0, linkedAccountID);
						addTransactionDouble("Top-Up", amount, linkedAccountID, pocketAccountID);
						if (getBalance(linkedAccountID) <= 0.01) {
							markAsClosed(linkedAccountID);
						}
					} catch(SQLException se)  { se.printStackTrace(); }
				}
				else {
					JOptionPane.showMessageDialog(f, "You do not have enough money to top-up.");
				}
			}
			else {
				JOptionPane.showMessageDialog(f, "You cannot top-up from a checking or savings account.");
			}
		}
		else {
			JOptionPane.showMessageDialog(f, "The account does not exist or you do not have access to this account.");
		}
	}

	//DONE
	public void withdraw(double amount, String accountID) {
		if (isClosed(accountID)) {
			JOptionPane.showMessageDialog(f, "You cannot withdraw from a closed account.");  
			return;
		}
		if (checkAccountAccess(accountID)) {
			if (getAccountType(accountID).equals("Pocket")){
				JOptionPane.showMessageDialog(f, "Cannot withdraw from a pocket account.");
			}
			else {
				if (hasEnoughMoney(amount, accountID)) {
					String query = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + accountID;
					try {
						int rs = MainApp.stmt.executeUpdate(query);
						System.out.println("Updated: " + rs);
						if (getBalance(accountID) <= 0.01) {
							markAsClosed(accountID);
						}
						addTransactionSingle("Withdraw", amount, accountID);

					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					JOptionPane.showMessageDialog(f, "You do not have enough money to withdraw.");
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(f, "The account does not exist or you do not have access to this account.");
		}
	}

	//DONE
	//Subtract money from the pocket account balance
	public void purchase(double amount, String pocketAccountID) {
		if (checkAccountAccess(pocketAccountID)) {
			if (getAccountType(pocketAccountID).equals("Pocket")){
				if (hasEnoughMoney(amount, pocketAccountID)) {
					String query = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + pocketAccountID;
					try {
						int rs = MainApp.stmt.executeUpdate(query);
						System.out.println("Update: " + rs);
						addTransactionSingle("Purchase", amount, pocketAccountID);
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					JOptionPane.showMessageDialog(f, "You do not have enough money to purchase.");
				}
			}
			else {
				JOptionPane.showMessageDialog(f, "You cannot purchase from a checking or savings account.");
			}
		}
		else {
			JOptionPane.showMessageDialog(f, "The account does not exist or you do not have access to this account.");
		}
	}

	public void transfer(double amount, String fromAccountID, String toAccountID) {
		if (isClosed(fromAccountID) || isClosed(toAccountID)) {
			JOptionPane.showMessageDialog(f, "You cannot transfer from/to a closed account.");  
			return;
		}
		if (checkAccountAccess(fromAccountID)) {
			if (getAccountType(fromAccountID).equals("Pocket") || getAccountType(toAccountID).equals("Pocket")){
				JOptionPane.showMessageDialog(f, "You cannot transfer to/from a pocket account.");
				return;
			}
			if (amount > 2000) {
				JOptionPane.showMessageDialog(f, "You cannot transfer > $2000.");
				return;
			}
			String query = "SELECT COUNT (taxID) FROM Owned_By O, Accounts A1 WHERE A1.aid=" + fromAccountID + " AND O.aid=A1.aid AND O.taxID IN (SELECT taxID FROM Accounts A2 WHERE A2.aid=" + toAccountID+ ")";
			try {
				ResultSet rs = MainApp.stmt.executeQuery(query);
				while (rs.next()) {
					System.out.println("Num of taxIDs: " + rs.getInt(1));
					if (rs.getInt(1) < 1) {
						JOptionPane.showMessageDialog(f, "The account does not exist or you do not have access to this account.");
						return;
					}
				}
				if (hasEnoughMoney(amount, fromAccountID)) {
					//subtract from account
					String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + fromAccountID;
					try {
						int rs1 = MainApp.stmt.executeUpdate(subQuery);
						System.out.println("Updated1: " + rs1);
						if (getBalance(fromAccountID) <= 0.01) {
							markAsClosed(fromAccountID);
						}
					} catch(SQLException se) { se.printStackTrace(); }

					//add to account
					String addQuery = "UPDATE Accounts A SET A.balance = A.balance+" + amount + " WHERE A.aid=" + toAccountID;
					try {
						int rs2 = MainApp.stmt.executeUpdate(addQuery);
						System.out.println("Updated2: " + rs2);
						addTransactionDouble("Transfer", amount, fromAccountID, toAccountID);
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					JOptionPane.showMessageDialog(f, "You do not have enough funds to transfer.");
				}

			} catch(SQLException se) { se.printStackTrace(); }
		}
		else {
			JOptionPane.showMessageDialog(f, "The account does not exist or you do not have access to this account.");
		}
	}

	public void collect(double amount, String pocketAccountID) {
		if (checkAccountAccess(pocketAccountID)) {
			if (getAccountType(pocketAccountID).equals("Pocket")){
				if (hasEnoughMoney(amount, pocketAccountID)) {
					int associatedID = -1;
					String assQuery = "SELECT associatedID FROM Pocket P WHERE aid=" + pocketAccountID;
					try {
						ResultSet rs3 = MainApp.stmt.executeQuery(assQuery);
						while (rs3.next()) {
							associatedID = rs3.getInt("associatedID");
						}					
					} catch(SQLException se) { se.printStackTrace(); }
					if (isClosed(Integer.toString(associatedID))) {
						JOptionPane.showMessageDialog(f, "You cannot collect from a closed account.");
						return;
					}
					//subtract from pocket account
					String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + pocketAccountID;
					try {
						int rs1 = MainApp.stmt.executeUpdate(subQuery);
						System.out.println("Updated1: " + rs1);
						//addTransaction("Collect", amount*-1.0, pocketAccountID);
					} catch(SQLException se) { se.printStackTrace(); }

					//add to linked account
					String addQuery = "UPDATE Accounts A SET A.balance = A.balance+" + (amount - (amount*.03)) + "WHERE A.aid = (SELECT associatedID FROM Pocket P WHERE aid=" + pocketAccountID + ")";
					try {
						int rs2 = MainApp.stmt.executeUpdate(addQuery);
						System.out.println("Updated2: " + rs2);
						addTransactionDouble("Collect", amount, pocketAccountID, Integer.toString(associatedID));
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					JOptionPane.showMessageDialog(f, "You do not have enough funds to collect.");
				}
			}
			else {
				JOptionPane.showMessageDialog(f, "You cannot collect from a checking or savings account.");
			}
		}
		else {
			JOptionPane.showMessageDialog(f, "The account does not exist or you do not have access to this account.");
		}
	}

	//DONE
	public void wire(double amount, String fromAccountID, String toAccountID) {
		if (isClosed(fromAccountID) || isClosed(toAccountID)) {
			JOptionPane.showMessageDialog(f, "You cannot wire to/from a closed account.");
			return;
		}
		if (checkAccountAccess(fromAccountID) && checkAccountAccess(toAccountID)) {
			if (!getAccountType(fromAccountID).equals("Pocket") && !getAccountType(toAccountID).equals("Pocket")) {
				//subtract from pocket account
				if (hasEnoughMoney(amount, fromAccountID)) {
					String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + fromAccountID;
					try {
						int rs1 = MainApp.stmt.executeUpdate(subQuery);
						System.out.println("Updated1: " + rs1);
						if (getBalance(fromAccountID) <= 0.01) {
							markAsClosed(fromAccountID);
						}
					} catch(SQLException se) { se.printStackTrace(); }

					//add to other account
					String addQuery = "UPDATE Accounts A SET A.balance = A.balance+" + (amount - (amount*.02)) + "WHERE A.aid=" + toAccountID;
					try {
						int rs2 = MainApp.stmt.executeUpdate(addQuery);
						System.out.println("Updated2: " + rs2);
						//addTransaction("Wire", amount, toAccountID);
						addTransactionDouble("Wire", amount, fromAccountID, toAccountID);
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					JOptionPane.showMessageDialog(f, "You do not have enough money to wire.");
				}
			}
			else {
				JOptionPane.showMessageDialog(f, "You cannot wire to/from a pocket account.");
			}
		}
		else {
			JOptionPane.showMessageDialog(f, "The account does not exist or you do not have access to this account.");
		}
	}

	//DONE
	public void payFriend(double amount, String fromPocketID, String toPocketID) {
		if (isClosed(Integer.toString(getLinkedAccountID(fromPocketID))) || isClosed(Integer.toString(getLinkedAccountID(fromPocketID)))) {
			JOptionPane.showMessageDialog(f, "You cannot pay a friend from/to a closed account.");
			return;
		}
		if (checkAccountAccess(fromPocketID)) {
			String checkQuery = "SELECT COUNT(aid) FROM Pocket P WHERE P.aid=" + toPocketID;
			try {
				ResultSet rs1 = MainApp.stmt.executeQuery(checkQuery);
				while (rs1.next()) {
					if (rs1.getInt(1) < 1) {
						JOptionPane.showMessageDialog(f, "The friend pocket ID does not exist.");
						return;
					}
				}
			} catch(SQLException se) { se.printStackTrace(); }

			if (getAccountType(fromPocketID).equals("Pocket") && getAccountType(toPocketID).equals("Pocket")){
				if (hasEnoughMoney(amount, fromPocketID)) {
					//add to friend's pocket
					String addQuery = "UPDATE Accounts A SET A.balance = A.balance+" + amount + " WHERE A.aid=" + toPocketID;
					try {
						int rs2 = MainApp.stmt.executeUpdate(addQuery);
						System.out.println("Udpate: " + rs2);
						//addTransaction("Pay-friend", amount, toPocketID);
					} catch(SQLException se) { se.printStackTrace(); }

					//subtract from my pocket
					String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + fromPocketID;
					try {
						int rs3 = MainApp.stmt.executeUpdate(subQuery);
						System.out.println("Udpate: " + rs3);
						//addTransaction("Pay-friend", amount*-1.0, fromPocketID);
						addTransactionDouble("Pay-Friend", amount, fromPocketID, toPocketID);
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					JOptionPane.showMessageDialog(f, "You do not have enough funds to pay.");
				}
			}
			else {
				JOptionPane.showMessageDialog(f, "You cannot pay to/from a checking or savings account.");
			}
		}
		else {
			JOptionPane.showMessageDialog(f, "The account does not exist or you do not have access to this account.");
		}
	}

	public void setPin(String oldPin, String newPin) {
		//get tax id from old pin
		String checkQuery = "SELECT taxID FROM Customers C WHERE C.pin=" + Integer.parseInt(oldPin)*2;
		try {
			ResultSet rs = MainApp.stmt.executeQuery(checkQuery);
			if (rs.next()) {
				if ((rs.getInt("taxid")) == MainApp.atmTaxID) {
					String updateQuery = "UPDATE Customers SET pin=" + Integer.parseInt(newPin*2) + " WHERE taxid=" + MainApp.atmTaxID;
					int rs2 = MainApp.stmt.executeUpdate(updateQuery);
					System.out.println("Updated: " + rs2);
				}
				else {
					JOptionPane.showMessageDialog(f, "The PIN you inputted does not match your account's pin.");
				}
			}
			else {
				JOptionPane.showMessageDialog(f, "The PIN you inputted does not match your account's pin.");
			}
		} catch(SQLException se) { se.printStackTrace(); }
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Checks if the user is an owner of the account
	public boolean checkAccountAccess(String accountID) {
		String query = "SELECT COUNT(taxid) FROM Owned_By OB WHERE OB.aid=" + accountID + " AND OB.taxID=(SELECT taxID FROM Customers C WHERE C.taxID=" + MainApp.atmTaxID + ")";
		try {
			ResultSet count = MainApp.stmt.executeQuery(query);
			while (count.next()) {
				if (count.getInt(1) > 0) {
					return true;
				}
			}
		} catch(SQLException se) { se.printStackTrace(); }
		return false;
	}

	public String getAccountType(String accountID) {
		String query = "SELECT A.type FROM Accounts A WHERE A.aid=" + accountID;
		try  {
			ResultSet rs = MainApp.stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getString(1).trim();
			}
		} catch(SQLException se) { se.printStackTrace(); }
		return "";
	}

	//Checks to see if the user has enough money to make a transaction
	public boolean hasEnoughMoney(double amount, String accountID) {
		String query = "SELECT A.balance FROM Accounts A WHERE A.aid=" + accountID;
		try  {
			ResultSet rs = MainApp.stmt.executeQuery(query);
			while (rs.next()) {
				if (rs.getDouble(1) - amount < 0.0) {
					return false;
				}
			}
		} catch(SQLException se) { se.printStackTrace(); }
		return true;
	}

	public int getLinkedAccountID(String pocketAccountID) {
		String query = "SELECT P.associatedID FROM Pocket P WHERE P.aid=" + pocketAccountID;
		try  {
			ResultSet rs = MainApp.stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch(SQLException se) { se.printStackTrace(); }
		return -1;
	}

	public boolean isClosed(String accountID) {
		String query = "SELECT closed FROM Accounts A WHERE A.aid=" + accountID;
		try  {
			ResultSet rs = MainApp.stmt.executeQuery(query);
			while (rs.next()) {
				if (rs.getInt(1) == 1) {
					return true;
				}
			}
		} catch(SQLException se) { se.printStackTrace(); }
		return false;
	}

	public void markAsClosed(String accountID) {
		String query = "UPDATED Accounts SET closed=1 WHERE A.aid=" + accountID;
		try  {
			int rs = MainApp.stmt.executeUpdate(query);
			System.out.println("Marked as closed: " + rs);
		} catch(SQLException se) { se.printStackTrace(); }
	}

	public double getBalance(String accountID) {
		String query = "SELECT balance FROM Accounts A WHERE A.aid=" + accountID;
		try  {
			ResultSet rs = MainApp.stmt.executeQuery(query);
			while (rs.next()) {
				return rs.getDouble(1);
			}
		} catch(SQLException se) { se.printStackTrace(); }
		return -1;
	}

	public void updateTransID() {
		String query = "UPDATE App SET transid=transid+1";
		try  {
			int rs = MainApp.stmt.executeUpdate(query);
			System.out.println("updated transid: " + rs);
		} catch(SQLException se) { se.printStackTrace(); }
	}

	public java.sql.Date getDate() {
		try {
			String query = "SELECT today FROM App";
			ResultSet rs = MainApp.stmt.executeQuery(query);
			while (rs.next()) {
				System.out.println("date: " + rs.getDate("today"));
				return rs.getDate("today");
			}
		} catch(SQLException se) { se.printStackTrace(); }
		return new java.sql.Date((long) 1);
	}

	public int getTransactionID() {
		try  {
			String transQuery = "SELECT transid FROM App";
			ResultSet rs1 = MainApp.stmt.executeQuery(transQuery);
			while (rs1.next()) {
				System.out.println("transactionid: " + rs1.getInt("transid"));
				return rs1.getInt("transid");
			}
			updateTransID();
		} catch(SQLException se) { se.printStackTrace(); }
		return -1;
	}

	public void addTransactionSingle(String type, double amount, String accountID) {
		try {
			String query = "INSERT INTO Makes (toAid, amount, transactionid, when, type) VALUES (" + accountID + ", " + amount + ", " + getTransactionID() + ", TO_DATE('" + getDate() + "', 'yyyy-dd-mm')" + ", '" + type + "')";
			int rs = MainApp.stmt.executeUpdate(query);
			System.out.println("added transaction: " + rs);
			updateTransID();
		} catch(SQLException se) { se.printStackTrace(); }
	}

	public void addTransactionDouble(String type, double amount, String fromAccountID, String toAccountID) {
		try {
			String query = "INSERT INTO Makes (toAid, fromAid, amount, transactionid, when, type) VALUES (" + toAccountID + ", " + fromAccountID + ", " +  amount + ", " + getTransactionID() + ", TO_DATE('" + getDate() + "', 'yyyy-dd-mm')" + ", '" + type + "')";
			int rs = MainApp.stmt.executeUpdate(query);
			System.out.println("added transaction: " + rs);
			updateTransID();
		} catch(SQLException se) { se.printStackTrace(); }
	}
}