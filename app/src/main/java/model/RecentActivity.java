package model;

public class RecentActivity {
    private String name, date, amount, flag, id;

    public RecentActivity(String id, String name,String date, String amount, String flag) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.flag = flag;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getFlag() {
        return flag;
    }
}
