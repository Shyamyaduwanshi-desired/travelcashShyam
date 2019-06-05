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

public class ForgotPasswordPresenter {
    private Context context;
    private Otp otp;

    public ForgotPasswordPresenter(Context context, Otp otp) {
        this.context = context;
        this.otp = otp;
    }

    public interface Otp{
        void success(String response);
        void error(String response);
        void fail(String response);
        void verifyOTP(String response);
    }

    public void sentOtp(final String mobile) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Verifying number please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "sendOtpWhenForgotPassword", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        otp.success("Your OTP is : " + reader.getString("otp"));
                    }else if(status == 0){
                       otp.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    otp.fail("Something went wrong.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                otp.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile_no", mobile);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void verifyOtp(final String mobile, final String otpString) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Verifying OTP Please Wait..");
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
                        otp.verifyOTP(reader.getString("message"));
                    }else if(status == 0){
                       otp.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    otp.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                otp.fail("Server Error.\n Please try after some time.");
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

    public void setPassword(final String mobile, final String password) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "resetPassword", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        otp.success(reader.getString("message"));
                    }else if(status == 0){
                       otp.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    otp.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                otp.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("new_password", password);
                params.put("mobile_no", mobile);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
