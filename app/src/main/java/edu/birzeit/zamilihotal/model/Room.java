package edu.birzeit.zamilihotal.model;

public class Room {
    private Hotel hotel;
    private int roomNo;

    public Room(Hotel hotel, int roomNo) {
        this.hotel = hotel;
        this.roomNo = roomNo;
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
