package presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

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
import model.AddMoneyModel;

public class CancelOrderPresenter {
    private Context context;
    private CancelInfo money;
    private AppData appData;

    public CancelOrderPresenter(Context context, CancelInfo money) {
        this.context = context;
        this.money = money;
        appData = new AppData(context);
    }

    public interface CancelInfo{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void CancelOrderMethod(String requestId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "cancelRequestByUser", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        money.success(reader.getString("message"));
                    }else if(status == 0){
                        money.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    money.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                money.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("agent_recieved_request_id", requestId);
                Log.e("",""+params.toString());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }


}
