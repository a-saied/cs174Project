import javax.swing.*;
import java.awt.*;

public class ATMPanel extends JPanel{

	////// remember that all methods that are considered transactions should add an entry to the transaction table in the database ////
	public ATMPanel() {
		this.setLayout( new GridLayout(0, 1, 0, 10));
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
			MainApp.window.setContentPane(MainApp.scene);
			MainApp.window.invalidate();
			MainApp.window.validate();
		});

		this.add(l2);
		this.add(b1);
		this.add(b2);
		this.add(b3);
		this.add(b4);
		this.add(b5);
		this.add(b6);
		this.add(b7);
		this.add(b8);

		b1.addActionListener(e -> {
			JPanel depositPanel = new JPanel();
			JLabel amount_label = new JLabel("Enter amount to deposit.");
			JTextField amount_field = new JTextField(3);
			JButton deposit_button = new JButton("Deposit");
			depositPanel.add(amount_label);
			depositPanel.add(amount_field);
			depositPanel.add(deposit_button);
			MainApp.window.setContentPane(depositPanel);
			MainApp.window.invalidate();
			MainApp.window.validate();
			deposit_button.addActionListener(e2 -> {
				deposit(Double.valueOf(amount_field.getText()));
			});
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
	}

	public void deposit(double amount) {
		System.out.println(amount);
		try {
			ResultSet rs = stmt.executeQuery(query);
		} catch(SQLException se){
	        se.printStackTrace();
	    }
	}
	public void topUp() {}
	public void withdraw() {}
	public void purchase() {}
	public void transfer() {}
	public void collect() {}
	public void wire() {}
	public void payFriend() {}
}