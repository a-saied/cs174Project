import java.sql.*;
import java.util.*;

public class ATMFunctions {

	public ATMFunctions() {}

	//DONE
	public void deposit(double amount, String accountID) {
		if (checkAccountAccess(accountID)) {
			if(getAccountType(accountID).equals("Pocket")) {
				System.out.println("You cannot deposit into a pocket account.");
			}
			else {
				String updateQuery = "UPDATE Accounts SET balance = balance+" + amount + " WHERE aid=" + accountID;
				try {
					int rs = MainApp.stmt.executeUpdate(updateQuery);
					System.out.println("Updated: " + rs);
				} catch(SQLException se) { se.printStackTrace(); }
			}
		}
		else {
			System.out.println("The account does not exist or you do not have access to this account.");
		}
	}

	//DONE
	//Move money from linked acc to pocket
	public void topUp(double amount, String pocketAccountID) {
		if (checkAccountAccess(pocketAccountID)) {
			if (getAccountType(pocketAccountID).equals("Pocket")) {
				String linkedAccountID = Integer.toString(getLinkedAccountID(pocketAccountID));
				if (hasEnoughMoney(amount, linkedAccountID)) {
					String addQuery = "UPDATE Accounts A SET A.balance = A.balance+" + amount + " WHERE A.aid=" + pocketAccountID;
					String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=(SELECT associatedID FROM Pocket P WHERE aid=" + pocketAccountID + ")";
					try {
						int rs1 = MainApp.stmt.executeUpdate(addQuery);
						System.out.println("Updated1: " + rs1);
					} catch(SQLException se) { se.printStackTrace(); }

					try {
						int rs2 = MainApp.stmt.executeUpdate(subQuery);
						System.out.println("Updated2: " + rs2);
					} catch(SQLException se)  { se.printStackTrace(); }
				}
				else {
					System.out.println("You do not have enough money to top-up.");
				}
			}
			else {
				System.out.println("You cannot top-up from a checking or savings account.");
			}
		}
		else {
			System.out.println("The account does not exist or you do not have access to this account.");
		}
	}

