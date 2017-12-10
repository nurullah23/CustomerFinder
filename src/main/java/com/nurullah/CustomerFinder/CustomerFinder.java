package com.nurullah.CustomerFinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerFinder {
	
	private static final double DUBLIN_LATITUDE_IN_RAD = Math.toRadians(53.3393);
	private static final double DUBLIN_LONGITUDE_IN_RAD = Math.toRadians(-6.2576841);
	private static final double EARTH_RADIUS_IN_KM = 6371;
	private static final int CLOSENESS_DISTANCE_IN_KM = 100;
	
	public List<Customer> customersToBeInvited = new ArrayList<>();

	public static void main(String[] args) {
		String filePath = null;
		if (args != null && args.length > 1 && args[0].equals("-path")) {
			filePath = args[1];
			CustomerFinder cf = new CustomerFinder();
			cf.parseFile(filePath);
			cf.inviteCustomers();
		}
		else {
			System.out.println("Please specify the input file path using the '-path' argument");
		}
	}
	
	public void parseFile(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
		    String line;
		    ObjectMapper mapper = new ObjectMapper();
		    while ((line = br.readLine()) != null) {
		    	try {
		    		// Parse line
		    		Customer c = mapper.readValue(line, Customer.class);
		    		// Check distance
		    		if (isCustomerClose(c)) {
		    			customersToBeInvited.add(c);
		    		}
		    	}
		    	catch (Exception e) {
		    		System.out.println("Could not parse line: " + line);
		    		e.printStackTrace();
		    	}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isCustomerClose(Customer customer) {
		double latitudeInRad = Math.toRadians(customer.getLatitude());
		double longitudeInRad = Math.toRadians(customer.getLongitude());
		double distance = Math.acos(Math.sin(DUBLIN_LATITUDE_IN_RAD) * Math.sin(latitudeInRad) +
				Math.cos(DUBLIN_LATITUDE_IN_RAD) * Math.cos(latitudeInRad) *
				Math.cos(Math.abs(DUBLIN_LONGITUDE_IN_RAD - longitudeInRad))) * EARTH_RADIUS_IN_KM;
		return distance <= CLOSENESS_DISTANCE_IN_KM;
	}
	
	public void inviteCustomers() {
		if (customersToBeInvited.size() == 0) {
			System.out.println("No customers nearby!");
		}
		else {
			customersToBeInvited.stream()
				.sorted((c1, c2) -> c1.getUserId() - c2.getUserId())
				.forEach((c) -> System.out.println(c.getName() + " (id: " + c.getUserId() + ")"));
		}
	}
}
