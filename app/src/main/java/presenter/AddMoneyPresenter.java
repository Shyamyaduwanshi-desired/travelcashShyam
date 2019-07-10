package presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.travelcash.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import constant.AppData;
import model.AddMoneyModel;
import view.activity.AddMoney;

public class AddMoneyPresenter {
    private Context context;
    private Money money;
    private AppData appData;

    public AddMoneyPresenter(Context context, Money money) {
        this.context = context;
        this.money = money;
        appData = new AppData(context);
    }

    public interface Money{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void proceedPayment(final AddMoneyModel model) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "userTopUp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress!=null)
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
                if(progress!=null)
                progress.dismiss();
                money.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("topUpAmount", model.getAmount());
                params.put("bank_name", model.getBank_name());
                params.put("ac_no", model.getAcc_num());
                params.put("bank_transaction_id", model.getBank_txnID());
                params.put("payment_transaction_id", model.getPayment_tnxID());
                params.put("transaction_status", model.getPayment_status());
                params.put("dateTime", model.getDate_time());
                Log.e("",""+params.toString());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }


}
