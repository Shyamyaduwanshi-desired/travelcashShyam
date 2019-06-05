package model;

public class Vendor {
    private String shopName;
    private String shopImage;
    private String shopAddress;
    private String vendorID;
    private String isPurchase;
    private String isPromo;
    private String distance;
    private String latitude;
    private String longitude;
    private String promosDiscount;
    private String purchaseLimit;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPromosDiscount() {
        return promosDiscount;
    }

    public void setPromosDiscount(String promosDiscount) {
        this.promosDiscount = promosDiscount;
    }

    public String getPurchaseLimit() {
        return purchaseLimit;
    }

    public void setPurchaseLimit(String purchaseLimit) {
        this.purchaseLimit = purchaseLimit;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getIsPurchase() {
        return isPurchase;
    }

    public void setIsPurchase(String isPurchase) {
        this.isPurchase = isPurchase;
    }

    public String getIsPromo() {
        return isPromo;
    }

    public void setIsPromo(String isPromo) {
        this.isPromo = isPromo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
