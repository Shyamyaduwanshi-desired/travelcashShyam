package model;

public class HistoryModel {
    private String date;
    private String mode;
    private String time;
    private String amount;
    private String id;

    public HistoryModel(String id, String date, String mode, String time, String amount) {
        this.id = id;
        this.date = date;
        this.mode = mode;
        this.time = time;
        this.amount = amount;
    }

    public String getID() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getMode() {
        return mode;
    }

    public String getTime() {
        return time;
    }

    public String getAmount() {
        return amount;
    }
}
