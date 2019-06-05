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

public class OtpPresenter {
    private Context context;
    private Otp otp;

    public OtpPresenter(Context context, Otp otp) {
        this.context = context;
        this.otp = otp;
    }

    public interface Otp{
        void success(String response, String type);
        void success(String response);
        void error(String response, String type);
        void fail(String response, String type);
    }

    public void resentOtp(final String mobile) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Resend OTP please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "resendOtp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        otp.success("OTP is : " + reader.getString("otp"));
                    }else if(status == 0){
                       otp.error(reader.getString("message"), "otp");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    otp.fail("Something went wrong.", "otp");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                otp.fail("Server Error.\n Please try after some time.", "otp");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_contact_number", mobile);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void verifyOtp(final String mobile, final String otpString) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Sending please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "verifyOtp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        otp.success(reader.getString("message"), "otp");
                    }else if(status == 0){
                       otp.error(reader.getString("message"), "otp");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    otp.fail("Something went wrong. Please try after some time.", "otp");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                otp.fail("Server Error.\n Please try after some time.", "otp");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", otpString);
                params.put("user_contact_number", mobile);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void register() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "registration", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        otp.success(reader.getString("message"), "register");
                    }else if(status == 0){
                        otp.error(reader.getString("message"), "register");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    otp.fail("Something went wrong. Please try after some time.", "register");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                otp.fail("Server Error.\n Please try after some time.", "register");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", SignUpPreference.getInstance(context).getValue("username"));
                params.put("password", SignUpPreference.getInstance(context).getValue("password"));
                params.put("email", SignUpPreference.getInstance(context).getValue("email"));
                params.put("mobileno", SignUpPreference.getInstance(context).getValue("mobile"));
                params.put("pin", SignUpPreference.getInstance(context).getValue("pin"));
                params.put("profile_picture", SignUpPreference.getInstance(context).getValue("picture"));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
