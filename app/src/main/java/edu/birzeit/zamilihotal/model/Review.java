package edu.birzeit.zamilihotal.model;

public class Review {

    private User user;
    private String review;
    private double rate;


    public Review(User user, String review, double rate) {
        this.user = user;
        this.review = review;
        this.rate = rate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
