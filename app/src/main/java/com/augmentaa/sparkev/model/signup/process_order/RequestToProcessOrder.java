package com.augmentaa.sparkev.model.signup.process_order;

public class RequestToProcessOrder {
   public String charger_transaction_id;

    public RequestToProcessOrder(String charger_transaction_id) {
        this.charger_transaction_id = charger_transaction_id;
    }
}
