
public class ATMFunctions {

	public ATMFunctions() {
		
	}

	public void deposit(double amount) {
		System.out.println(amount);
		/*
		String query = "UPDATE Account A SET A.balance = A.balance+" + amount + " WHERE A.PIN=" + MainApp.atmPIN;
		try {
			ResultSet rs = MainApp.stmt.executeQuery(query);
		} catch(SQLException se){
	        se.printStackTrace();
	    }
	    */
	}

	public void topUp(double amount) {

	}

	public void withdraw(double amount) {
		System.out.println(amount);
		/*
		String query = "UPDATE Account A SET A.balance = A.balance+" - amount + " WHERE A.PIN=" + MainApp.atmPIN;
		try {
			ResultSet rs = MainApp.stmt.executeQuery(query);
		} catch(SQLException se){
	        se.printStackTrace();
	    }
	    */
	}

	//Subtract money from the pocket account balance
	public void purchase(double amount) {
		System.out.println(amount);
	}

	public void transfer() {}
	public void collect() {}
	public void wire() {}
	public void payFriend() {}
}