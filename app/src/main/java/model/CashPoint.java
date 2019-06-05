package model;

public class CashPoint {
    private int image;
    private String name, location, address;

    public CashPoint(int image, String name, String location, String address) {
        this.image = image;
        this.name = name;
        this.location = location;
        this.address = address;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }
}
