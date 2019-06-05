package presenter;

import android.app.ProgressDialog;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import constant.AppData;
import view.activity.SignUpActivity;

public class SignupPresenter {
    private Context context;
    private SignUp signUp;

    public SignupPresenter(Context context, SignUp signUp) {
        this.context = context;
        this.signUp = signUp;
    }

    public interface SignUp{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void sentRequest(final String mobile) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
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
                        signUp.success("Your OTP is : " + reader.getString("otp"));
                    }else if(status == 0){
                       signUp.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    signUp.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                signUp.fail("Server Error.\n Please try after some time.");
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
}
