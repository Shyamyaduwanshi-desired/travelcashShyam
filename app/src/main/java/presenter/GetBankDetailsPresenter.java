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

public class GetBankDetailsPresenter {
    private Context context;
    private BankDetals money;
    private AppData appData;

    public GetBankDetailsPresenter(Context context, BankDetals money) {
        this.context = context;
        this.money = money;
        appData = new AppData(context);
    }

    public interface BankDetals{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void GetBankDetail() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.GET, AppData.url + "getAdminBankDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        money.success(reader.getString("bank_details"));
                    }else if(status == 0){
                        money.error("Something went wrong. Please try after some time.");
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
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }


}
