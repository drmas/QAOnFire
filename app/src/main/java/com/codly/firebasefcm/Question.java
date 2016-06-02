package com.codly.firebasefcm;

/**
 * Created by User on 6/1/16.
 */

public class Question {

    private String date;
    private String user;
    private String question;
    private Boolean answered;
    private String imageUrl;

    public  Question(){}

    public Question(String date, String user, String question, Boolean answered, String imageUrl) {

        this.date = date;
        this.user = user;
        this.question = question;
        this.answered = answered;
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public String getQuestion() {
        return question;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
