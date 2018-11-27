import java.util.*;

public class Customer {
	private String name;
	private String taxID;
	private String address;
	private String pin;
	private HashMap<String, Account> accounts = new HashMap<>(); 

	public Customer(String name, String taxID, String address, String pin, Account a) {
		this.name = name;
		this.taxID = taxId;
		this.address = address;
		this.pin = pin;
		accounts.put(a.getAccountId(), a);
	}

	public boolean verifyPIN(String comparedPin) {
		return (this.pin == comparedPin);
	}

	public void setPIN(String oldPin, String newPin) {
		if (oldPin == this.pin) {
			pin = newPin;
		}
	}

	public void purchase(int accountId, int amount) {
		accounts.get(accountId).withdraw(amount);
	}

}