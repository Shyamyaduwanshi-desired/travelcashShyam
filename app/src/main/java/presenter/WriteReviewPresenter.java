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

public class WriteReviewPresenter {
    private Context context;
    private Review review;
    private AppData appData;

    public WriteReviewPresenter(Context context, Review review) {
        this.context = context;
        this.review = review;
        appData = new AppData(context);
    }

    public interface Review{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void sendReview(final String agent_id, final String star_count, final String reviewText) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Sending Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "writeReviewAgainstAgent", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        review.success(reader.getString("message"));
                    }else if(status == 0){
                       review.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    review.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                review.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("agent_id", agent_id);
                params.put("star_count", star_count);
                params.put("review", reviewText);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
