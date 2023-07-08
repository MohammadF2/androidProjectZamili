package edu.birzeit.zamilihotal.model;

public class Reservation {
    private int roomId;
    private String userEmail;
    private String reservationDate;
    private int numberOfDays;
    private int ReservationNum;
    private String isRated;

    public Reservation(int roomNo, String userEmail, String date) {
        this.roomId = roomNo;
        this.userEmail = userEmail;
        this.reservationDate = date;
    }

    public Reservation(int roomNo, String userEmail, String reservationDate , int ReservationNum,  int numberOfDays, String isRated) {
        this.roomId = roomNo;
        this.userEmail = userEmail;
        this.reservationDate = reservationDate;
        this.isRated = isRated;
        this.ReservationNum = ReservationNum;
        this.numberOfDays = numberOfDays;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public int getReservationNum() {
        return ReservationNum;
    }
    public String getIsRated() {
        return isRated;
    }
    public int getRoomNo() {
        return roomId;
    }

    public String getDate() {
        return reservationDate;
    }


    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public void setReservationNum(int reservationNum) {
        ReservationNum = reservationNum;
    }

    public void setIsRated(String isRated) {
        this.isRated = isRated;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "roomId=" + roomId +
                ", userEmail='" + userEmail + '\'' +
                ", reservationDate='" + reservationDate + '\'' +
                ", numberOfDays=" + numberOfDays +
                ", ReservationNum=" + ReservationNum +
                ", isRated='" + isRated + '\'' +
                '}';
    }
}
