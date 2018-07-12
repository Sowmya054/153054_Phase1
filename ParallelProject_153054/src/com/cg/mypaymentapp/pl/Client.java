package com.cg.mypaymentapp.pl;

import java.math.BigDecimal;
import java.util.Scanner;

import org.apache.log4j.PropertyConfigurator;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.service.WalletService;
import com.cg.mypaymentapp.service.WalletServiceImpl;

public class Client {
	WalletService wal;

	public Client() {
		wal = new WalletServiceImpl();
	}

	public void menu() {
		Scanner console = new Scanner(System.in);

		System.out.println("1) Create Account");
		System.out.println("2) Show Balance");
		System.out.println("3) Fund Transfer");
		System.out.println("4) Deposit Money");
		System.out.println("5) Withdraw Money");

		System.out.print("\nEnter Your Choice: ");
		int choice = console.nextInt();

		operation(choice);

	}

	public void operation(int choice) {
		switch (choice) {
		case 1:
			createAccount();
			break;
		case 2:
			showBalance();
			break;
		case 3:
			fundTransfer();
			break;
		case 4:
			depositMoney();
			break;
		case 5:
			withdrawMoney();
			break;
		case 6:
			exitApplication();
			break;
		default:
			break;
		}
	}

	private void createAccount() {
		// Taking details from the User

		Scanner console = new Scanner(System.in);

		System.out.print("\n\nEnter Your Name: ");
		String accountName = console.nextLine();

		System.out.print("Enter Phone Number: ");
		String phoneNumber = console.next();

		System.out.print("Enter Amount: ");
		BigDecimal amount = console.nextBigDecimal();

		try {
			Customer cus = wal.createAccount(accountName, phoneNumber, amount);
			System.out.println("\nYour account created successfully with account number :" + cus.getMobileNo());
		} catch (InvalidInputException e) {
			System.out.println("Something went wrong.Reason:" + e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void showBalance() {
		Scanner console = new Scanner(System.in);

		System.out.print("\nEnter the mobile number: ");
		String mobilenumber = console.next();

		try {
			Customer cus = wal.showBalance(mobilenumber);
			BigDecimal balance = cus.getWallet().getBalance();
			System.out.println("Your bank balance is:" + balance);
		} catch (InvalidInputException e) {
			System.out.println("Something went wrong.Reason:" + e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void fundTransfer() {
		Scanner console = new Scanner(System.in);

		System.out.print("\n\nEnter Source account number : ");
		String sphoneNumber = console.next();

		System.out.print("Enter destination account Number: ");
		String dphoneNumber = console.next();

		System.out.print("Enter Amount: ");
		BigDecimal amount = console.nextBigDecimal();
		try {
			wal.fundTransfer(sphoneNumber, dphoneNumber, amount);
		} catch (InvalidInputException e) {
			System.out.println("Something went wrong.Reason:" + e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void depositMoney() {
		Scanner console = new Scanner(System.in);

		System.out.print("\n\nEnter account number : ");
		String phoneNumber = console.next();
		System.out.print("Enter Amount: ");
		BigDecimal amount = console.nextBigDecimal();

		try {
			wal.depositAmount(phoneNumber, amount);
			System.out.println("successfully deposited\n");
		} catch (InvalidInputException e) {
			System.out.println("Something went wrong.Reason:" + e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void withdrawMoney() {
		Scanner console = new Scanner(System.in);

		System.out.print("\n\nEnter account number : ");
		String phoneNumber = console.next();
		System.out.print("Enter Amount: ");
		BigDecimal amount = console.nextBigDecimal();

		try {
			wal.withdrawAmount(phoneNumber, amount);
			System.out.println("successfully withdrawed");
		} catch (InvalidInputException e) {
			System.out.println("Something went wrong.Reason:" + e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void exitApplication() {
		System.out.println("Thank you for using out Appointment Service Application");
		System.exit(0);
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		Client client = new Client();

		while (true)
			client.menu();
	}
}
