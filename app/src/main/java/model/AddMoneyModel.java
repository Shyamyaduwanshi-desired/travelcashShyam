package model;

public class AddMoneyModel {
    private String amount, bank_name, acc_num, bank_txnID, payment_tnxID, payment_status, date_time;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAcc_num() {
        return acc_num;
    }

    public void setAcc_num(String acc_num) {
        this.acc_num = acc_num;
    }

    public String getBank_txnID() {
        return bank_txnID;
    }

    public void setBank_txnID(String bank_txnID) {
        this.bank_txnID = bank_txnID;
    }

    public String getPayment_tnxID() {
        return payment_tnxID;
    }

    public void setPayment_tnxID(String payment_tnxID) {
        this.payment_tnxID = payment_tnxID;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
