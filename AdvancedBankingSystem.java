import java.util.*;
 
class Account {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private String accountType; // Savings or Checking
    private boolean isActive;
    private List<String> transactionHistory;
    private double overdraftLimit;
    private static final double INTEREST_RATE = 0.02; // 2% annual interest
 
    public Account(String accountNumber, String accountHolderName, String accountType) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = 0.0;
        this.accountType = accountType;
        this.isActive = true;
        this.transactionHistory = new ArrayList<>();
        this.overdraftLimit = 500.0; // Default overdraft limit
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }

    public void reactivate() {
        isActive = true;
    }

    public void deposit(double amount) {
        if (amount > 0 && isActive) {
            balance += amount;
            transactionHistory.add("Deposited: " + amount);
            System.out.println("Successfully deposited: " + amount);
        } else {
            System.out.println("Deposit amount must be positive or account is inactive.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance + overdraftLimit && isActive) {
            balance -= amount;
            transactionHistory.add("Withdrawn: " + amount);
            System.out.println("Successfully withdrawn: " + amount);
        } else {
            System.out.println("Insufficient balance/overdraft or invalid amount or account is inactive.");
        }
    }

    public void transfer(Account target, double amount) {
        if (amount > 0 && amount <= balance + overdraftLimit && isActive && target.isActive) {
            balance -= amount;
            target.deposit(amount);
            transactionHistory.add("Transferred: " + amount + " to " + target.getAccountNumber());
            System.out.println("Successfully transferred: " + amount + " to " + target.getAccountNumber());
        } else {
            System.out.println("Transfer failed due to insufficient balance/overdraft or account is inactive.");
        }
    }

    public void calculateInterest() {
        if (accountType.equals("Savings")) {
            double interest = balance * INTEREST_RATE;
            balance += interest;
            transactionHistory.add("Interest credited: " + interest);
        }
    }

    public void printTransactionHistory() {
        System.out.println("Transaction History for account " + accountNumber + " (" + accountType + "):");
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }

    public void printFilteredTransactionHistory(String filter) {
        System.out.println("Filtered Transaction History for account " + accountNumber + " (" + accountType + "):");
        for (String transaction : transactionHistory) {
            if (transaction.toLowerCase().contains(filter.toLowerCase())) {
                System.out.println(transaction);
            }
        }
    }
}

class BankingSystem {
    private Map<String, Account> accounts;
    private Scanner scanner;

    public BankingSystem() {
        accounts = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    private boolean authenticate(String accountNumber) {
        System.out.print("Enter account holder name: ");
        String name = scanner.nextLine();
        if (accounts.containsKey(accountNumber) && accounts.get(accountNumber).getAccountHolderName().equals(name)) {
            return true;
        } else {
            System.out.println("Authentication failed.");
            return false;
        }
    }

    public void createAccount() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter account holder name: ");
        String accountHolderName = scanner.nextLine();
        System.out.print("Enter account type (Savings/Checking): ");
        String accountType = scanner.nextLine();

        if (!accounts.containsKey(accountNumber)) {
            Account newAccount = new Account(accountNumber, accountHolderName, accountType);
            accounts.put(accountNumber, newAccount);
            System.out.println("Account created successfully.");
        } else {
            System.out.println("Account number already exists.");
        }
    }

    public void deposit() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        if (accounts.containsKey(accountNumber) && authenticate(accountNumber)) {
            System.out.print("Enter deposit amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();  // Consume newline
            accounts.get(accountNumber).deposit(amount);
        } else {
            System.out.println("Account not found.");
        }
    }

    public void withdraw() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        if (accounts.containsKey(accountNumber) && authenticate(accountNumber)) {
            System.out.print("Enter withdrawal amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();  // Consume newline
            accounts.get(accountNumber).withdraw(amount);
        } else {
            System.out.println("Account not found.");
        }
    }

    public void transferFunds() {
        System.out.print("Enter source account number: ");
        String sourceAccountNumber = scanner.nextLine();
        System.out.print("Enter target account number: ");
        String targetAccountNumber = scanner.nextLine();

        if (accounts.containsKey(sourceAccountNumber) && accounts.containsKey(targetAccountNumber) && authenticate(sourceAccountNumber)) {
            System.out.print("Enter transfer amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();  // Consume newline
            accounts.get(sourceAccountNumber).transfer(accounts.get(targetAccountNumber), amount);
        } else {
            System.out.println("Account(s) not found.");
        }
    }

    public void printBalance() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        if (accounts.containsKey(accountNumber) && authenticate(accountNumber)) {
            double balance = accounts.get(accountNumber).getBalance();
            System.out.println("Current balance: " + balance);
        } else {
            System.out.println("Account not found.");
        }
    }

    public void printTransactionHistory() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        if (accounts.containsKey(accountNumber) && authenticate(accountNumber)) {
            accounts.get(accountNumber).printTransactionHistory();
        } else {
            System.out.println("Account not found.");
        }
    }

    public void printFilteredTransactionHistory() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        if (accounts.containsKey(accountNumber) && authenticate(accountNumber)) {
            System.out.print("Enter filter keyword (e.g., 'Deposit', 'Withdraw', 'Transfer'): ");
            String filter = scanner.nextLine();
            accounts.get(accountNumber).printFilteredTransactionHistory(filter);
        } else {
            System.out.println("Account not found.");
        }
    }

    public void calculateInterest() {
        for (Account account : accounts.values()) {
            account.calculateInterest();
        }
        System.out.println("Interest calculated and credited to all savings accounts.");
    }

    public void deactivateAccount() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        if (accounts.containsKey(accountNumber) && authenticate(accountNumber)) {
            accounts.get(accountNumber).deactivate();
            System.out.println("Account deactivated successfully.");
        } else {
            System.out.println("Account not found.");
        }
    }

    public void reactivateAccount() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        if (accounts.containsKey(accountNumber) && authenticate(accountNumber)) {
            accounts.get(accountNumber).reactivate();
            System.out.println("Account reactivated successfully.");
        } else {
            System.out.println("Account not found.");
        }
    }

    public void run() {
        while (true) {
            System.out.println("\n--- Banking System Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer Funds");
            System.out.println("5. Check Balance");
            System.out.println("6. Print Transaction History");
            System.out.println("7. Print Filtered Transaction History");
            System.out.println("8. Calculate Interest");
            System.out.println("9. Deactivate Account");
            System.out.println("10. Reactivate Account");
            System.out.println("11. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    transferFunds();
                    break;
                case 5:
                    printBalance();
                    break;
                case 6:
                    printTransactionHistory();
                    break;
                case 7:
                    printFilteredTransactionHistory();
                    break;
                case 8:
                    calculateInterest();
                    break;
                case 9:
                    deactivateAccount();
                    break;
                case 10:
                    reactivateAccount();
                    break;
                case 11:
                    System.out.println("Exiting Banking System.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

public class AdvancedBankingSystem {
    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();
        bankingSystem.run();
    }
}
