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
import model.UserReview;

public class VendorReviewPresenter {
    private Context context;
    private Review review;
    private ArrayList<UserReview> userReviews;

    public VendorReviewPresenter(Context context, Review review) {
        this.context = context;
        this.review = review;
        userReviews = new ArrayList<>();
    }

    public interface Review{
        void success(String agentFname, String agentImage, String ratingAvrg, String reviewCount, ArrayList<UserReview> userReviews);
        void error(String response);
        void fail(String response);
    }

    public void getReview(final String agent_id) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Gating Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showAgentReviews", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        userReviews.clear();
                        JSONArray jsonArray = reader.getJSONArray("agentRecievedReviews");
                        for (int count = 0 ; count<jsonArray.length(); count++){
                            JSONObject jsonObject = jsonArray.getJSONObject(count);
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            String date = jsonObject.getString("review_date_time");
                            Date date_change = input.parse(date);
                            UserReview review = new UserReview(jsonObject.getString("user_fname"), output.format(date_change),
                                        jsonObject.getString("rating"), jsonObject.getString("review"));
                            userReviews.add(review);
                        }

                        review.success(reader.getString("agentFname"), reader.getString("agentImage"), reader.getString("ratingAvrg"), reader.getString("reviewCount"),userReviews);
                    }else if(status == 0){
                       review.error(reader.getString("message"));
                    }
                }catch (ParseException ex) {
                    ex.printStackTrace();
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
                params.put("agent_id", agent_id);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
