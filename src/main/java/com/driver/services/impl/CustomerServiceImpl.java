package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer=customerRepository2.findById(customerId).get();
		List<TripBooking>bookedTrip=customer.getTripBookingList();



	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query


		List<Driver> driverList=driverRepository2.findAll();
		Driver driver = null;
		for (Driver currentDrivr:driverList){
			if(currentDrivr.getCab().getAvailable()){
				currentDrivr.getCab().setAvailable(false);
				driver=currentDrivr;
				break;
			}
		}

		if(driver==null) throw new Exception("No cab available!");

		Customer customer = customerRepository2.findById(customerId).get();
		TripBooking tripBooking = new TripBooking();
		tripBooking.setCustomer(customer);
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setDriver(driver);
		tripBooking.setStatus(TripStatus.CONFIRMED);
//		tripBookingRepository2.save(tripBooking);
		customer.getTripBookingList().add(tripBooking);
		driver.getTripBookingList().add(tripBooking);
		customerRepository2.save(customer);
		driverRepository2.save(driver);

		return tripBooking;


	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking oldtripBooking=tripBookingRepository2.findById(tripId).get();
		oldtripBooking.setStatus(TripStatus.CANCELED);
		oldtripBooking.setBill(0);
		oldtripBooking.getDriver().getCab().setAvailable(true);
		tripBookingRepository2.save(oldtripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking oldtripBooking=tripBookingRepository2.findById(tripId).get();
		oldtripBooking.setStatus(TripStatus.COMPLETED);
		oldtripBooking.getDriver().getCab().setAvailable(true);
		tripBookingRepository2.save(oldtripBooking);

	}
}
