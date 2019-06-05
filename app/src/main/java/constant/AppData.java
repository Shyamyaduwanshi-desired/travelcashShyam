package constant;

import android.content.Context;
import android.content.SharedPreferences;

public class AppData {
    public static String url = "https://www.omsoftware.org/cashapp/api/";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    public static String image = "";
    public static String agentID = "";
    public static final String API_KEY = "c26b952cf256107329b42f97bd9af382";

    public AppData(Context context) {
        sharedPref = context.getSharedPreferences("loginPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setUserID(String userID) {
        editor.putString("userID", userID);
        editor.commit();
    }

    public void setMobile(String mobile) {
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public void setEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    public void setUsername(String username) {
        editor.putString("username", username);
        editor.commit();
    }

    public void setPin(String pin) {
        editor.putString("pin", pin);
        editor.commit();
    }

    public void setProfile(String profile) {
        editor.putString("profile", profile);
        editor.commit();
    }

    public void setGoogleID(String id) {
        editor.putString("id", id);
        editor.commit();
    }

    public void setFbID(String fbID) {
        editor.putString("fbID", fbID);
        editor.commit();
    }

    public String getUserID() {
        return  sharedPref.getString("userID", "NA");
    }

    public String getMobile() {
        return sharedPref.getString("mobile", "NA");
    }

    public String getEmail() {
        return sharedPref.getString("email", "NA");
    }

    public String getUsername() {
        return sharedPref.getString("username", "NA");
    }

    public String getPin() {
        return sharedPref.getString("pin", "NA");
    }

    public String getProfile() {
        return sharedPref.getString("profile", "NA");
    }

    public String getGoogleID() {
        return sharedPref.getString("id", "NA");
    }

    public String getFbID() {
        return sharedPref.getString("fbID", "NA");
    }

    public void clearData(){
        editor.clear();
        editor.commit();
    }
}
