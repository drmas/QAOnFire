package com.codly.firebasefcm;

/**
 * Created by User on 6/1/16.
 */

public class Comment {

    private String comment;
    private String date;
    private String user;

    public Comment(){}

    public Comment(String date, String user, String comment) {

        this.date = date;
        this.user = user;
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }
}
