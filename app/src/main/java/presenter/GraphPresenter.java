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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import constant.AppData;
import model.GraphModel;
import model.HistoryModel;

public class GraphPresenter {
    private Context context;
    private GraphInfo graphInfo;
    private AppData appData;

    public GraphPresenter(Context context, GraphInfo graphInfo) {
        this.context = context;
        this.graphInfo = graphInfo;
        appData = new AppData(context);
    }

    public interface GraphInfo {
        void success(ArrayList<GraphModel> response);

        void error(String response);

        void fail(String response);
    }

    public void getGraphInfo() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<GraphModel> list = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.GET, AppData.url + "getLast5daysRates", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        list.clear();
                        JSONArray jsArrayMain = reader.getJSONArray("data");
                        GraphModel bean;
                        for (int count = 0; count < jsArrayMain.length(); count++) {
                            bean=new GraphModel();
                            JSONObject object = jsArrayMain.getJSONObject(count);
                            String date = object.getString("date");
                            String rate = object.getString("rate");
                            rate=rate.replaceAll(",","");
//                            rate=(Math.round(rate));

                            bean.setDate(date);
                            bean.setRate(rate);
                            list.add(bean);
                        }
                        graphInfo.success(list);
                    } else if (status == 0) {
                        graphInfo.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    graphInfo.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                graphInfo.fail("Server Error.\n Please try after some time.");
            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
