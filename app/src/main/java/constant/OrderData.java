package constant;

import android.content.Context;
import android.content.SharedPreferences;

public class OrderData {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public OrderData(Context context){
        sharedPref = context.getSharedPreferences("order", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void agentID(String agentID) {
        editor.putString("agentID", agentID);
        editor.commit();
    }

    public void shopName(String shopName) {
        editor.putString("shopName", shopName);
        editor.commit();
    }

    public void shopAddress(String shopAddress) {
        editor.putString("shopAddress", shopAddress);
        editor.commit();
    }

    public void shopDistance(String shopDistance) {
        editor.putString("shopDistance", shopDistance);
        editor.commit();
    }

    public void shopLatitude(String shopLatitude) {
        editor.putString("shopLatitude", shopLatitude);
        editor.commit();
    }

    public void shopLongitude(String shopLongitude) {
        editor.putString("shopLongitude", shopLongitude);
        editor.commit();
    }

    public void requestAmount(String requestAmount) {
        editor.putString("requestAmount", requestAmount);
        editor.commit();
    }

    public void flagPurchase(int flagPurchase) {
        editor.putInt("flagPurchase", flagPurchase);
        editor.commit();
    }

    public void purchaseAmount(String purchaseAmount) {
        editor.putString("purchaseAmount", purchaseAmount);
        editor.commit();
    }

    public void flagPromos(int flagPromos) {
        editor.putInt("flagPromos", flagPromos);
        editor.commit();
    }

    public void promoDiscount(String promoDiscount) {
        editor.putString("promoDiscount", promoDiscount);
        editor.commit();
    }

    public String agentID() {
        return  sharedPref.getString("agentID", "");
    }

    public String shopName() {
        return  sharedPref.getString("shopName", "");
    }

    public String shopAddress() {
        return  sharedPref.getString("shopAddress", "");
    }

    public String shopDistance() {
        return  sharedPref.getString("shopDistance", "");
    }

    public String shopLatitude() {
        return  sharedPref.getString("shopLatitude", "");
    }

    public String shopLongitude() {
        return  sharedPref.getString("shopLongitude", "");
    }

    public String requestAmount() {
        return  sharedPref.getString("requestAmount", "");
    }

    public int flagPurchase() {
        return  sharedPref.getInt("flagPurchase", 0);
    }

    public String purchaseAmount() {
        return  sharedPref.getString("purchaseAmount", "");
    }

    public int flagPromos() {
        return  sharedPref.getInt("flagPromos", 0);
    }

    public String promoDiscount() {
        return  sharedPref.getString("promoDiscount", "");
    }

    public void clearData(){
        editor.clear();
        editor.commit();
    }
}
