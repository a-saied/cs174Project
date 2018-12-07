import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.sql.*;

public class BankTellerPanel extends JPanel {
	static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
   	static final String DB_URL = "jdbc:oracle:thin:@cloud-34-133.eci.ucsb.edu:1521:XE";
   	static final String USERNAME = "asaied";
	static final String PASSWORD = "cs174";
	static Connection conn = null;
    static Statement stmt = null;

	public BankTellerPanel() {
		this.setLayout( new GridLayout(0,1, 0, 10));
		JLabel l3 = new JLabel("Welcome, Bank Teller! What would you like to do?\n");
		l3.setHorizontalAlignment(JLabel.CENTER);
		JButton c1 = new JButton("Enter Check Transaction");
		JButton c2 = new JButton("Generate Monthly Statement");
		JButton c3 = new JButton("List Closed Accounts");
		JButton c4 = new JButton("Generate DTER");
		JButton c5 = new JButton("Customer Report");
		JButton c6 = new JButton("Add Interest");
		JButton c7 = new JButton("Create Account");
		JButton c8 = new JButton("Delete Closed Accts & Customers");
		JButton c9 = new JButton("Delete Transactions");
		JButton c0 = new JButton("Back");
		c0.addActionListener(e -> {
			MainApp.window.setContentPane(MainApp.scene);
			MainApp.window.invalidate();
			MainApp.window.validate();
		});

		this.add(l3);
		this.add(c1);
		this.add(c2);
		this.add(c3);
		this.add(c4);
		this.add(c5);
		this.add(c6);
		this.add(c7);
		this.add(c8);
		this.add(c9);
		this.add(c0);

		c1.addActionListener(e -> {
			checkTransaction();
		});
		c2.addActionListener(e -> {
			monthlyStatement();
		});
		c3.addActionListener(e -> {
			closedAccounts();
		});
		c4.addActionListener(e -> {
			getDTER();
		});
		c5.addActionListener(e -> {
			customerReport();
		});
		c6.addActionListener(e -> {
			addInterest();
		});
		c7.addActionListener(e -> {
			createAcct();
		});
		c8.addActionListener(e -> {
			deleteClosed();
		});
		c9.addActionListener(e -> {
			deleteTransactions(); 
		});
	}

