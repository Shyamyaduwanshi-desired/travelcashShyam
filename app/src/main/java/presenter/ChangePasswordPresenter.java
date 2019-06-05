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

public class ChangePasswordPresenter {
    private Context context;
    private PasswordChange passwordChange;

    public ChangePasswordPresenter(Context context, PasswordChange passwordChange) {
        this.context = context;
        this.passwordChange = passwordChange;
    }

    public interface PasswordChange{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void sentRequest(final String userID, final String oldPass, final String newPass) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Change Password Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "changePassword", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        passwordChange.success(reader.getString("message"));
                    }else if(status == 0){
                       passwordChange.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    passwordChange.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                passwordChange.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userID);
                params.put("oldPassword", oldPass);
                params.put("newPassword", newPass);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
