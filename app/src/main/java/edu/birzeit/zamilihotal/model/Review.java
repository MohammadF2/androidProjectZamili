package edu.birzeit.zamilihotal.model;

public class Review {
    private String userEmail;

    private int roomNo;
    private String review;
    private double rate;

    public Review(String userEmail, int roomNo, String review, double rate) {
        this.userEmail = userEmail;
        this.roomNo = roomNo;
        this.review = review;
        this.rate = rate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