	// FINISHED
	// DONE
	private static void checkTransaction(){
		String q = "SELECT A.today, A.transID FROM App A";
		int p = 0;
		java.sql.Date d = new java.sql.Date((long) 1);
		try{
			//MainApp.stmt = MainApp.conn.createStatement();
			ResultSet rs = MainApp.stmt.executeQuery(q);
			if(rs.next()){
				d = rs.getDate(1);
				p = rs.getInt("transID");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		
		System.out.println(d);
		
		System.out.println(p);
		String aid = JOptionPane.showInputDialog("Enter AID");
		String amt = null;
		if(aid != null){
			amt = JOptionPane.showInputDialog("Enter Amount");
		}

		if(aid != null && amt != null){
			Double am = Double.parseDouble(amt);
			ArrayList<String>  x = getData("Select A.balance, A.closed from Accounts A where A.type <> 'Pocket' and A.type <> 'Savings' and A.aid = " + aid);
			if(x.size() > 0){
				if(Integer.parseInt(x.get(1)) == 0){
					if(Double.parseDouble(x.get(0)) + am > 0.0){
						String let = "";
						System.out.println("progresso");
						if(Double.parseDouble(x.get(0)) + am <= 0.01){
							let = "UPDATE Accounts SET closed = 1, balance = balance + " + am + " WHERE aid = " + aid + "";
						}
						else{
							let = "UPDATE Accounts SET balance = balance + " + am + " WHERE aid = " + aid + "";
						}
						// String let = "UPDATE Accounts SET balance = balance + " + am + " WHERE aid = " + aid + "";
						simpleExec(let);
						// try{
						// 	boolean v = MainApp.stmt.execute(x);
						// }catch(SQLException se){
						// 	se.printStackTrace();
						// 	System.out.println("fail");
						// 	System.exit(0);
						// }
						System.out.println("progress");
						// aid = "100000";
						// p = "100.00";
						// String v = "INSERT INTO Accounts values(1, 120, 0, 'Savings', 0, 0, 1234)";
						// simpleExec(v);
						String z = "INSERT INTO Makes(toaid, fromaid, when, amount, transactionid, type) values( " + aid + ", null, TO_DATE('" + d + "', 'YYYY-MM-DD'), " + am + ", " + p + ", \'Check-Transaction\')";
						simpleExec(z);
						JOptionPane.showMessageDialog(null, "Transaction completed");
						String u = "UPDATE App SET transID = " + p + " + 1";
						simpleExec(u);

					}
				}
			}
		}else {
			JOptionPane.showMessageDialog(null, "Check Transaction Cancelled");
		}
		// String u = "UPDATE App SET transID = " + p + " + 1";
		// simpleExec(u);
	    
	}

	//FINISHED
	//DONE
	private static void monthlyStatement(){
	    String masterStatement = "";
		String customerid = JOptionPane.showInputDialog("Enter Customer TaxID");
		if(customerid != null){
			customerid = customerid.trim();
			masterStatement += "Customer ID: " + customerid + "\n";
			String cust_accts_query = "SELECT O.aid from Owned_By O where O.taxID = " + customerid;
			ArrayList<String> customer_accts_aids = getData(cust_accts_query);
			System.out.println("got accounts");
			String transaction_query = null;
			String transaction_query2 = null;
			String transaction_query3 = null;
			ArrayList<String> trans = null;
			ArrayList<String> trans2 = null;
			ArrayList<String> custos = null;
			String cust_query = null;

			for(int i=0; i < customer_accts_aids.size(); i++){
				masterStatement += "Account aid: " + customer_accts_aids.get(i) + "\n";
				cust_query = "SELECT C.name, C.address FROM Customers C, Owned_By O WHERE C.taxID = O.taxID and O.aid = " + customer_accts_aids.get(i);
				custos =  getData(cust_query);
				System.out.println("got custos");
				for(int k = 0; k < custos.size(); k++){
					masterStatement += custos.get(k);
					if(k% 2 == 1) {
						masterStatement += "\n";
					}
					else{
						masterStatement += ", ";
					}
				}

				masterStatement += "\n Transactions list \n";
				transaction_query = "SELECT M.toaid, M.fromaid, M.when, M.amount FROM Makes M where M.toaid = " + customer_accts_aids.get(i) + " or M.fromaid = " + customer_accts_aids.get(i);
				trans = getData(transaction_query);
				System.out.println("got transactions");
				for(int j = 0; j < trans.size(); j++){
					masterStatement += trans.get(j);
					if(j % 5 == 4){
						masterStatement += "\n";
					}else { 
						masterStatement += ", "; 
					}
				}


				transaction_query2 = "SELECT A.balance FROM Accounts A WHERE A.aid = " + customer_accts_aids.get(i);
				transaction_query3 = "SELECT SUM(M.amount) FROM Makes M WHERE M.toaid = " + customer_accts_aids.get(i);
				String q3 = "SELECT SUM(M.amount) FROM Makes M WHERE M.fromaid = " + customer_accts_aids.get(i);
				double z = 0;
				double w = 0;
				double w2 = 0;
				boolean changed1 = false;
				boolean changed2 = false;
				try{
					ResultSet rs = MainApp.stmt.executeQuery(transaction_query2);
					if(rs.next()){
						z = rs.getDouble(1);
						changed1 = true;
					}
				}catch(SQLException e){
					e.printStackTrace();
				}

				try{
					ResultSet rs = MainApp.stmt.executeQuery(transaction_query3);
					if(rs.next()){
						w = rs.getDouble(1);
						changed2 = true;
					}
				}catch(SQLException e){
					e.printStackTrace();
				}
				try{
					ResultSet rs = MainApp.stmt.executeQuery(q3);
					if(rs.next()){
						w2 = rs.getDouble(1);
						changed2 = true;
					}
				}catch(SQLException e){
					e.printStackTrace();
				}
				// System.out.println(trans.get(0));
				// System.out.println(trans.get(1));
				if(changed1 && changed2){ 
					double init = z - w;
					masterStatement += "Initial Balance: " + init + "\n";
					masterStatement += "Final Balance: " + z + "\n";
				}
				System.out.println(masterStatement);
				//get initial and final(current) amt 
			}

			String primary = "SELECT A.balance from Accounts A where A.owner = " + customerid;
			double lump_sum = 0.0;
			ArrayList<String> values = getData(primary);
			for(int a = 0; a < values.size(); a++){
				lump_sum += Double.parseDouble(values.get(a));
			}
			if(lump_sum > 100000.00){
				JOptionPane.showMessageDialog(null, "WARNING: Insurance limit has been reached");
			}
		}
	    
	}

	//FINISHED
	//DONE 
	private static void closedAccounts(){
		String master = ""; 
		String q = "SELECT A.aid, A.balance, A.type FROM Accounts A WHERE A.closed = 1";
		ArrayList<String> x = getData(q); /// fix get data method 
		if(x.size() > 0){ 
			for(int i = 0; i < x.size(); i++){
				master += x.get(i);
				if(i % 3 == 2){
					master += "\n";
				}
				else {
					master += ", ";
				}
			}
			JOptionPane.showMessageDialog(null, master);
		}
		else{
			JOptionPane.showMessageDialog(null, "No accounts found");
		}
		
					
	}


	//CAN'T CHECK
	//DONE 
	private static void getDTER(){
		String master = "";
		// and (M.type = \'Deposit\' or M.type = \'Transfer\' or M.type = \'Wire\')
		String q = "SELECT C.taxID, C.name, C.address from Customers C where C.taxID in (SELECT O.taxID from Owned_By O, Makes M where (M.toaid = O.aid) and (M.type =\'Deposit\' or M.type = \'Wire\' or M.type =\'Transfer\') group by O.taxID having SUM(M.amount) > 10000)";
		ArrayList<String> x = getData(q);
		if(x.size() > 0){
			for(int i = 0; i < x.size(); i++){
				if(i%3 == 0){
					master += "\n";
				}else {
					master += "   ";
				}
				master += x.get(i);
			}
			JOptionPane.showMessageDialog(null, master);
		}
		else{
			JOptionPane.showMessageDialog(null, "No customers found");
		}

	}

	//FINISHED
	//DONE
	private static void customerReport(){
		String master = "";
		String cid = JOptionPane.showInputDialog("Enter Customer PIN");
		int pin = Integer.parseInt(cid);
		pin = pin * 2;
		if(cid != null){
			//normal query shit done here 
			String q = "SELECT A.aid, A.closed FROM Accounts A, Owned_By O, Customers C WHERE O.aid = A.aid and O.taxID = C.taxID and C.PIN = '" + pin + "'"; 
			ArrayList<String> accts = getData(q);
			if(accts.size() == 0){
				JOptionPane.showMessageDialog(null, "Customer Not Found, Exiting...");
			}
			else{
				master += "AID          CLOSED?(1 = YES, 0 = NO)\n";
				for(int i = 0; i < accts.size(); i++){
					master += accts.get(i);
					if(i % 2 == 1){
						master += "\n";
					}else{
						master += ",              ";
					}

				}
				JOptionPane.showMessageDialog(null, master);
			}
		}else {
			JOptionPane.showMessageDialog(null, "Customer Report Cancelled");
		}
		
	}

	//FINISHED
	//DONE
	private static void addInterest(){
		//get interest rates 
		String q = "SELECT A.student, A.interest, A.savings, A.pocket, A.transID from App A";
		ArrayList<String> interests = getData(q);
		java.sql.Date d = new java.sql.Date((long) 1);
		try{
			//MainApp.stmt = MainApp.conn.createStatement();
			ResultSet rs = MainApp.stmt.executeQuery("SELECT A.today from App A");
			if(rs.next()){
				d = rs.getDate(1);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}

		ArrayList<String> sChecking = getData("SELECT A.aid, A.avgBalance from Accounts A where A.type = \'Student-Checking\' and A.closed = 0 and A.interestAdded = 0");
		ArrayList<String> iChecking = getData("SELECT A.aid, A.avgBalance from Accounts A where A.type = \'Interest-Checking\' and A.closed = 0 and A.interestAdded = 0");
		ArrayList<String> sav = getData("SELECT A.aid, A.avgBalance from Accounts A where A.type = \'Savings\' and A.closed = 0 and A.interestAdded = 0");
		ArrayList<String> pock = getData("SELECT A.aid, A.avgBalance from Accounts A where A.type = \'Pocket\' and A.closed = 0 and A.interestAdded = 0");

		System.out.println("got entries");

		for(int i = 0; i < sChecking.size(); i = i + 2){
			double fill = (Double.parseDouble(sChecking.get(i + 1)) * Double.parseDouble(interests.get(0)));
			String quer = "INSERT INTO Makes(toaid, fromaid, when, amount, transactionid, type) values(" + sChecking.get(i) + ", " + sChecking.get(i) + ", " + "TO_DATE('" + d + "', 'YYYY-MM-DD'), " + fill + ", " + interests.get(4) + ", \'Accrue-Interest\')";
			simpleExec(quer);
			simpleExec("UPDATE App SET transID = " + interests.get(4) + " + 1");
		}
		for(int i = 0; i < iChecking.size(); i = i + 2){
			String quer = "INSERT INTO MAKES (toaid, fromaid, when, amount, transactionid, type) values(" + iChecking.get(i) + ", " + iChecking.get(i) + ", TO_DATE('" + d + "', 'YYYY-MM-DD'), " + (Double.parseDouble(iChecking.get(i + 1)) * Double.parseDouble(interests.get(1))) + ", " + interests.get(4) + ", \'Accrue-Interest\')";
			simpleExec(quer);
			simpleExec("UPDATE App SET transID = " + interests.get(4) + " + 1");
		}
		for(int i = 0; i < sav.size(); i = i + 2){
			String quer = "INSERT INTO MAKES (toaid, fromaid, when, amount, transactionid, type) values(" + sav.get(i) + ", " + sav.get(i) + ", TO_DATE('" + d + "', 'YYYY-MM-DD'), " + (Double.parseDouble(sav.get(i + 1)) * Double.parseDouble(interests.get(2))) + ", " + interests.get(4) + ", \'Accrue-Interest\')";
			simpleExec(quer);
			simpleExec("UPDATE App SET transID = " + interests.get(4) + " + 1");
		}
		for(int i = 0; i < pock.size(); i = i + 2){
			String quer = "INSERT INTO MAKES (toaid, fromaid, when, amount, transactionid, type) values(" + pock.get(i) + ", " + pock.get(i) + ", TO_DATE('" + d + "', 'YYYY-MM-DD'), " + (Double.parseDouble(pock.get(i + 1)) * Double.parseDouble(interests.get(3))) + ", " + interests.get(4) + ", \'Accrue-Interest\')";
			simpleExec(quer);
			simpleExec("UPDATE App SET transID = " + interests.get(4) + " + 1");
		}

		System.out.println(interests.get(4));

		if(interests.size() > 0){
			// // update student checking 
			// * " + interests.get(0) + ",
			String q2 = "UPDATE Accounts SET balance = balance + avgBalance * " + interests.get(0) + ", interestAdded = 1 WHERE closed = 0 and interestAdded = 0 and type = \'Student-Checking\'";
			try{
				boolean r = MainApp.stmt.execute(q2);
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			String q3 = "UPDATE Accounts SET balance = balance + avgBalance * " + interests.get(1) + ", interestAdded = 1 WHERE closed = 0 and interestAdded = 0 and type = \'Interest-Checking\'";
			try{
				boolean r = MainApp.stmt.execute(q3);
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			String q4 = "UPDATE Accounts SET balance = balance + avgBalance * " + interests.get(2) + ", interestAdded = 1 WHERE closed = 0 and interestAdded = 0 and type = \'Savings\'";
			try{
				boolean r = MainApp.stmt.execute(q4);
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			String q5 = "UPDATE Accounts SET balance = balance + avgBalance * " + interests.get(3) + ", interestAdded = 1 WHERE closed = 0 and interestAdded = 0 and type = \'Pocket\'";
			try{
				boolean r = MainApp.stmt.execute(q5);
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			
		}

		System.out.println("interests added");



	}

	// HALFWAY FINISHED AND DONE
	private static void createAcct(){
		String branch = JOptionPane.showInputDialog("What bank branch is this for?");
		String balance = JOptionPane.showInputDialog("What is the initial balance of this account \n Pocket accounts must have an initial value greater than $5. \n(please use decimals if not full dollar amount e.g. 10.99)");
		String typ = JOptionPane.showInputDialog("What type of account is this?\n Type 1 for Student Checking\n Type 2 for Interest Checking\n Type 3 for Savings\n Type 4 for Pocket");
		String aid = JOptionPane.showInputDialog("What is the account id?");
		ArrayList<String> nextAID = new ArrayList<String>();
		nextAID.add(aid.trim());
		// ArrayList<String> nextAID = getData("Select A.nextAID from App A where A.ID = 1");
		String type = typ;
		JOptionPane.showMessageDialog(null, "AID is " + nextAID.get(0));
		if(type != null && branch != null){
			type = type.trim();
			System.out.println(type);
			if(Integer.parseInt(type) != 4 && balance != null){
				balance = balance.trim();
				//type = type.trim();
				String cnt = JOptionPane.showInputDialog("How many owners are on this account?");
				if(cnt != null){
					JOptionPane.showMessageDialog(null, "Thank you now please enter the information on these customers. \n Start with the primary owner.");
					System.out.println(type);
					int numtype = Integer.parseInt(type);
					switch(numtype){
						case 1 : 
							type = "Student-Checking";
							break;
						case 2 :
							type = "Interest-Checking";
							break;
						case 3 :
							type = "Savings";
							break;
					}
					cnt = cnt.trim();
					int count = Integer.parseInt(cnt);
					System.out.println("count: " + count);
					for(int i = 0; i < count; i++){
						String[] x = inputCustomerData(nextAID.get(0));
						if(i == 0 && x[0] != ""){
							simpleExec("INSERT INTO Accounts (aid, balance, closed, type, interestAdded, avgBalance, owner, branch) Values(" + nextAID.get(0) + ", " +
								balance + ", 0, '" + type +"', 0, " + balance + ", '" + x[1] + "', '" + branch + "')");
							simpleExec("update App set nextaid = nextaid + 1"); 
							System.out.println("Account created");
							String q = "SELECT A.today, A.transID FROM App A";
							int p = 0;
							java.sql.Date d = new java.sql.Date((long) 1);
							try{
								//MainApp.stmt = MainApp.conn.createStatement();
								ResultSet rs = MainApp.stmt.executeQuery(q);
								if(rs.next()){
									d = rs.getDate(1);
									p = rs.getInt("transID");
								}
							}catch (SQLException e){
								e.printStackTrace();
							}

							simpleExec("INSERT INTO Makes (toaid, fromaid, when, amount, transactionid, type) values(" + nextAID.get(0) + ", null, " + "TO_DATE('" + d + "', 'YYYY-MM-DD'), " + balance + ", " + p + ", \'Deposit\')");
							String u = "UPDATE App SET transID = " + p + " + 1";
							simpleExec(u);
						}
						if(x[0] != ""){
							simpleExec("INSERT INTO Owned_By (aid, taxID) Values(" + nextAID.get(0) + ", " + x[1] + ")");
							System.out.println("Owned_By entry created");
						}
						if(x[0] == ""){
							i--;
						}			
					}
				}
			}



			//if we're dealing with a pocket account so we have to find the linked bank account 
			else if(Integer.parseInt(type) == 4 && balance != null){
				ArrayList<String> pins = new ArrayList<String>(); /// actually stores tax ids
				String cnt = JOptionPane.showInputDialog("How many owners are on this pocket account?");
				if(cnt != null){
					JOptionPane.showMessageDialog(null, "Thank you now please enter the information on these customers. \n Start with the primary owner.");
				
					cnt = cnt.trim();
					int count = Integer.parseInt(cnt);
					for(int i = 0; i < count; i++){
						String[] x = inputCustomerData(nextAID.get(0));
						pins.add(x[1]);
						if(i == 0 && x[0] != ""){
							simpleExec("INSERT INTO Accounts (aid, balance, closed, type, interestAdded, avgBalance, owner) Values(" + nextAID.get(0) + ", " +
								(Double.parseDouble(balance) - 5) + ", 0, \'Pocket\', 0, " + (Double.parseDouble(balance) - 5) + ", " + x[1] + ")");
							simpleExec("update App set nextaid = nextaid + 1");
							System.out.println("Account created");
						}
						if(x[0] != ""){
							simpleExec("INSERT INTO Owned_By (aid, taxID) Values(" + nextAID.get(0) + ", " + x[1] + ")");
						}
						if(x[0] == ""){
							i--;
						}		
					}	
				}
				String assoc = JOptionPane.showInputDialog("Type aid for linked account");
				ArrayList<String> sol = getData("SELECT O.taxID from Owned_By O where O.aid = " + assoc);
				boolean flag = false;
				boolean flag2 = false;
				for(int v = 0; v < sol.size(); v++){
					for(int z = 0; z < pins.size(); z++){
						if(sol.get(v).equals(pins.get(z))) flag = true;
					}
				}
				ArrayList<String> assBalance = getData("SELECT A.balance from Accounts A where A.aid =" + assoc);
				if(assBalance.size() > 0){
					if(flag && Double.parseDouble(assBalance.get(0)) > Double.parseDouble(balance)){
						simpleExec("INSERT INTO Pocket (aid, associatedid, first) values(" + nextAID.get(0) + ", " + assoc + ", 1)");
						simpleExec("UPDATE Accounts set balance = balance - " + balance + " where aid = " + assoc);
						String q = "SELECT A.today, A.transID FROM App A";
						int p = 0;
						java.sql.Date d = new java.sql.Date((long) 1);
						try{
							//MainApp.stmt = MainApp.conn.createStatement();
							ResultSet rs = MainApp.stmt.executeQuery(q);
							if(rs.next()){
								d = rs.getDate(1);
								p = rs.getInt("transID");
							}
						}catch (SQLException e){
							e.printStackTrace();
						}
						simpleExec("INSERT INTO Makes (toaid, fromaid, when, amount, transactionid, type) values(" + nextAID.get(0) + ", " + assoc + ", " + "TO_DATE('" + d + "', 'YYYY-MM-DD'), " + (Double.parseDouble(balance) - 5) + ", " + p + ", \'Top-Up\')");
						String u = "UPDATE App SET transID = " + p + " + 1";
						simpleExec(u);
						System.out.println("Owned_By and Makes entry created");

					}
					else {
						JOptionPane.showMessageDialog(null, "Invalid linked account ID or not enough money in linked acct to create");
						simpleExec("DELETE FROM Accounts A WHERE A.aid = " + nextAID.get(0));
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid linked account ID or not enough money in linked acct to create");
					simpleExec("DELETE FROM Accounts A WHERE A.aid = " + nextAID.get(0));
				}

			}else{
				JOptionPane.showMessageDialog(null, "Invalid entry");
			}
		}
	}

	// FINISHED
	// DONE
	private static void deleteClosed(){
		String q = "DELETE FROM Accounts A WHERE A.closed = 1";
		try{
			boolean res = MainApp.stmt.execute(q);

			System.out.println("Accounts closed");
		}catch(SQLException se){
			se.printStackTrace();
		}

		//String q2 = "DELETE FROM Customers C WHERE C.taxID NOT IN " + "(SELECT C2.taxID FROM Customers C2 WHERE NOT EXISTS " + "( SELECT C3.taxID FROM Customers C3 WHERE NOT EXISTS (SELECT O.taxID FROM Owned_By O WHERE O.taxID = C3.taxID and O.taxID = C2.taxID)))";
		String q3 = "DELETE FROM Customers C WHERE C.taxID NOT IN (SELECT O.taxID from Owned_By O)";
		try{
			boolean res = MainApp.stmt.execute(q3);

			System.out.println("Subsequent customers closed");
		}catch(SQLException se){
			se.printStackTrace();
		}

	}

	// FINISHED 
	// DONE
	private static void deleteTransactions(){
		//pretty simple, just clear all entries in the Makes table and all transactions are removed :)
		String q = "DELETE FROM Makes";
		try{
			boolean res = MainApp.stmt.execute(q);
			//System.out.println("Transactions deleted");

			JOptionPane.showMessageDialog(null, "Transactions deleted.");
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		// getData(q);
	}


	private static String[] inputCustomerData(String aid){
		String pin = JOptionPane.showInputDialog("Enter customer PIN.\n If the PIN doesn't exist we will create a customer with that PIN. \nPlease note that if you are creating a new customer DO NOT \nuse PIN 0000 for security reasons");
		pin = pin.trim();
		int pin_num = Integer.parseInt(pin);
		pin_num = pin_num * 2;
		ArrayList<String> val = getData("SELECT COUNT(*) FROM Customers C WHERE C.PIN = '" + pin_num + "'");
		if(Integer.parseInt(val.get(0)) <  1 || pin.length() != 4){
			System.out.println("NEW");
			String name = JOptionPane.showInputDialog("NEW CUSTOMER! Please type the Customer's name:");
			if(name != null){
				String addy = JOptionPane.showInputDialog("Enter address: ");
				if(addy != null){
					String taxId = JOptionPane.showInputDialog("Enter taxID: ");
					if(taxId != null){
						simpleExec("INSERT INTO Customers (taxID, name, address, PIN) values(" + taxId + ", '" + name + "', '" + addy + "', '" + pin_num + "')");
						String[] ret = {pin, taxId};
						return ret;
					}
				}
			}
		}
		else if(pin.length() == 4 && Integer.parseInt(val.get(0)) >= 1){
			System.out.println("OLD");
			String tazID = "";
			try{
				String z = "SELECT C.pin, C.taxID from Customers C where C.pin = '" + pin_num + "'";
				ResultSet rs = MainApp.stmt.executeQuery(z);
				if(rs.next()){
					pin = rs.getString("PIN");
					tazID = rs.getString("taxID");
					System.out.println("got tazid");
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			String [] ret2 = {pin, tazID};
			return ret2;
		}
		String[] fin = {"", ""};
		return fin;

	}
 
	///useless lol///
	private static ArrayList<String> getData(String query){ /// everything will come out a String
		// Connection conn = null;
  //     	Statement stmt = null;
      	ArrayList<String> result = new ArrayList<String>();
      	try{
	         //STEP 2: Register JDBC driver
	         // Class.forName(JDBC_DRIVER);

	         // //STEP 3: Open a connection
	         // //System.out.println("Connecting to a selected database...");
	         // conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
	         // // System.out.println("Connected database successfully...");
	         
	         // //STEP 4: Execute a query
	         // // System.out.println("Creating statement...");
	         // stmt = conn.createStatement();

	         //String sql = "SELECT cid, cname, city, discount FROM cs174.Customers";
      		MainApp.stmt = MainApp.conn.createStatement();
      		System.out.println("this far");
	         ResultSet rs = MainApp.stmt.executeQuery(query);
	         System.out.println("this far");
	         ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
	         int cols = rsmd.getColumnCount();
	         //STEP 5: Extract data from result set
	         while(rs.next()){
	            //Retrieve by column name
	            // String cid  = rs.getString("cid");
	            // String cname = rs.getString("cname");
	            // String city = rs.getString("city");
	            // double discount = rs.getDouble("discount");

	         	for(int i = 1; i < cols + 1; i++){
	         		result.add(rs.getString(i));
	         	}
	            //Display values
	            // System.out.print("cid: " + cid);
	            // System.out.print(", cname: " + cname);
	            // System.out.print(", city: " + city);
	            // System.out.println(", discount: " + discount);
	         }
	         rs.close();
	    }catch(SQLException se){
	         //Handle errors for JDBC
	         se.printStackTrace();
	    }catch(Exception e){
	         //Handle errors for Class.forName
	         e.printStackTrace();
	    }finally{
	         //finally block used to close resources
	         try{
	            if(stmt!=null)
	               conn.close();
	         }catch(SQLException se){
	         }// do nothing
	         try{
	            if(conn!=null)
	               conn.close();
	         }catch(SQLException se){
	            se.printStackTrace();
	         }//end finally try
	    }//end try
	    // System.out.println("Query complete");
	    return result;
	} 


	private static void simpleExec(String x){
		try{
			int res = MainApp.stmt.executeUpdate(x);
			//System.out.println("Transactions deleted");

		}
		catch(SQLIntegrityConstraintViolationException s){
			s.printStackTrace();
			try{
	            if(MainApp.stmt!=null)
	               MainApp.conn.close();
	         }catch(SQLException se){
	         }// do nothing
	         try{
	            if(MainApp.conn!=null)
	               MainApp.conn.close();
	         }catch(SQLException se){
	            se.printStackTrace();
	         }//end finally try
			System.exit(0);
		}
		catch(SQLException s){
			s.printStackTrace();
			try{
	            if(MainApp.stmt!=null)
	               MainApp.conn.close();
	         }catch(SQLException se){
	         }// do nothing
	         try{
	            if(MainApp.conn!=null)
	               MainApp.conn.close();
	         }catch(SQLException se){
	            se.printStackTrace();
	         }//end finally try
			System.exit(0);

		}
		// finally{
		// 	try{
	 //            if(MainApp.stmt!=null)
	 //               MainApp.conn.close();
	 //         }catch(SQLException se){
	 //         }// do nothing
	 //         try{
	 //            if(MainApp.conn!=null)
	 //               MainApp.conn.close();
	 //         }catch(SQLException se){
	 //            se.printStackTrace();
	 //         }//end finally try
		// }
		// // getData(q);
	}





}