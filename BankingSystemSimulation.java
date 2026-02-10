// Package declaration
package banking;

import java.util.*;

// AccountOperations interface defining basic account operations
interface AccountOperations 
{
    void deposit(double amount);
    void withdraw(double amount);
    void transfer(Account recipient, double amount);
}

// Interface for Transaction History
interface TransactionHistory 
{
    void viewTransactionHistory();
}

// Abstract class Account providing a base for concrete account types
abstract class Account implements AccountOperations, TransactionHistory 
{
    protected String accountId;
    protected String accountHolderName;
    protected double balance;
    protected int pin; // PIN added
    protected ArrayList<String> transactions;

    // Constructor
    public Account(String accountId, String accountHolderName, double balance, int pin) 
    {
        this.accountId = accountId;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.pin = pin; // Storing the PIN corresponding to the account
        this.transactions = new ArrayList<>();
    }

    // Getters and setters for PIN
    public int getPin() 
    {
        return pin;
    }

    public void setPin(int pin) 
    {
        this.pin = pin;
    }

    // Getters for account ID and balance
    public String getAccountId() 
    {
        return accountId;
    }

    public double getBalance() 
    {
        return balance;
    }

    // Abstract methods to be implemented by concrete account types
    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);
    public abstract void transfer(Account recipient, double amount);

    // Method to add transaction to history
    protected void addTransaction(String transaction) 
    {
        transactions.add(transaction);
    }

    // Method to view transaction history
    @Override
    public void viewTransactionHistory() 
    {
        System.out.println("Transaction History:");
        for (String transaction : transactions) 
        {
            System.out.println(transaction);
        }
    }
}

// SavingsAccount class implementing Account interface
class SavingsAccount extends Account 
{
    public SavingsAccount(String accountId, String accountHolderName, double balance, int pin) 
    {
        super(accountId, accountHolderName, balance, pin);
    }

    // Implementation of deposit method
    @Override
    public void deposit(double amount) 
    {
        balance += amount;
        System.out.println(amount + " deposited successfully.");
        addTransaction("Deposit: +" + amount); // for Transaction history
        printBalance();
    }

    // Implementation of withdraw method
    @Override
    public void withdraw(double amount) 
    {
        if (balance >= amount) 
        {
            balance -= amount;
            System.out.println(amount + " withdrawn successfully.");
            addTransaction("Withdrawal: -" + amount);
            printBalance();
        } 
        else 
        {
            System.out.println("Insufficient funds.");
        }
    }

    // Implementation of transfer method
    @Override
    public void transfer(Account recipient, double amount) 
    {
        if (balance >= amount) 
        {
            balance -= amount;
            recipient.deposit(amount);
            System.out.println(amount + " transferred successfully to " + recipient.getAccountId());
            addTransaction("Transfer to " + recipient.getAccountId() + ": -" + amount);
            printBalance();
        } 
        else 
        {
            System.out.println("Insufficient funds for transfer.");
        }
    }

    // Method to print balance after each transaction
    private void printBalance() 
    {
        System.out.println("Current Balance: " + balance);
    }
}

// Main class to run the program
public class BankingSystemSimulation 
{
    // Static variable
    private static ArrayList<Account> accounts = new ArrayList<>();

