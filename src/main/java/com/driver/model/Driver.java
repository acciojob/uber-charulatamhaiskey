package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "driver")
public
class Driver{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int DriverId;

    private String mobile;
    private String password;


   //Cab Mapping---(one driver-one cab)(parent)
    @OneToOne(mappedBy = "driver",cascade = CascadeType.ALL)
    Cab cab;


    //TripBooking Mapping (everyTripBooking has one particular driver and
    // one driver can have multiple trip booking)(child)
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    List<TripBooking> tripBookingList=new ArrayList<>();

    public Driver() {
    }


    public int getDriverId() {
        return DriverId;
    }

    public void setDriverId(int driverId) {
        DriverId = driverId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public List<TripBooking> getTripBookingList() {
        return tripBookingList;
    }

    public void setTripBookingList(List<TripBooking> tripBookingList) {
        this.tripBookingList = tripBookingList;
    }
}