package com.augmentaa.sparkev.model.signup.payment;

public class OnlinePaymentResponse {
    @Override
    public String toString() {
        return "OnlinePaymentResponse{" +
                "BANKNAME='" + BANKNAME + '\'' +
                ", BANKTXNID='" + BANKTXNID + '\'' +
                ", CHECKSUMHASH='" + CHECKSUMHASH + '\'' +
                ", CURRENCY='" + CURRENCY + '\'' +
                ", GATEWAYNAME='" + GATEWAYNAME + '\'' +
                ", ORDERID='" + ORDERID + '\'' +
                ", PAYMENTMODE='" + PAYMENTMODE + '\'' +
                ", RESPCODE='" + RESPCODE + '\'' +
                ", RESPMSG='" + RESPMSG + '\'' +
                ", STATUS='" + STATUS + '\'' +
                ", TXNAMOUNT='" + TXNAMOUNT + '\'' +
                ", TXNDATE='" + TXNDATE + '\'' +
                ", TXNID='" + TXNID + '\'' +
                '}';
    }

    public String BANKNAME, BANKTXNID, CHECKSUMHASH, CURRENCY,
            GATEWAYNAME, ORDERID, PAYMENTMODE, RESPCODE, RESPMSG, STATUS, TXNAMOUNT,
            TXNDATE, TXNID;

}
