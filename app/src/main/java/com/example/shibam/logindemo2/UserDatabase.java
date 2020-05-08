package com.example.shibam.logindemo2;

public class UserDatabase {
    public String Email;
    public String Mobile;
    public String Name;
    public String clg;
    public String Roll;

    public UserDatabase(String userEmail, String userMobile, String userName, String userclg, String userRoll) {
        this.Email = userEmail;
        this.Mobile = userMobile;
        this.Name = userName;
        this.clg = userclg;
        this.Roll = userRoll;
    }
}
