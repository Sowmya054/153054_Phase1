
package com.cg.mypaymentapp.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {
	static Logger myLogger = Logger.getLogger(WalletServiceImpl.class);
	private WalletRepo repo = new WalletRepoImpl();
	Map<String, Customer> data = new HashMap<String, Customer>();

	public WalletServiceImpl(WalletRepo repo) {
		super();
		myLogger.info("constructor called");
		this.repo = repo;
	}

	public WalletServiceImpl(Map<String, Customer> data) {
		super();
		this.data = data;
	}

	public WalletServiceImpl() {

	}

	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {
		if (!isValidName(name) || !isValidMobile(mobileNo) || !isValidAmount(amount)) {
			throw new InvalidInputException("Sorry , your details are incorrect");
		}
		Customer cus = new Customer(name, mobileNo, new Wallet(amount));
		myLogger.info("create account");
		boolean b = repo.save(cus);
		return cus;

	}

	public Customer showBalance(String mobileNo) {
		if (!isValidMobile(mobileNo)) {
			throw new InvalidInputException("Invalid Mobile number");
		} else {
			Customer customer = repo.findOne(mobileNo);
			myLogger.info("show balance");
			if (customer != null)
				return customer;
			else
				throw new InvalidInputException("account with mobile number not found ");
		}
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {
		if (!isValidMobile(sourceMobileNo) || !isValidMobile(targetMobileNo) || !isValidAmount(amount)) {
			throw new InvalidInputException("Sorry , your details are incorrect");
		}
		Customer sourceCustomer = repo.findOne(sourceMobileNo);
		Customer destCustomer = repo.findOne(targetMobileNo);

		if (sourceCustomer != null && destCustomer != null) {
			Wallet balance1 = sourceCustomer.getWallet();
			Wallet balance2 = destCustomer.getWallet();
			if (balance1.getBalance().compareTo(amount) > 0) {
				BigDecimal remainBalance = balance1.getBalance().subtract(amount);
				BigDecimal addedBalance = balance2.getBalance().add(amount);
				balance1.setBalance(remainBalance);
				balance2.setBalance(addedBalance);
				myLogger.info("fund transfer");
				return sourceCustomer;
			} else {
				throw new InsufficientBalanceException("insufficient balance");

			}
		} else {
			throw new InvalidInputException("account with mobile number not found ");
		}

	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		if (!isValidMobile(mobileNo) || !isValidAmount(amount)) {
			throw new InvalidInputException("Sorry , your details are incorrect");
		}
		Customer cus = repo.findOne(mobileNo);
		if (cus != null) {
			myLogger.info("deposit money");
			Wallet balance = cus.getWallet();
			balance.setBalance(balance.getBalance().add(amount));
			return cus;
		} else {
			throw new InvalidInputException("account with mobile number not found ");
		}
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		if (!isValidMobile(mobileNo) || !isValidAmount(amount)) {
			throw new InvalidInputException("Sorry , your details are incorrect");
		}
		Customer cus = repo.findOne(mobileNo);
		if (cus != null) {
			Wallet balance = cus.getWallet();
			if (balance.getBalance().compareTo(amount) > 0) {
				BigDecimal addedBalance = balance.getBalance().subtract(amount);
				balance.setBalance(addedBalance);
				myLogger.info("withdraw money");
				return cus;
			} else {
				throw new InsufficientBalanceException("Insufficient balance ");
			}
		} else {
			throw new InvalidInputException("account with mobile number not found ");
		}
	}

	private boolean isValidMobile(String mobileNo) {
		if (String.valueOf(mobileNo).matches("[1-9][0-9]{9}")) {
			return true;
		} else {
			return false;
		}

	}

	private boolean isValidAmount(BigDecimal amount) {
		BigDecimal val = new BigDecimal("0");
		if (amount.compareTo(val) > 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidName(String name) {
		if (name.isEmpty()) {
			return false;
		} else {
			return true;
		}

	}

}
