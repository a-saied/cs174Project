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

	static int transactionID = 0;
	static int aid = 0; 

	static JPanel login = new JPanel();
	static JPanel scene = new JPanel();
	static JPanel atmPanel = new ATMPanel();
	static JPanel tellerPanel = new BankTellerPanel();

	static JFrame window; 

	static String atmPIN;

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
			window.setContentPane(tellerPanel);
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
	         ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
	         int cols = rsmd.getColumnCount();
	         //STEP 5: Extract data from result set
	         while(rs.next()){
	            //Retrieve by column name
	            // String cid  = rs.getString("cid");
	            // String cname = rs.getString("cname");
	            // String city = rs.getString("city");
	            // double discount = rs.getDouble("discount");

	         	for(int i = 0; i < cols; i++){
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
	    System.out.println("Query complete");
	    for(int j = 0; j < result.size(); j++){
	    	System.out.println(result.get(j));
	    }
	    return result;
	} 
}
