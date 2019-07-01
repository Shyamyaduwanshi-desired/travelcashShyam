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
import model.SubmitTopup;

public class SubmitTopupPresenter {
    private Context context;
    private SubmitTopup topup;
    private AppData appData;

    public SubmitTopupPresenter(Context context, SubmitTopup topup) {
        this.context = context;
        this.topup = topup;
        appData = new AppData(context);
    }

    public interface SubmitTopup{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void SubmitTopupRequest(final model.SubmitTopup model) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
//        https://omsoftware.org/cashapp/api/submitTopUpRequest
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "submitTopUpRequest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        topup.success(reader.getString("message")+"\r\n"+reader.getString("transaction_id"));
                    }else if(status == 0){
                        topup.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    topup.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                topup.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("amount", model.getAmount());
                params.put("extension", model.getExtentionfile());

                if(model.getExtentionfile().equals("pdf")||model.getExtentionfile().equals("PDF"))
                {
                    params.put("pdf", model.getPdfExtenstion());
                }
                else {
                    params.put("proof_file", model.getFileurl());
                }



                Log.e("",""+params.toString());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }


}
