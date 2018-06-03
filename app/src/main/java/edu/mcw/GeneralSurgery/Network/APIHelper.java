package edu.mcw.GeneralSurgery.Network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.Utilities.TimestampUtils;
import edu.mcw.GeneralSurgery.dagger.MainApplication;
import edu.mcw.GeneralSurgery.models.Content;
import edu.mcw.GeneralSurgery.models.DBhelper;
import edu.mcw.GeneralSurgery.models.Image;
import edu.mcw.GeneralSurgery.models.Option;
import edu.mcw.GeneralSurgery.models.Prompt;
import edu.mcw.GeneralSurgery.models.SharedPreferencesHelper;
import edu.mcw.GeneralSurgery.models.Topic;

/**
 * Created by arham on 2/8/18.
 */

public class APIHelper {

    Context context;
    final String API_BASE = "https://appbrewerydev.uwm.edu/trauma-guidelines/api/v1";

    @Inject
    public APIHelper(Context context) {
        this.context = context;
        ((MainApplication)context).getAppComponent().inject(this);
    }

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    public void Register(String email, String name, final GenericNetworkCallback callback) {

        final String url = API_BASE + "/register";
        Map<String, String> parameters = new HashMap();

        parameters.put("email", email);
        parameters.put("name", name);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST, url, new JSONObject(parameters), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject meta = response.getJSONObject("meta");

                    boolean success = meta.getBoolean("success");

                    JSONObject data = response.getJSONObject("data");

                    String metaTitle;
                    String metaMessage;

                    if (!success) {
                        metaTitle = data.has("title") ? data.getString("title") : context.getString(R.string.request_error_title);
                        metaMessage = data.has("message") ? data.getString("message") : context.getString(R.string.request_error_message);
                        callback.onError(metaTitle, metaMessage);
                    } else {
                        metaTitle = data.has("title") ? data.getString("title") : context.getString(R.string.request_access_title);
                        metaMessage = data.has("message") ? data.getString("message") : context.getString(R.string.request_access_message);
                        callback.onSuccess(metaTitle, metaMessage);
                    }


                } catch (JSONException e) {
                    callback.onError(context.getString(R.string.server_error), context.getString(R.string.server_error_message));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(context.getString(R.string.network_error), context.getString(R.string.network_error_message));
            }
        });

        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }

    public void Authenticate(String email, String password, final AuthenticateNetworkCallback callback) {

        final String url = API_BASE + "/authenticate";
        Map<String, String> parameters = new HashMap();

        parameters.put("email", email);
        parameters.put("password", password);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST, url, new JSONObject(parameters), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject meta = response.getJSONObject("meta");

                    boolean success = meta.getBoolean("success");

                    JSONObject data = response.getJSONObject("data");

                    String metaTitle;
                    String metaMessage;

                    if (!success) {
                        metaTitle = data.has("title") ? data.getString("title") : context.getString(R.string.request_error_title);
                        metaMessage = data.has("message") ? data.getString("message") : context.getString(R.string.request_error_message);
                        callback.onError(metaTitle, metaMessage);
                    } else {
                        metaTitle = data.has("title") ? data.getString("title") : context.getString(R.string.request_access_title);
                        metaMessage = data.has("message") ? data.getString("message") : context.getString(R.string.request_access_message);

                        JSONObject user = data.getJSONObject("user");
                        int id = user.has("name") ? user.getInt("id") : 0  ;
                        String name = user.has("name") ? user.getString("name") :"User" ;
                        String email = user.has("email") ? user.getString("email") :"Default email" ;
                        String api_token = user.has("api_token") ? user.getString("api_token") :"Default token" ;

                        callback.onSuccess(id,name,email,api_token,metaTitle, metaMessage);
                    }

                } catch (JSONException e) {
                    callback.onError(context.getString(R.string.server_error), context.getString(R.string.server_error_message));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(context.getString(R.string.network_error), context.getString(R.string.network_error_message));
            }
        });

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void Authenticate(String api_token, final AuthenticateNetworkCallback callback) {

        final String url = API_BASE + "/authenticate";
        Map<String, String> parameters = new HashMap();

        parameters.put("api_token", api_token);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST, url, new JSONObject(parameters), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject meta = response.getJSONObject("meta");

                    boolean success = meta.getBoolean("success");

                    JSONObject data = response.getJSONObject("data");

                    String metaTitle;
                    String metaMessage;

                    if (!success) {
                        metaTitle = data.has("title") ? data.getString("title") : context.getString(R.string.request_error_title);
                        metaMessage = data.has("message") ? data.getString("message") : context.getString(R.string.request_error_message);
                        callback.onError(metaTitle, metaMessage);
                    } else {
                        metaTitle = data.has("title") ? data.getString("title") : context.getString(R.string.request_access_title);
                        metaMessage = data.has("message") ? data.getString("message") : context.getString(R.string.request_access_message);

                        JSONObject user = data.getJSONObject("user");
                        int id = user.has("name") ? user.getInt("id") : 0  ;
                        String name = user.has("name") ? user.getString("name") :"User" ;
                        String email = user.has("email") ? user.getString("email") :"Default email" ;
                        String api_token = user.has("api_token") ? user.getString("api_token") :"Default token" ;


                        callback.onSuccess(id,name,email,api_token,metaTitle, metaMessage);
                    }

                } catch (JSONException e) {
                    callback.onError(context.getString(R.string.server_error), context.getString(R.string.server_error_message));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(context.getString(R.string.network_error), context.getString(R.string.network_error_message));
            }
        });

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }


    public void Forgot(String email, final GenericNetworkCallback callback) {

        final String url = API_BASE + "/forgot";
        Map<String, String> parameters = new HashMap();

        parameters.put("email", email);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST, url, new JSONObject(parameters), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject meta = response.getJSONObject("meta");

                    boolean success = meta.getBoolean("success");

                    JSONObject data = response.getJSONObject("data");

                    String metaTitle;
                    String metaMessage;

                    if (!success) {
                        metaTitle = data.has("title") ? data.getString("title") : context.getString(R.string.request_error_title);
                        metaMessage = data.has("message") ? data.getString("message") : context.getString(R.string.request_error_message);
                        callback.onError(metaTitle, metaMessage);
                    } else {
                        metaTitle = data.has("title") ? data.getString("title") : context.getString(R.string.request_access_title);
                        metaMessage = data.has("message") ? data.getString("message") : context.getString(R.string.request_access_message);
                        callback.onSuccess(metaTitle, metaMessage);
                    }


                } catch (JSONException e) {
                    callback.onError(context.getString(R.string.server_error), context.getString(R.string.server_error_message));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(context.getString(R.string.network_error), context.getString(R.string.network_error_message));
            }
        });

        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }

    public void downloadLocalData( final GenericNetworkCallback callback){ //should I make a new callback for clarity?
        String last_updated = sharedPreferencesHelper.getLastUpdatedDate();
        String url = API_BASE + "/batch";
        if(!last_updated.isEmpty()) {
            url += "?since = " + last_updated;
        }
        final DBhelper dBhelper = new DBhelper(context);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<Topic> topics  = new ArrayList<>();
                ArrayList<Content> contents  = new ArrayList<>();
                ArrayList<Image> images  = new ArrayList<>();
                ArrayList<Prompt> prompts = new ArrayList<>();
                ArrayList<Option> options = new ArrayList<>();


                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray topicObjects = data.getJSONArray("topics");

                    for (int i = 0; i < topicObjects.length(); i++){

                        JSONObject topicObject =topicObjects.getJSONObject(i);
                        int id = topicObject.getInt("id");
                        int parentID = topicObject.optInt("parent_id" );
                        String title = topicObject.getString("title");
                        String tags = topicObject.getString("tags");
                        String summary = topicObject.getString("summary");
                        int priority = topicObject.getInt("priority");

                        Topic topic = new Topic(id, parentID,title,tags,summary, priority);
                        topics.add(topic);
                    }
                    dBhelper.addTopics(topics);

                    JSONArray contentObjects = data.getJSONArray("contents");

                    for (int i = 0; i < contentObjects.length(); i++){
                        JSONObject contentObject =contentObjects.getJSONObject(i);
                        int id = contentObject.getInt("id");
                        int topicID = contentObject.getInt("topic_id");
                        String description = contentObject.getString("description");

                        Content content = new Content(id, topicID, description);
                        contents.add(content);
                    }
                    dBhelper.addContent(contents);

                    JSONArray imageObjects = data.getJSONArray("images");

                    for (int i = 0; i < imageObjects.length(); i++){
                        JSONObject imageObject =imageObjects.getJSONObject(i);
                        int id = imageObject.getInt("id");
                        int contentID = imageObject.getInt("content_id");
                        String title = imageObject.getString("title");
                        String url = imageObject.getString("url");
                        int priority = imageObject.getInt("priority");

                        Image image = new Image(id, contentID,title,url, priority);
                        images.add(image);
                    }
                    dBhelper.addimages(images);

                    JSONArray promptObjects = data.getJSONArray("prompts");

                    for (int i = 0; i < promptObjects.length(); i++){
                        JSONObject promptObject =promptObjects.getJSONObject(i);

                        int id = promptObject.getInt("id");
                        int contentID = promptObject.getInt("content_id");
                        int isRoot = promptObject.getInt("is_root");
                        boolean root;
                        if(isRoot == 1){
                            root = true;
                        }else{
                            root = false;
                        }
                        String promptString = promptObject.getString("prompt");

                        Prompt prompt = new Prompt(id, contentID,root, promptString);
                        prompts.add(prompt);
                    }
                    dBhelper.addPrompt(prompts);

                    JSONArray optionObjects = data.getJSONArray("options");

                    for (int i = 0; i < optionObjects.length(); i++){
                        JSONObject optionObject =optionObjects.getJSONObject(i);
                        int id = optionObject.getInt("id");
                        int parentID = optionObject.getInt("parent_id");
                        int targetID = optionObject.optInt("target_id");
                        String optionString = optionObject.getString("prompt");

                        Option option = new Option(id, parentID,targetID, optionString);
                        options.add(option);
                    }

                    dBhelper.addOption(options);

                    String time = TimestampUtils.getISO8601StringForCurrentDate();
                    sharedPreferencesHelper.setLastUpdatedDate(time);

                    callback.onSuccess("Success","Assignments downloaded");


                } catch (JSONException exception) {

                    callback.onError(context.getString(R.string.server_error), context.getString(R.string.server_error_message));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(context.getString(R.string.network_error), context.getString(R.string.network_error_message));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + sharedPreferencesHelper.getToken());
                params.put("Accept", "application/json");
                return params;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }


    public void sendFeedback(int id,String type, String reason, String description,final GenericNetworkCallback callback) {

        final String url = API_BASE + "/feedback";
        Map<String, String> parameters = new HashMap();

        parameters.put("id", String.valueOf(id));
        parameters.put("type", type);
        parameters.put("reason", reason);
        parameters.put("description", description);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST, url, new JSONObject(parameters), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject meta = response.getJSONObject("meta");

                    boolean success = meta.getBoolean("success");

                    JSONObject data = response.getJSONObject("data");

                    String metaTitle;
                    String metaMessage;

                    if (!success) {
                        metaTitle = data.has("title") ? data.getString("title") : context.getString(R.string.request_error_title);
                        metaMessage = data.has("message") ? data.getString("message") : context.getString(R.string.request_error_message);
                        callback.onError(metaTitle, metaMessage);
                    } else {
                        metaTitle = data.has("title") ? data.getString("title") : context.getString(R.string.request_access_title);
                        metaMessage = data.has("message") ? data.getString("message") : context.getString(R.string.request_access_message);
                        callback.onSuccess(metaTitle, metaMessage);
                    }


                } catch (JSONException e) {
                    callback.onError(context.getString(R.string.server_error), context.getString(R.string.server_error_message));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(context.getString(R.string.network_error), context.getString(R.string.network_error_message));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + sharedPreferencesHelper.getToken());
                params.put("Accept", "application/json");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }



}
