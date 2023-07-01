package edu.birzeit.zamilihotal.model;

public class User {

    private String Email;
    private String password;
    private String F_Name;
    private String L_Name;
    private String Phone;


    public User(String email, String password, String f_Name, String l_Name, String phone_number) {
        this.Email = email;
        this.password = password;
        F_Name = f_Name;
        L_Name = l_Name;
        this.Phone = phone_number;
    }

    public User(String email, String f_Name, String l_Name, String phone_number) {
        this.Email = email;
        this.password = password;
        F_Name = f_Name;
        L_Name = l_Name;
        this.Phone = phone_number;
    }

    public String getEmail() {
        return Email;
    }

    public String getF_Name() {
        return F_Name;
    }

    public void setF_Name(String f_Name) {
        F_Name = f_Name;
    }

    public String getL_Name() {
        return L_Name;
    }

    public void setL_Name(String l_Name) {
        L_Name = l_Name;
    }

    public String getPhone_number() {
        return Phone;
    }

    public void setPhone_number(String phone_number) {
        this.Phone = phone_number;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + Email + '\'' +
                ", password='" + password + '\'' +
                ", F_Name='" + F_Name + '\'' +
                ", L_Name='" + L_Name + '\'' +
                ", phone_number='" + Phone + '\'' +
                '}';
    }
}
