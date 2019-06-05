package presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import constant.AppData;
import constant.SignUpPreference;

public class ProfilePresenter {
    private Context context;
    private Profile profile;
    private AppData appData;

    public ProfilePresenter(Context context, Profile profile) {
        this.context = context;
        this.profile = profile;
        appData = new AppData(context);
    }

    public interface Profile{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void updateProfile() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Update Profile Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "updateUserProfile", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        SignUpPreference.getInstance(context).clear();
                        profile.success(reader.getString("message"));
                    }else if(status == 0){
                       profile.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    profile.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                profile.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("username", SignUpPreference.getInstance(context).getValue("username"));
                params.put("email_id", SignUpPreference.getInstance(context).getValue("email"));
                params.put("mobile_no", SignUpPreference.getInstance(context).getValue("mobile"));
                params.put("profile_picture", SignUpPreference.getInstance(context).getValue("image"));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void viewReview(final String userID) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showUserReviews", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        profile.success(response);
                    }else if(status == 0){
                       profile.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    profile.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                profile.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
