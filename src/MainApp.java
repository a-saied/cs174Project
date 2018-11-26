import java.io.*;
//import javafx.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
////// local computer run cmd: java -cp /Users/AhmedS/Downloads/ojdbc6.jar:.: MainApp
////// CSIL computer run cmd: java -cp /fs/student/asaied/Documents/ojdbc6.jar:.: MainApp
public class MainApp { 

	/////// JDBC setup ////////
	static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
   	static final String DB_URL = "jdbc:oracle:thin:@cloud-34-133.eci.ucsb.edu:1521:XE";
   	static final String USERNAME = "asaied";
	static final String PASSWORD = "cs174";

	static int transactionID = 0;
	static int aid = 0; 

	static JPanel login = new JPanel();
	static JPanel scene = new JPanel();
	static JPanel atmScene = new JPanel();
	static JPanel tellerScene = new JPanel();

	static JFrame window; 

	public static void main(String[] args){


		setupPanels();
		window = new JFrame();
		window.setSize(400,400);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		JLabel welcome = new JLabel("Banking App\n Created by Ahmed Saied Lindsey Nguyen\n");

		JButton atm = new JButton("Go to ATM interface");
		atm.addActionListener(e -> {
			//System.out.println("atm");
			window.setContentPane(login);
			window.invalidate();
			window.validate();
		});
		JButton teller = new JButton("Go to Bank Teller Interface");
		teller.addActionListener(e -> {
			window.setContentPane(tellerScene);
			window.invalidate();
			window.validate();
		});

		JButton setDate = new JButton("Set Date");
		// setDate.addActionListener(e ->{

		// })
		JButton setRate = new JButton("Set Interest Rate");
		// setRate.addActionListener(e ->{

		// })
		scene.setLayout( new GridLayout(0,1, 0, 20));
		welcome.setHorizontalAlignment(JLabel.CENTER);
		scene.add(welcome);
		scene.add(atm);
		scene.add(teller);
		scene.add(setDate);
		scene.add(setRate);

		window.setContentPane(scene);
		//window.add(scene);

		window.setVisible(true);
	}


	public static void setupPanels(){

		////// login screen //////
		JLabel login_text = new JLabel("Enter your unique 4-digit PIN");
		login_text.setHorizontalAlignment(JLabel.CENTER);
		JTextField pin_field = new JTextField(3);
		JButton log = new JButton("Submit");
		JButton b = new JButton("Back");
		log.addActionListener(e-> {
			window.setContentPane(atmScene);
			window.invalidate();
			window.validate();
		});
		b.addActionListener(e -> {
			window.setContentPane(scene);
			window.invalidate();
			window.validate();
		});
		login.add(login_text);
		login.add(pin_field);
		login.add(log);
		login.add(b);

		//////atm screen ///////
		atmScene.setLayout( new GridLayout(0,1, 0, 10));
		JLabel l2 = new JLabel("Welcome, ATM User! What would you like to do?\n");
		l2.setHorizontalAlignment(JLabel.CENTER);
		JButton b1 = new JButton("Deposit");
		JButton b2 = new JButton("Top Up");
		JButton b3 = new JButton("Withdraw");
		JButton b4 = new JButton("Purchase");
		JButton b5 = new JButton("Transfer");
		JButton b6 = new JButton("Collect");
		JButton b7 = new JButton("Wire");
		JButton b8 = new JButton("Pay-Friend");
		JButton b9 = new JButton("Back");
		b9.addActionListener(e -> {
			window.setContentPane(scene);
			window.invalidate();
			window.validate();
		});
		// b1.addActionListener(al);
		// b2.addActionListener(al);
		// b3.addActionListener(al);
		// b4.addActionListener(al);
		// b5.addActionListener(al);
		// b6.addActionListener(al);
		// b7.addActionListener(al);
		// b8.addActionListener(al);
		atmScene.add(l2);
		atmScene.add(b1);
		atmScene.add(b2);
		atmScene.add(b3);
		atmScene.add(b4);
		atmScene.add(b5);
		atmScene.add(b6);
		atmScene.add(b7);
		atmScene.add(b8);
		atmScene.add(b9);

		//// teller screen
		tellerScene.setLayout( new GridLayout(0,1, 0, 10));
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
			window.setContentPane(scene);
			window.invalidate();
			window.validate();
		});

		// c1.addActionListener(e -> {
		// });
		// c2.addActionListener(al);
		// c3.addActionListener(al);
		// c4.addActionListener(al);
		// c5.addActionListener(al);
		// c6.addActionListener(al);
		// c7.addActionListener(al);
		// c8.addActionListener(al);
		// c9.addActionListener(al);
		
		tellerScene.add(l3);
		tellerScene.add(c1);
		tellerScene.add(c2);
		tellerScene.add(c3);
		tellerScene.add(c4);
		tellerScene.add(c5);
		tellerScene.add(c6);
		tellerScene.add(c7);
		tellerScene.add(c8);
		tellerScene.add(c9);
		tellerScene.add(c0);


		////// adding ALL Action Listeners here, methods will be written separately //////// 
		////// remember that all methods that are considered transactions should add an entry to the transaction table in the database ////
		b1.addActionListener(e -> {
			deposit();
		});
		b2.addActionListener(e -> {
			topUp();
		});
		b3.addActionListener(e -> {
			withdraw();
		});
		b4.addActionListener(e -> {
			purchase();
		});
		b5.addActionListener(e -> {
			transfer();
		});
		b6.addActionListener(e -> {
			collect();
		});
		b7.addActionListener(e -> {
			wire();
		});
		b8.addActionListener(e -> {
			payFriend();
		});




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

	/////// method to grab data from sql database //////
	private static ArrayList<String> getData(String query, String[] cols){ /// everything will come out a String
		Connection conn = null;
      	Statement stmt = null;
      	ArrayList<String> result = new ArrayList<String>();
      	try{
	         //STEP 2: Register JDBC driver
	         Class.forName(JDBC_DRIVER);

	         //STEP 3: Open a connection
	         System.out.println("Connecting to a selected database...");
	         conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
	         System.out.println("Connected database successfully...");
	         
	         //STEP 4: Execute a query
	         System.out.println("Creating statement...");
	         stmt = conn.createStatement();

	         //String sql = "SELECT cid, cname, city, discount FROM cs174.Customers";
	         ResultSet rs = stmt.executeQuery(query);
	         //STEP 5: Extract data from result set
	         while(rs.next()){
	            //Retrieve by column name
	            // String cid  = rs.getString("cid");
	            // String cname = rs.getString("cname");
	            // String city = rs.getString("city");
	            // double discount = rs.getDouble("discount");

	         	for(int i = 0; i < cols.length; i++){
	         		result.add(rs.getString(cols[i]));
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
	    System.out.println("Query complete");
	    for(int j = 0; j < result.size(); j++){
	    	System.out.println(result.get(j));
	    }
	    return result;
	} 


	//// these methods aren't actually fully formed yet, there can be different return values/parameters, just haven't gotten that far////
	private static void deposit(){
		String[] x = {"cid", "cname", "city", "discount"};
		getData("SELECT cid, cname, city, discount FROM cs174.Customers", x);
	}
	private static void topUp(){

	}
	private static void withdraw(){

	}
	private static void purchase(){

	}
	private static void transfer(){

	}
	private static void collect(){

	}
	private static void wire(){

	}
	private static void payFriend(){

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

	}
	private static void customerReport(){

	}
	private static void addInterest(){

	}
	private static void createAcct(){

	}
	private static void deleteClosed(){

	}
	private static void deleteTransactions(){

	}

}
