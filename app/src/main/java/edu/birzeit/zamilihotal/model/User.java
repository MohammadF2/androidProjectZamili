package edu.birzeit.zamilihotal.model;

public class User {
    private String email;
    private String password;
    private String f_Name;
    private String l_Name;
    private String phone_number;

    public User(String email, String password, String f_Name, String l_Name, String phone_number) {
        this.email = email;
        this.password = password;
        this.f_Name = f_Name;
        this.l_Name = l_Name;
        this.phone_number = phone_number;
    }

    public User(String email, String f_Name, String l_Name, String phone_number) {
        this.email = email;
        this.f_Name = f_Name;
        this.l_Name = l_Name;
        this.phone_number = phone_number;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getF_Name() {
        return f_Name;
    }

    public void setF_Name(String f_Name) {
        this.f_Name = f_Name;
    }

    public String getL_Name() {
        return l_Name;
    }

    public void setL_Name(String l_Name) {
        this.l_Name = l_Name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", f_Name='" + f_Name + '\'' +
                ", l_Name='" + l_Name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                '}';
    }
}