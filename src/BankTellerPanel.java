import javax.swing.*;
import java.awt.*;

public class BankTellerPanel extends JPanel {
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
			//customerReport();
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

	private static void checkTransaction(){
	    /*	// String[] x = {"cid", "cname", "city", "discount"};
		// getData("SELECT cid, cname, city, discount FROM cs174.Customers", x);
		String q = "SELECT * FROM Makes M WHERE M.aid = ";
		String aid = JOptionPane.showInputDialog("Enter AID");
		String amt = null;
		if(aid != null){
			amt = JOptionPane.showInputDialog("Enter Amount");
		}

		if(aid != null && amt != null){
			//normal query shit done here 
		}else {
		JOptionPane.showMessageDialog(null, "Check Transaction Cancelled");
		}
	    */


	}
	private static void monthlyStatement(){
	    /*	String masterStatement = "";
		String customerid = JOptionPane.showInputDialog("Enter Customer TaxID");
		if(customerid != null){
			customerid = customerid.trim();
			masterStatement += "Customer ID: " + customerid + "\n";
			String cust_accts_query = "SELECT O.aid from Owned_By O where O.taxID = " + customerid + ";";
			ArrayList<String> customer_accts_aids = getData(cust_accts_query);
			String transaction_query = null;
			ArrayList<String> trans = null;
			ArrayList<String> custos = null;

			for(int i=0; i < customer_accts_aids.size(); i++){
				masterStatement += "Account aid: " + customer_accts_aids[i] + "\n";
				cust_query = "SELECT C.name, C.address FROM Customers C, Owned_By O WHERE C.taxID = O.taxID and O.aid = " + customer_accts_aids[i] +";";
				custos =  getData(cust_query);
				for(int k = 0; k < custos.size(); k++){
					masterStatement += custos[k];
					if(k%2 == 1) masterStatement += "\n";
				}
				transaction_query = "SELECT * FROM Makes M WHERE M.aid = " + customer_accts_aids[i] + ";";
				trans = getData(transaction_query);
				for(int j = 0; j < trans.size(); j++){
					//check if transaction was within the last month. If so, add 
				}
				//get initial and final(current) amt 
			}

			String primary = "SELECT A.balance from Account A where A.primaryID = " + customerid + ";";
			double lump_sum = 0.0;
			ArrayList<String> values = getData(primary)
			for(int a = 0; a < values.size(); a++){
				lump_sum += Double.parseDouble(values[a]);
			}
			if(lump_sum > 100000.00){
				JOptionPane.showMessageDialog(null, "WARNING: Insurance limit has been reached");
			}
		}
	    */
	}
	private static void closedAccounts(){/*
		String q = "SELECT * FROM Accounts A WHERE A.closed = 1;";
		ArrayList<String> x = getData(q) /// fix get data method  
					     */
	}
	private static void getDTER(){
		//String q = "SELECT C.cid FROM Customers C, Owned_By O WHERE O.cid = C.cid and "

	}
	
	private static void customerReport(){
		/*
		String master = "";
		String cid = JOptionPane.showInputDialog("Enter Customer taxID");
		if(aid != null){
			//normal query shit done here 
			String q = "SELECT A.aid, A.closed FROM Accounts A, Owned_By O WHERE O.aid = A.aid and O.taxID = " + cid + ";"; 
			ArrayList<String> accts = getData(q);
			if(accts.size() == 0){
				JOptionPane.showMessageDialog(null, "Customer Not Found, Exiting...");
			}
			master += "AID    OPEN(1 = YES, 0 = NO)\n";
			for(int i = 0; i < accts.size(); i++){
				//master += accts[i];
				if(i % 2 == 0){
					master += ",       ";
				}else{
					master += "\n";
				}
			}
			JOptionPane.showMessageDialog(null, master);
		}else {
			JOptionPane.showMessageDialog(null, "Customer Report Cancelled");
		}
		*/
	}
	
	private static void addInterest(){
		//get interest rates 
		String q = "SELECT A.student, A.interest, A.savings, A.pocket from AppInfo A;"
		ArrayList<String> interests = getData(q);

		if(interests.size() > 0){
			/// update student checking 
			String q2 = "UPDATE Accounts A SET A.balance = A.balance + (A.avgBalance * " + interests[0] + "), A.interestAdded = 1 WHERE A.closed = 0, A.interestAdded = 0;";
			getData(q2);
			// interest checking
			String q3 = "UPDATE Accounts A SET A.balance = A.balance + (A.avgBalance * " + interests[1] + "), A.interestAdded = 1 WHERE A.closed = 0, A.interestAdded = 0;";
			getData(q3);
			// savings
			String q4 = "UPDATE Accounts A SET A.balance = A.balance + (A.avgBalance * " + interests[2] + "), A.interestAdded = 1 WHERE A.closed = 0;, A.interestAdded = 0";
			getData(q4);
			// pocket
			String q5 = "UPDATE Accounts A SET A.balance = A.balance + (A.avgBalance * " + interests[3] + "), A.interestAdded = 1 WHERE A.closed = 0, A.interestAdded = 0;";
			getData(q5);
		}


	}
	private static void createAcct(){
		
	}
	private static void deleteClosed(){
		String q = "DELETE FROM Accounts A WHERE A.closed = 1";
		getData(q);

		String q2 = "DELETE FROM Customers C WHERE C.cid NOT IN " +
				  	"(SELECT C2.cid FROM Customers C2 WHERE NOT EXISTS " +
				  	"( SELECT C3.cid FROM Customers C3 WHERE NOT EXISTS (SELECT O.cid FROM Owned_By O WHERE O.cid = C3.cid and O.cid = C2.cid))"; 

		getData(q2);
	}
	private static void deleteTransactions(){
		//pretty simple, just clear all entries in the Makes table and all transactions are removed :)
		String q = "DELETE FROM Makes";
		getData(q);
	}

}