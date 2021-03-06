import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ATMPanel extends JPanel {
	ATMFunctions atmf = new ATMFunctions();

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
		JButton b9 = new JButton("Change Pin");
		JButton b10 = new JButton("Back");
		b10.addActionListener(e -> {
			MainApp.window.setContentPane(MainApp.scene);
			MainApp.window.invalidate();
			MainApp.window.validate();
		});
		JButton b11 = new JButton("Cancel");
		b11.addActionListener(e -> {
			MainApp.window.setContentPane(this);
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
		this.add(b9);
		this.add(b10);

		//Desposit
		b1.addActionListener(e -> {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter amount to deposit. $");
			JTextField amount_field = new JTextField(3);
			JLabel label2 = new JLabel("Enter the Checking/Savings Account ID to deposit to.");
			JTextField aid_field = new JTextField(5);
			JButton btn = new JButton("Deposit");
			panel.add(label2);
			panel.add(aid_field);
			panel.add(label); 
			panel.add(amount_field); 
			panel.add(btn);
			panel.add(b11);
			MainApp.window.setContentPane(panel);
			MainApp.window.invalidate();
			MainApp.window.validate();
			btn.addActionListener(e2 -> {
				atmf.deposit(Double.valueOf(amount_field.getText()), aid_field.getText());
				MainApp.window.setContentPane(this);
				MainApp.window.invalidate();
				MainApp.window.validate();
			});
		});

		//Top-Up
		b2.addActionListener(e -> {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter amount to transfer. $");
			JTextField amount_field = new JTextField(3);
			JLabel label2 = new JLabel("Enter the Pocket Account ID to transfer to:");
			JTextField aid_field = new JTextField(5);
			JButton btn = new JButton("Top-Up");
			panel.add(label2);
			panel.add(aid_field);
			panel.add(label);
			panel.add(amount_field);
			panel.add(btn);
			panel.add(b11);
			MainApp.window.setContentPane(panel);
			MainApp.window.invalidate();
			MainApp.window.validate();
			btn.addActionListener(e2 -> {
				atmf.topUp(Double.valueOf(amount_field.getText()), aid_field.getText());
				MainApp.window.setContentPane(this);
				MainApp.window.invalidate();
				MainApp.window.validate();
			});
		});

		//Withdraw
		b3.addActionListener(e -> {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter amount to withdraw. $");
			JTextField amount_field = new JTextField(3);
			JLabel label2 = new JLabel("Enter the Checking/Savings Account ID to withdraw from.");
			JTextField aid_field = new JTextField(5);
			JButton btn = new JButton("Withdraw");
			panel.add(label2);
			panel.add(aid_field);
			panel.add(label);
			panel.add(amount_field);
			panel.add(btn);
			panel.add(b11);
			MainApp.window.setContentPane(panel);
			MainApp.window.invalidate();
			MainApp.window.validate();
			btn.addActionListener(e2 -> {
				atmf.withdraw(Double.valueOf(amount_field.getText()), aid_field.getText());
				MainApp.window.setContentPane(this);
				MainApp.window.invalidate();
				MainApp.window.validate();
			});
		});

		//Purchase
		b4.addActionListener(e -> {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter total purchase. $");
			JTextField amount_field = new JTextField(3);
			JLabel label2 = new JLabel("Enter the Pocket Account ID to purchase from.");
			JTextField aid_field = new JTextField(5);
			JButton btn = new JButton("Enter");
			panel.add(label2);
			panel.add(aid_field);
			panel.add(label);
			panel.add(amount_field);
			panel.add(btn);
			panel.add(b11);
			MainApp.window.setContentPane(panel);
			MainApp.window.invalidate();
			MainApp.window.validate();
			btn.addActionListener(e2 -> {
				atmf.purchase(Double.valueOf(amount_field.getText()), aid_field.getText());
				MainApp.window.setContentPane(this);
				MainApp.window.invalidate();
				MainApp.window.validate();
			});
		});

		//Transfer
		b5.addActionListener(e -> {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter amount to transfer. $");
			JTextField amount_field = new JTextField(3);
			JLabel label2 = new JLabel("Enter the Account ID to transfer from.");
			JTextField aid_field2 = new JTextField(5);
			JLabel label3 = new JLabel("Enter the Account ID to transfer to.");
			JTextField aid_field3 = new JTextField(5);
			JButton btn = new JButton("Transfer");
			panel.add(label2);
			panel.add(aid_field2);
			panel.add(label3);
			panel.add(aid_field3);
			panel.add(label);
			panel.add(amount_field);
			panel.add(btn);
			panel.add(b11);
			MainApp.window.setContentPane(panel);
			MainApp.window.invalidate();
			MainApp.window.validate();
			btn.addActionListener(e2 -> {
				atmf.transfer(Double.valueOf(amount_field.getText()), aid_field2.getText(), aid_field3.getText());
				MainApp.window.setContentPane(this);
				MainApp.window.invalidate();
				MainApp.window.validate();
			});
		});

		//Collect
		b6.addActionListener(e -> {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter amount to collect. $");
			JTextField amount_field = new JTextField(3);
			JLabel label2 = new JLabel("Enter the Pocket Account ID to collect from.");
			JTextField aid_field = new JTextField(5);
			JButton btn = new JButton("Collect");
			panel.add(label2);
			panel.add(aid_field);
			panel.add(label);
			panel.add(amount_field);
			panel.add(btn);
			panel.add(b11);
			MainApp.window.setContentPane(panel);
			MainApp.window.invalidate();
			MainApp.window.validate();
			btn.addActionListener(e2 -> {
				atmf.collect(Double.valueOf(amount_field.getText()), aid_field.getText());
				MainApp.window.setContentPane(this);
				MainApp.window.invalidate();
				MainApp.window.validate();
			});
		});

		//Wire
		b7.addActionListener(e -> {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter amount to wire. $");
			JTextField amount_field = new JTextField(3);
			JLabel label2 = new JLabel("Enter the Account ID to transfer from.");
			JTextField aid_field2 = new JTextField(5);
			JLabel label3 = new JLabel("Enter the Account ID to transfer to.");
			JTextField aid_field3 = new JTextField(5);
			JButton btn = new JButton("Transfer");
			panel.add(label2);
			panel.add(aid_field2);
			panel.add(label3);
			panel.add(aid_field3);
			panel.add(label);
			panel.add(amount_field);
			panel.add(btn);
			panel.add(b11);
			MainApp.window.setContentPane(panel);
			MainApp.window.invalidate();
			MainApp.window.validate();
			btn.addActionListener(e2 -> {
				atmf.wire(Double.valueOf(amount_field.getText()), aid_field2.getText(), aid_field3.getText());
				MainApp.window.setContentPane(this);
				MainApp.window.invalidate();
				MainApp.window.validate();
			});
		});

		//Pay-Friend
		b8.addActionListener(e -> {
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter amount to pay your friend. $");
			JTextField amount_field = new JTextField(3);
			JLabel label2 = new JLabel("Enter the Pocket Account ID to pay from.");
			JTextField aid_field2 = new JTextField(5);
			JLabel label3 = new JLabel("Enter the Pocket Account ID to pay to.");
			JTextField aid_field3 = new JTextField(5);
			JButton btn = new JButton("Pay Friend");
			panel.add(label2);
			panel.add(aid_field2);
			panel.add(label3);
			panel.add(aid_field3);
			panel.add(label);
			panel.add(amount_field);
			panel.add(btn);
			panel.add(b11);
			MainApp.window.setContentPane(panel);
			MainApp.window.invalidate();
			MainApp.window.validate();
			btn.addActionListener(e2 -> {
				atmf.payFriend(Double.valueOf(amount_field.getText()), aid_field2.getText(), aid_field3.getText());
				MainApp.window.setContentPane(this);
				MainApp.window.invalidate();
				MainApp.window.validate();
			});
		});

		//Set Pin
		b9.addActionListener(e -> {
			JPanel panel = new JPanel();
			JLabel label1 = new JLabel("Enter your old pin: ");
			JTextField field1 = new JTextField(4);
			JLabel label2 = new JLabel("Enter your new pin: ");
			JTextField field2 = new JTextField(4);
			JButton btn = new JButton("Change");
			panel.add(label1);
			panel.add(field1);
			panel.add(label2);
			panel.add(field2);
			panel.add(btn);
			panel.add(b11);
			MainApp.window.setContentPane(panel);
			MainApp.window.invalidate();
			MainApp.window.validate();
			btn.addActionListener(e2 -> {
				atmf.setPin(field1.getText(), field2.getText());
				MainApp.window.setContentPane(this);
				MainApp.window.invalidate();
				MainApp.window.validate();
			});
		});
	}
}