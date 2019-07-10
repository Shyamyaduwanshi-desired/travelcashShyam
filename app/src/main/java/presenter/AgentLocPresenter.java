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

public class AgentLocPresenter {
    private Context context;
    private ShowAgentLoc saveToken;
    private AppData appData;

    public AgentLocPresenter(Context context, ShowAgentLoc saveToken) {
        this.context = context;
        this.saveToken = saveToken;
        appData = new AppData(context);
    }

    public interface ShowAgentLoc{
        void success(String response, String lati, String longi);
        void error(String response, String check);
        void fail(String response, String check);
    }

    public void GetAgentLoc(final String agentid) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "getAgentLatLong", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        Log.e("","agent loc lati= "+reader.getString("latitude")+"agent loc longi= "+reader.getString("longitude"));
                        saveToken.success("success",reader.getString("latitude"),reader.getString("longitude"));
                    }else if(status == 0){
                       saveToken.error(reader.getString("message"),"not found agent data");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    saveToken.fail("Something went wrong. Please try after some time.","token");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                saveToken.fail("Server Error.\n Please try after some time.","token");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("agent_id", agentid);
                Log.e("",""+params.toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
