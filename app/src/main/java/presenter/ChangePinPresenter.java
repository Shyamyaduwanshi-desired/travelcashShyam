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

public class ChangePinPresenter {
    private Context context;
    private Pin pin;
    private AppData appData;

    public ChangePinPresenter(Context context, Pin pin) {
        this.context = context;
        this.pin = pin;
        appData = new AppData(context);
    }

    public interface Pin{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void changePin(final String oldPin, final String newPin) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Change Pin Please Wait..");//Change Pin
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "userChangePin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        pin.success(reader.getString("message"));
                    }else if(status == 0){
                        pin.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pin.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                pin.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("oldPin", oldPin);
                params.put("newPin", newPin);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
