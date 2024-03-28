package com.augmentaa.sparkev.model.signup.charger_summry;

public class RequestGetSummary {
    public int userid;


    public RequestGetSummary(int userid, String serialno) {
        this.userid = userid;
        this.serialno = serialno;
    }

    public String serialno;

    public RequestGetSummary(int userid, String serialno, String mode) {
        this.userid = userid;
        this.serialno = serialno;
        this.mode = mode;
    }

    public String mode;

}
