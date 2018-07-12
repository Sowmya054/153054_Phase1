package com.cg.mypaymentapp.repo;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.service.WalletServiceImpl;

public class WalletRepoImpl implements WalletRepo {

	private Map<String, Customer> data;

	static Logger myLogger = Logger.getLogger(WalletRepoImpl.class);

	public WalletRepoImpl() {
		data = new HashMap<String, Customer>();
	}

	public boolean save(Customer customer) {
		myLogger.info("putting in hashmap");
		data.put(customer.getMobileNo(), customer);
		return true;
	}

	public Customer findOne(String mobileNo) {
		Customer cus = null;
		if (data.containsKey(mobileNo)) {
			myLogger.info("retriving from hashmap");
			cus = data.get(mobileNo);
			return cus;
		} else {
			return null;
		}
	}
}
