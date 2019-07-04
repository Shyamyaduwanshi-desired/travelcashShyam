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

public class DeviceTokenPresenter {
    private Context context;
    private SaveDeviceToken saveToken;
    private AppData appData;

    public DeviceTokenPresenter(Context context, SaveDeviceToken saveToken) {
        this.context = context;
        this.saveToken = saveToken;
        appData = new AppData(context);
    }

    public interface SaveDeviceToken{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void SaveToken(final String devicetoken) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "saveUserDeviceToken", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        saveToken.success(reader.getString("message"));
                    }else if(status == 0){
                       saveToken.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    saveToken.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                saveToken.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("device_token", devicetoken);
                params.put("device_type", "android");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

 /*   public void signInThroughSocialMedia(final String username, final String email, final String token) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("saveToken Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "signInThroughSocialMediaUser", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        appData.setUserID(reader.getString("user_id"));
                        saveToken.success(reader.getString("message"));
                    }else if(status == 0){
                       saveToken.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    saveToken.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                saveToken.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("email_id", email);
                params.put("id", token);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }*/
}
