import java.util.*;
import java.sql.*;

abstract class Account {
	int accountId;
	Vector<Integer> owners;
	int primaryTaxID; //primary owner
	int pocketAccountID;
	boolean interestAdded; 
	boolean isClosed;
	double balance;
	String bankBranch;
	double annualRate;
	double monthlyRate;

	public void deposit(int amount) {
		this.balance += amount;
	}

	public void withdraw(int amount) {
		this.balance -= amount;
	}

	public void transfer(int amount, Account otherAccount) {
		this.withdraw(amount);
		otherAccount.deposit(amount);
	}

	public void wire(Account fromAccount, Account toAccount, int amount) {
		fromAccount.withdraw(amount);
		fromAccount.deposit(amount);
	}

	public void accrueInterest() {
		
	}

}