	//DONE
	public void withdraw(double amount, String accountID) {
		if (checkAccountAccess(accountID)) {
			System.out.println("Account Type: " + getAccountType(accountID) + ".");
			if (getAccountType(accountID).equals("Pocket")){
				System.out.println("Cannot withdraw from a pocket account.");
			}
			else {
				if (hasEnoughMoney(amount, accountID)) {
					String query = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + accountID;
					try {
						int rs = MainApp.stmt.executeUpdate(query);
						System.out.println("Updated: " + rs);
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					System.out.println("You do not have enough money to withdraw.");
				}
			}
		}
		else {
			System.out.println("The account does not exist or you do not have access to this account.");
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
						System.out.println("Udpate: " + rs);
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					System.out.println("You do not have enough money to purchase.");
				}
			}
			else {
				System.out.println("You cannot purchase from a checking or savings account.");
			}
		}
		else {
			System.out.println("The account does not exist or you do not have access to this account.");
		}
	}

	public void transfer(double amount, String fromAccountID, String toAccountID) {
		if (checkAccountAccess(fromAccountID)) {
			if (getAccountType(fromAccountID).equals("Pocket") || getAccountType(toAccountID).equals("Pocket")){
				System.out.println("You cannot transfer to/from a pocket account.");
				return;
			}
			if (amount > 2000) {
				System.out.println("You cannot transfer > $2000.");
				return;
			}
			String query = "SELECT COUNT (taxID) FROM Owned_By O, Accounts A1 WHERE A1.aid=" + fromAccountID + " AND O.aid=A1.aid AND O.taxID IN (SELECT taxID FROM Accounts A2 WHERE A2.aid=" + toAccountID+ ")";
			try {
				ResultSet rs = MainApp.stmt.executeQuery(query);
				while (rs.next()) {
					System.out.println("Num of taxIDs: " + rs.getInt(1));
					if (rs.getInt(1) < 1) {
						System.out.println("The account does not exist or you do not have access to this account.");
						return;
					}
				}
				if (hasEnoughMoney(amount, fromAccountID)) {
					//subtract from account
					String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + fromAccountID;
					try {
						int rs1 = MainApp.stmt.executeUpdate(subQuery);
						System.out.println("Updated1: " + rs1);
					} catch(SQLException se) { se.printStackTrace(); }

					//add to account
					String addQuery = "UPDATE Accounts A SET A.balance = A.balance+" + amount + " WHERE A.aid=" + toAccountID;
					try {
						int rs2 = MainApp.stmt.executeUpdate(addQuery);
						System.out.println("Updated2: " + rs2);
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					System.out.println("You do not have enough funds to transfer.");
				}

			} catch(SQLException se) { se.printStackTrace(); }
		}
		else {
			System.out.println("The account does not exist or you do not have access to this account.");
		}
	}

	public void collect(double amount, String pocketAccountID) {
		if (checkAccountAccess(pocketAccountID)) {
			if (getAccountType(pocketAccountID).equals("Pocket")){
				if (hasEnoughMoney(amount, pocketAccountID)) {
					//subtract from pocket account
					String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + pocketAccountID;
					try {
						int rs1 = MainApp.stmt.executeUpdate(subQuery);
						System.out.println("Updated1: " + rs1);
					} catch(SQLException se) { se.printStackTrace(); }

					//add to linked account
					String addQuery = "UPDATE Accounts A SET A.balance = A.balance+" + (amount - (amount*.03)) + "WHERE A.aid = (SELECT associatedID FROM Pocket P WHERE aid=" + pocketAccountID + ")";
					try {
						int rs2 = MainApp.stmt.executeUpdate(addQuery);
						System.out.println("Updated2: " + rs2);
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					System.out.println("You do not have enough funds to collect.");
				}
			}
			else {
				System.out.println("You cannot collect from a checking or savings account.");
			}
		}
		else {
			System.out.println("The account does not exist or you do not have access to this account.");
		}
	}

	//DONE
	public void wire(double amount, String fromAccountID, String toAccountID) {
		if (checkAccountAccess(fromAccountID) && checkAccountAccess(toAccountID)) {
			if (!getAccountType(fromAccountID).equals("Pocket") && !getAccountType(toAccountID).equals("Pocket")) {
				//subtract from pocket account
				if (hasEnoughMoney(amount, fromAccountID)) {
					String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + fromAccountID;
					try {
						int rs1 = MainApp.stmt.executeUpdate(subQuery);
						System.out.println("Updated1: " + rs1);
					} catch(SQLException se) { se.printStackTrace(); }

					//add to other account
					String addQuery = "UPDATE Accounts A SET A.balance = A.balance+" + (amount - (amount*.02)) + "WHERE A.aid=" + toAccountID;
					try {
						int rs2 = MainApp.stmt.executeUpdate(addQuery);
						System.out.println("Updated2: " + rs2);
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					System.out.println("You do not have enough money to wire.");
				}
			}
			else {
				System.out.println("You cannot wire to/from a pocket account.");
			}
		}
		else {
			System.out.println("The account does not exist or you do not have access to this account.");
		}
	}

	//DONE
	public void payFriend(double amount, String fromPocketID, String toPocketID) {
		if (checkAccountAccess(fromPocketID)) {
			String checkQuery = "SELECT COUNT(aid) FROM Pocket P WHERE P.aid=" + toPocketID;
			try {
				ResultSet rs1 = MainApp.stmt.executeQuery(checkQuery);
				while (rs1.next()) {
					if (rs1.getInt(1) < 1) {
						System.out.println("The friend pocket ID does not exist.");
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
					} catch(SQLException se) { se.printStackTrace(); }

					//subtract from my pocket
					String subQuery = "UPDATE Accounts A SET A.balance = A.balance-" + amount + " WHERE A.aid=" + fromPocketID;
					try {
						int rs3 = MainApp.stmt.executeUpdate(subQuery);
						System.out.println("Udpate: " + rs3);
					} catch(SQLException se) { se.printStackTrace(); }
				}
				else {
					System.out.println("You do not have enough funds to pay.");
				}
			}
			else {
				System.out.println("You cannot pay to/from a checking or savings account.");
			}
		}
		else {
			System.out.println("The account does not exist or you do not have access to this account.");
		}
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Checks if the user is an owner of the account
	public boolean checkAccountAccess(String accountID) {
		String query = "SELECT COUNT(taxid) FROM Owned_By OB WHERE OB.aid=" + accountID + " AND OB.taxID=(SELECT taxID FROM Customers C WHERE C.PIN=0000)";//MainApp.atmPIN + ")";
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
}