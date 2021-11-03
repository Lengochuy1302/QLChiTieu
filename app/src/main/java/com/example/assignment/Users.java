package com.example.assignment;

public class Users {
    private String userName, feedback, password, email, birthday, numberphone, linkhinhanh;
    public Users() {
    }

    public Users(String userName, String feedback, String password, String email, String birthday, String numberphone, String linkhinhanh) {
        this.userName = userName;
        this.feedback = feedback;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.numberphone = numberphone;
        this.linkhinhanh = linkhinhanh;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }

    public String getLinkhinhanh() {
        return linkhinhanh;
    }

    public void setLinkhinhanh(String linkhinhanh) {
        this.linkhinhanh = linkhinhanh;
    }
}