    // Main method and menu loop
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);

        int choice;
        do 
        {
            // Menu using switch-case
            System.out.println("1. Create Savings Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Account Balance Inquiry");
            System.out.println("6. Transaction History");
            System.out.println("7. Account Closure");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) 
            {
                case 1:
                    // Create Savings Account
                    createSavingsAccount();
                    break;
                case 2:
                    // Deposit Money
                    depositMoney();
                    break;
                case 3:
                    // Withdraw Money
                    withdrawMoney();
                    break;
                case 4:
                    // Transfer Money
                    transferMoney();
                    break;
                case 5:
                    // Account Balance Inquiry
                    checkAccountBalance();
                    break;
                case 6:
                    // Transaction History
                    viewTransactionHistory();
                    break;
                case 7:
                    // Account Closure
                    closeAccount();
                    break;
                case 8:
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } 
        while (choice != 8);

        scanner.close();
    }
    
    // Method to create savings account
    private static void createSavingsAccount() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        
        // Check if account already exists
        for (Account acc : accounts) 
        {
            if (accountId.equals(acc.getAccountId())) 
            {
                System.out.println("Account already exists.");
                return;
            }
        }
        
        System.out.print("Enter account holder name: ");
        String accountHolderName = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();
        System.out.print("Set PIN: ");
        int pin = scanner.nextInt(); // Prompt user to set PIN
        accounts.add(new SavingsAccount(accountId, accountHolderName, initialBalance, pin));
        System.out.println("Savings Account created successfully.");
    }

    // Method to deposit money
    private static void depositMoney() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        Account account = findAccount(accountId);
        if (account != null) 
        {
            System.out.print("Enter PIN: ");
            int enteredPin = scanner.nextInt();
            if (enteredPin == account.getPin()) 
            { // Verify PIN
                System.out.print("Enter amount to deposit: ");
                double amount = scanner.nextDouble();
                account.deposit(amount);
            } 
            else 
            {
                System.out.println("Incorrect PIN.");
            }
        }
    }

    // Method to withdraw money
    private static void withdrawMoney() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        Account account = findAccount(accountId);
        if (account != null) 
        {
            System.out.print("Enter PIN: ");
            int enteredPin = scanner.nextInt();
            if (enteredPin == account.getPin()) 
            { // Verify PIN
                System.out.print("Enter amount to withdraw: ");
                double amount = scanner.nextDouble();
                account.withdraw(amount);
            } 
            else 
            {
                System.out.println("Incorrect PIN.");
            }
        }
    }

    // Method to transfer money
    private static void transferMoney() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter sender account ID: ");
        String senderAccountId = scanner.nextLine();
        Account sender = findAccount(senderAccountId);
        if (sender != null) 
        {
            System.out.print("Enter PIN: ");
            int enteredPin = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (enteredPin == sender.getPin()) 
            { // Verify PIN
                System.out.print("Enter recipient account ID: ");
                String recipientAccountId = scanner.nextLine();
                Account recipient = findAccount(recipientAccountId);
                if (recipient != null) 
                {
                    System.out.print("Enter amount to transfer: ");
                    double amount = scanner.nextDouble();
                    if (sender.getBalance() >= amount) 
                    { // Check if sender has sufficient balance
                        sender.transfer(recipient, amount);
                    } 
                    else 
                    {
                        System.out.println("Insufficient funds for transfer.");
                    }
                } 
                else 
                {
                    System.out.println("Recipient account does not exist.");
                }
            } 
            else 
            {
                System.out.println("Incorrect PIN.");
            }
        }
    }

    // Method to check account balance
    private static void checkAccountBalance() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        Account account = findAccount(accountId);
        if (account != null) 
        {
            System.out.print("Enter PIN: ");
            int enteredPin = scanner.nextInt();
            if (enteredPin == account.getPin()) 
            { // Verify PIN
                System.out.println("Account Balance: " + account.getBalance());
            } 
            else 
            {
                System.out.println("Incorrect PIN.");
            }
        }
    }

    // Method to view transaction history
    private static void viewTransactionHistory() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        Account account = findAccount(accountId);
        if (account != null) 
        {
            System.out.print("Enter PIN: ");
            int enteredPin = scanner.nextInt();
            if (enteredPin == account.getPin()) 
            { // Verify PIN
                account.viewTransactionHistory();
            } 
            else 
            {
                System.out.println("Incorrect PIN.");
            }
        }
    }

    // Method to close account
    private static void closeAccount() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        Account account = findAccount(accountId);
        if (account != null) 
        {
            System.out.print("Enter PIN: ");
            int enteredPin = scanner.nextInt();
            if (enteredPin == account.getPin()) 
            { // Verify PIN
                accounts.remove(account);
                System.out.println("Account closed successfully.");
            } 
            else 
            {
                System.out.println("Incorrect PIN.");
            }
        }
    }

    // Method to find account by ID
    private static Account findAccount(String accountId) 
    {
        for (Account acc : accounts) 
        {
            if (acc.getAccountId().equals(accountId)) 
            {
                return acc;
            }
        }
        System.out.println("Account not found.");
        return null;
    }
}