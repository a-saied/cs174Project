import java.io.*;
//import javafx.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class MainApp { 

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
		scene.setLayout( new BoxLayout(scene, BoxLayout.Y_AXIS));
		scene.add(welcome);
		scene.add(atm);
		scene.add(teller);

		window.setContentPane(scene);
		//window.add(scene);

		window.setVisible(true);
	}


	public static void setupPanels(){

		////// login screen //////
		JLabel login_text = new JLabel("Enter your unique 4-digit PIN");
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
		JLabel l2 = new JLabel("Welcome, ATM User! What would you like to do?\n");
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
		JLabel l3 = new JLabel("Welcome, Bank Teller! What would you like to do?\n");
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


	//// these methods aren't actually fully formed yet, there can be different return values/parameters, just haven't gotten that far////
	private static void deposit(){

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

	}
	private static void monthlyStatement(){

	}
	private static void closedAccounts(){

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