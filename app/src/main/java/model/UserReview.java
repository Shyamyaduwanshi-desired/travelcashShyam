package model;

public class UserReview {
    private String name, date, rating, message;

    public UserReview(String name, String date, String rating, String message) {
        this.name = name;
        this.date = date;
        this.rating = rating;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getRating() {
        return rating;
    }

    public String getMessage() {
        return message;
    }
}
