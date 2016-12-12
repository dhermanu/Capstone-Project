package com.dhermanu.soulfull.model;

import org.parceler.Parcel;

/**
 * Created by dean on 9/29/16.
 */

@Parcel
public class Halal {
    String name;
    String id;
    String backgroundPath;
    String category;
    String phone;
    String phoneDisplay;
    String review;
    int rating;
    String address;
    String displayAddress;
    String neighbourhoods;
    String city;
    String zip_code;
    String state;
    double coord_lat;
    double coord_long;
    String distance;

    public Halal(){}

    public Halal(String name, String id){
        this.name = name;
        this.id = id;
    }

    /* setter and getter for id*/
    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    /* setter and getter for name*/
    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneDisplay() {
        return phoneDisplay;
    }

    public void setPhoneDisplay(String phoneDisplay) {
        this.phoneDisplay = phoneDisplay;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getCoord_lat() {
        return coord_lat;
    }

    public void setCoord_lat(double coord_lat) {
        this.coord_lat = coord_lat;
    }

    public double getCoord_long() {
        return coord_long;
    }

    public void setCoord_long(double coord_long) {
        this.coord_long = coord_long;
    }

    public String getDisplayAddress() {
        return displayAddress;
    }

    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }

    public String getNeighbourhoods() {
        return neighbourhoods;
    }

    public void setNeighbourhoods(String neighbourhoods) {
        this.neighbourhoods = neighbourhoods;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
