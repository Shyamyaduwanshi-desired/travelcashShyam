package constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.travelcash.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class AppData {
    public static String url = "https://www.omsoftware.org/cashapp/api/";
    public static String noti_url = "https://fcm.googleapis.com/fcm/send";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    public static String image = "";
    public static String agentID = "";
    public static final String API_KEY = "c26b952cf256107329b42f97bd9af382";
//    public static final String API_KEY = "AIzaSyDzdTN8o1eYFEnCOF080AA7LN6GxaH-VLc";

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



    public void setWalletAmount(String walletAmount) {
        editor.putString("walletAmount", walletAmount);
        editor.commit();
    }

    public String getWalletAmount() {
        return  sharedPref.getString("walletAmount", "0");
    }

    public void setReferalCode(String referalcode) {
        editor.putString("referal_code", referalcode);
        editor.commit();
    }

    public String getReferalCode() {
        return  sharedPref.getString("referal_code", "0");
    }



    public void clearData(){
        editor.clear();
        editor.commit();
    }
    PrettyDialog prettyDialog=null;
    private void ShowNewAlert(Context context,String message) {
        if(prettyDialog!=null)
        {
            prettyDialog.dismiss();
        }
        prettyDialog = new PrettyDialog(context);
        prettyDialog.setCanceledOnTouchOutside(false);
        TextView title = (TextView) prettyDialog.findViewById(libs.mjn.prettydialog.R.id.tv_title);
        TextView tvmessage = (TextView) prettyDialog.findViewById(libs.mjn.prettydialog.R.id.tv_message);
        title.setTextSize(15);
        tvmessage.setTextSize(15);
        prettyDialog.setIconTint(R.color.colorPrimary);
        prettyDialog.setIcon(R.drawable.pdlg_icon_info);
        prettyDialog.setTitle("");
        prettyDialog.setMessage(message);
        prettyDialog.setAnimationEnabled(false);
        prettyDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        prettyDialog.addButton("Cancel", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();

            }
        }).show();

        prettyDialog.addButton("Search again", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();

            }
        }).show();
    }
//2019-06-18 11:17:55 to 18 jun 2019 11:17:55
    public String ConvertDate(String indate)
    {
        String formattedDate = null;
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss aa");
            Date date = originalFormat.parse(indate);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

//    //2019-06-18 11:17:55 to 18/06/2019
    public String ConvertDate1(String indate)
    {
        String formattedDate = null;
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = originalFormat.parse(indate);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
    //2019-06-18 11:17:55 to June 18 2019
    public String ConvertDate2(String indate)
    {
        String formattedDate = null;
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMMM dd  yyyy");
            Date date = originalFormat.parse(indate);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
    //    //2019-06-18  to  18 June
    public String ConvertDate3(String indate)
    {
        String formattedDate = null;
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd MMMM");
            Date date = originalFormat.parse(indate);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
    //    //2019-06-18  to  18 June 2019
    public String ConvertDate4(String indate)
    {
        String formattedDate = null;
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = originalFormat.parse(indate);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

//   2019-06-18 11:17:55 to 11:17
    public String ConvertTime(String indate)
    {
        String shortTimeStr="";
        try {
            SimpleDateFormat toFullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fullDate = toFullDate.parse(indate);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
             shortTimeStr = sdf.format(fullDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*String formattedDate = null;
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//            DateFormat targetFormat = new SimpleDateFormat("hh:mm:ss aa");
            DateFormat targetFormat = new SimpleDateFormat("HH:mm");
//            DateFormat targetFormat = new SimpleDateFormat("hh:mm:ss");
            Date date = originalFormat.parse(indate);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        return shortTimeStr;
    }

    public void setNotiClick(String setNotivalue) {
        editor.putString("set_noti", setNotivalue);
        editor.commit();
    }

    public String getNotiClick() {

        return  sharedPref.getString("set_noti", "0");
    }


}
