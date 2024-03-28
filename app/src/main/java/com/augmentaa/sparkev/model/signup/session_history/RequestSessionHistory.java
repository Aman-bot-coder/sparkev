package com.augmentaa.sparkev.model.signup.session_history;

public class RequestSessionHistory {


    public RequestSessionHistory(int userid, String state_date, String end_date) {
        this.userid = userid;
        this.state_date = state_date;
        this.end_date = end_date;
    }

    public int userid;
    public String state_date;
    public String end_date;

    public RequestSessionHistory(int userid, String state_date, String end_date, String mode) {
        this.userid = userid;
        this.state_date = state_date;
        this.end_date = end_date;
        this.mode = mode;
    }

    public String mode;

}
