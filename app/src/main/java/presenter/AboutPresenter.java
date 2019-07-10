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

import java.util.ArrayList;

import constant.AppData;
import model.GraphModel;
import model.aboutBean;

public class AboutPresenter {
    private Context context;
    private AboutInfo graphInfo;
    private AppData appData;

    public AboutPresenter(Context context, AboutInfo graphInfo) {
        this.context = context;
        this.graphInfo = graphInfo;
        appData = new AppData(context);
    }

    public interface AboutInfo {
        void success(ArrayList<aboutBean> response);

        void error(String response);

        void fail(String response);
    }

    public void getAboutInfo() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<aboutBean> list = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.GET, AppData.url + "getAboutPageData", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress!=null)
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        list.clear();
                        JSONObject jsObjMain = reader.getJSONObject("aboutData");
                        aboutBean bean;
                        if(jsObjMain!=null)
                        {
                            String static_page_title = jsObjMain.getString("static_page_title");
                            String short_description = jsObjMain.getString("short_description");
                            String description = jsObjMain.getString("description");
                            String banner_image = jsObjMain.getString("banner_image");
                            String imageUrl = jsObjMain.getString("imageUrl");


                            bean=new aboutBean();
                            bean.setStatic_page_title(static_page_title);
                            bean.setShort_description(short_description);
                            bean.setDescription(description);
                            bean.setBanner_image(banner_image);
                            bean.setImageUrl(imageUrl);

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
                if(progress!=null)
                progress.dismiss();
                graphInfo.fail("Server Error.\n Please try after some time.");
            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void getHelpInfo() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<aboutBean> list = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.GET, AppData.url + "getHelpPageData", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        list.clear();
                        JSONObject jsObjMain = reader.getJSONObject("helpData");
                        aboutBean bean;
                        if(jsObjMain!=null)
                        {
                            String static_page_title = jsObjMain.getString("static_page_title");
                            String short_description = jsObjMain.getString("short_description");
                            String description = jsObjMain.getString("description");
                            String banner_image = jsObjMain.getString("banner_image");
                            String imageUrl = jsObjMain.getString("imageUrl");


                            bean=new aboutBean();
                            bean.setStatic_page_title(static_page_title);
                            bean.setShort_description(short_description);
                            bean.setDescription(description);
                            bean.setBanner_image(banner_image);
                            bean.setImageUrl(imageUrl);

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

    public void getPrivacyInfo() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<aboutBean> list = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.GET, AppData.url + "getPrivacyPolicyPageData", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        list.clear();
                        JSONObject jsObjMain = reader.getJSONObject("privacyPolicyData");
                        aboutBean bean;
                        if(jsObjMain!=null)
                        {
                            String static_page_title = jsObjMain.getString("static_page_title");
                            String short_description = jsObjMain.getString("short_description");
                            String description = jsObjMain.getString("description");
                            String banner_image = jsObjMain.getString("banner_image");
                            String imageUrl = jsObjMain.getString("imageUrl");


                            bean=new aboutBean();
                            bean.setStatic_page_title(static_page_title);
                            bean.setShort_description(short_description);
                            bean.setDescription(description);
                            bean.setBanner_image(banner_image);
                            bean.setImageUrl(imageUrl);

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
