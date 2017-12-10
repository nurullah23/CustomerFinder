package com.nurullah.CustomerFinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CustomerFinderTest {
	
	private final ByteArrayOutputStream out = new ByteArrayOutputStream();

	@Before
	public void beforeEach() {
	    System.setOut(new PrintStream(out));
	}

	@After
	public void afterEach() {
	    System.setOut(null);
	}
	
	@Test
	public void testCustomerCloseness() {
		CustomerFinder cf = new CustomerFinder();
		Customer closeCustomer = new Customer();
		closeCustomer.setLatitude(53.997985);
		closeCustomer.setLongitude(-6.405630);

		Customer distantCustomer = new Customer();
		distantCustomer.setLatitude(54.606895);
		distantCustomer.setLongitude(-5.909750);

		Customer veryCloseCustomer = new Customer();
		veryCloseCustomer.setLatitude(53.3393);
		veryCloseCustomer.setLongitude(-6.2576841);

		Customer veryDistantCustomer = new Customer();
		veryDistantCustomer.setLatitude(39.880775);
		veryDistantCustomer.setLongitude(32.801394);
		
		assertTrue(cf.isCustomerClose(closeCustomer));
		assertTrue(cf.isCustomerClose(veryCloseCustomer));
		assertFalse(cf.isCustomerClose(distantCustomer));
		assertFalse(cf.isCustomerClose(veryDistantCustomer));
	}
	
	@Test
	public void noCustomersNearby() {
		CustomerFinder cf = new CustomerFinder();
		
		cf.inviteCustomers();
		
		assertEquals("No customers nearby!\r\n", out.toString());
	}
	
	@Test
	public void closeCustomers() {
		CustomerFinder cf = new CustomerFinder();

		Customer customer1 = new Customer();
		customer1.setName("Test1");
		customer1.setUserId(2);

		Customer customer2 = new Customer();
		customer2.setName("Test2");
		customer2.setUserId(1);
		
		cf.customersToBeInvited.add(customer1);
		cf.customersToBeInvited.add(customer2);
		
		cf.inviteCustomers();
		
		assertEquals("Test2 (id: 1)\r\nTest1 (id: 2)\r\n", out.toString());
	}
}
