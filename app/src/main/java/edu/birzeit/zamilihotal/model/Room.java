package edu.birzeit.zamilihotal.model;

import java.util.List;

public class Room {
    private Hotel hotel;
    private int roomNo;
    private double rate;
    private String roomType;
    private List<String> images;

    public Room(Hotel hotel, int roomNo, String roomType, List<String> images) {
        this.hotel = hotel;
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.images = images;
        rate = 5;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }
}
