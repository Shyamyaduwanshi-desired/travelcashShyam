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

public class LoginPresenter {
    private Context context;
    private Login login;
    private AppData appData;

    public LoginPresenter(Context context, Login login) {
        this.context = context;
        this.login = login;
        appData = new AppData(context);
    }

    public interface Login{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void sentRequest(final String mobile, final String password) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Login Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "userLogIn", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        login.success(reader.getString("message"));
                        appData.setUserID(reader.getString("user_id"));
                        appData.setUsername(reader.getString("user_fname"));
                        appData.setMobile(reader.getString("user_mobile_number"));
                        appData.setEmail(reader.getString("user_email_id"));
                        appData.setPin(reader.getString("user_pin"));
                        appData.setProfile(reader.getString("profile"));
                        appData.setWalletAmount(reader.getString("wallet_amount"));
                    }else if(status == 0){
                       login.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    login.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                login.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", mobile);
                params.put("password", password);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void signInThroughSocialMedia(final String username, final String email, final String token) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Login Please Wait..");
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
                        login.success(reader.getString("message"));
                    }else if(status == 0){
                       login.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    login.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                login.fail("Server Error.\n Please try after some time.");
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
    }
}
