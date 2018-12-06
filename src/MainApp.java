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
	static Connection conn = null;
    static Statement stmt = null;

	static JPanel login = new JPanel();
	static JPanel scene = new JPanel();
	static JPanel atmPanel = new ATMPanel();
	static JPanel tellerPanel = new BankTellerPanel();

	static JFrame window; 
	static ArrayList<String> eom;
	static String atmPIN;
	static Calendar c;
	public static void main(String[] args){
		try{
	         //STEP 2: Register JDBC driver
	         Class.forName(JDBC_DRIVER);

	         //STEP 3: Open a connection
	         System.out.println("Connecting to a selected database...");
	         conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
	         conn.setAutoCommit(true);
	         System.out.println("Connected database successfully...");
	         //STEP 4: Execute a query
	         //System.out.println("Creating statement...");
	         stmt = conn.createStatement();
	    }catch(SQLException se){
	         //Handle errors for JDBC
	         se.printStackTrace();
	    }catch(Exception e){
	         //Handle errors for Class.forName
	         e.printStackTrace();
	    }
	    System.out.println("DB ready");
	    String[] f = {"01-31", "02-28", "03-31", "04-30", "05-31", "06-30", "07-31", "08-31", "09-30", "10-31", "11-30", "12-31"};
	    eom = new ArrayList<String>(Arrays.asList(f));
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
			window.setContentPane(tellerPanel);
			window.invalidate();
			window.validate();
		});

		JButton setDate = new JButton("Set Date");
		setDate.addActionListener(e ->{
			String dte = JOptionPane.showInputDialog("Type in your new date (YYYY-MM-DD)");
			if(dte != null){
				changeDate(dte);

			}
		});
		JButton setRate = new JButton("Set Interest Rate");
		setRate.addActionListener(e ->{
			String aid = JOptionPane.showInputDialog("Which account type would you like to change the interest of? \n (1 = Student Checking, 2 = Interest Checking, 3 = Savings, 4 = Pocket\n");
			if(aid != null){
				aid = aid.trim();
				String n = JOptionPane.showInputDialog("What is the new interest rate (Please use decimal notation?");
				if(n != null) {
					String q = null;
					int z = Integer.parseInt(aid);
					if(z == 1){
						q = "UPDATE App A Set A.student = " + (Double.parseDouble(n)/12);
					}
					else if (z == 2){
						q = "UPDATE App A Set A.interest = " + (Double.parseDouble(n)/12);
					}
					else if (z == 3){
						q = "UPDATE App A Set A.savings = " + (Double.parseDouble(n)/12);
					}
					else if (z == 4){
						q = "UPDATE App A Set A.pocket = " + (Double.parseDouble(n)/12);
					}
					if(q != null){
						try{
							boolean rs = stmt.execute(q);

						}
						catch(SQLException s){
							s.printStackTrace();
						}
						JOptionPane.showMessageDialog(null, "Interest changed");
					}
				}
			}
		});
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

	private static void changeDate(String d){
		java.sql.Date today = new java.sql.Date((long) 1);
		try{
			String q = "select today from App";

			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			if(rs.next()){
				today = rs.getDate(1);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		String q = "select (to_date('" + d + "', 'YYYY-MM-DD') - to_date('" + today + "', 'YYYY-MM-DD')) dd from dual";
		int diff = 0;
		boolean reached = false;
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			if(rs.next()){
				reached = true;
				diff = rs.getInt(1);
				System.out.println(diff);
			}
			if(!reached){
				JOptionPane.showMessageDialog(null, "Not a proper date");
			}
			else{
				System.out.println("past datediff");
				for(int i = 0; i < diff; i++){
					incrementDay();
				}
			}

		}
		catch(SQLException s){
			s.printStackTrace();
		}
	}

	private static void incrementDay(){
		//java.sql.Date iter = new java.sql.Date((long) (1000 * 60 * 60 * 24));
		String q = "SELECT today from App";
		try{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			if(rs.next()){
				java.sql.Date x = rs.getDate(1);
				System.out.println(x);
				
				String daymos = x.toString().substring(5,10);
				System.out.println(daymos);

				int ind = eom.indexOf(daymos);
				int days = Integer.parseInt(daymos.substring(3,5));
				if(ind < 0){
					String qu = "UPDATE Accounts SET avgbalance = ((avgbalance * " + (days-1) + ") + balance)/" + days;
					int z = stmt.executeUpdate(qu);
					System.out.println(days);
				}else{
					String qu = "UPDATE Accounts SET avgbalance = ((avgbalance * " + (days-1) + ") + balance)/" + days;
					int z = stmt.executeUpdate(qu);
					addInterest();
					String q3 = "DELETE FROM Makes";
					int h = stmt.executeUpdate(q3);
					String q4 = "UPDATE Accounts SET interestAdded = 0, avgbalance = balance";
					int n = stmt.executeUpdate(q4);
					String q5 = "UPDATE Pocket Set first = 0";
					int z2 = stmt.executeUpdate(q5);
					deleteClosed();
				}


				String p = "UPDATE App SET today = (Select to_date('" + x + "', 'YYYY-MM-DD') + 1 from dual)";
				int l = stmt.executeUpdate(p);
			}
		}
		catch(SQLException s){
			s.printStackTrace();
		}
	}

	public static void setupPanels(){

		////// login screen //////
		JLabel login_text = new JLabel("Enter your unique 4-digit PIN");
		login_text.setHorizontalAlignment(JLabel.CENTER);
		JTextField pin_field = new JTextField(3);
		JButton log = new JButton("Submit");
		JButton b = new JButton("Back");
		log.addActionListener(e-> {
			atmPIN = pin_field.getText();
			window.setContentPane(atmPanel);
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
	}

	/////// method to grab data from sql database //////
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
	         ResultSet rs = MainApp.stmt.executeQuery(query);
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
	         // //finally block used to close resources
	         // try{
	         //    if(stmt!=null)
	         //       conn.close();
	         // }catch(SQLException se){
	         // }// do nothing
	         // try{
	         //    if(conn!=null)
	         //       conn.close();
	         // }catch(SQLException se){
	         //    se.printStackTrace();
	         // }//end finally try
	    }//end try
	    // System.out.println("Query complete");
	    return result;
	} 

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

	private static void simpleExec(String x){
		try{
			int res = stmt.executeUpdate(x);
			//System.out.println("Transactions deleted");

		}
		catch(SQLException s){
			s.printStackTrace();
			// try{
	  //           if(MainApp.stmt!=null)
	  //              MainApp.conn.close();
	  //        }catch(SQLException se){
	  //        }// do nothing
	  //        try{
	  //           if(MainApp.conn!=null)
	  //              MainApp.conn.close();
	  //        }catch(SQLException se){
	  //           se.printStackTrace();
	  //        }//end finally try
			// System.exit(0);

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
