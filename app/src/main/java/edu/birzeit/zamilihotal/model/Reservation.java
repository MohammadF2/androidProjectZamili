package edu.birzeit.zamilihotal.model;

public class Reservation {
    private int roomId;
    private String userEmail;
    private String reservationDate;

    private String isRated;

    public Reservation(int roomNo, String userEmail, String date) {
        this.roomId = roomNo;
        this.userEmail = userEmail;
        this.reservationDate = date;
    }

    public Reservation(int roomNo, String userEmail, String date, String isRated) {
        this.roomId = roomNo;
        this.userEmail = userEmail;
        this.reservationDate = date;
        this.isRated = isRated;
    }

    public int getRoomNo() {
        return roomId;
    }

    public void setRoomNo(int roomNo) {
        this.roomId = roomNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDate() {
        return reservationDate;
    }

    public void setDate(String date) {
        this.reservationDate = date;
    }
}
