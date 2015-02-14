package com.yan.stamped;

/**
 * Created by Henrik on 27/01/2015.
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class UserFunctions {

    private JSONParser jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://stamped.host56.com/";
    private static String registerURL = "http://10.0.2.2/ah_login_api/";

    private static String login_tag = "login";
    private static String sync_tag = "sync";
    private static String register_tag = "register";
    private static String increment_tag = "increment";
    private static String rewards_tag = "pullrewards";

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        Log.e("JSON", json.toString());
        return json;
    }

    /**
     * function make Login Request
     * @param email
     * */
    public JSONObject syncSchemes(String email){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", sync_tag));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        Log.e("JSON", json.toString());
        return json;
    }

    public JSONObject syncRewards(String email){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", rewards_tag));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        Log.e("JSON", json.toString());
        return json;
    }


    public JSONObject incrementStamp(String email, String SchemeID){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", increment_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("SchemeID", SchemeID));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        Log.e("JSON", json.toString());
        return json;
    }

    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }

    public String getUserEmail(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        String name = db.getUserDetails().get("Email");
        return name;
    }

    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

    public void sync(final Context context) {
        new Thread(new Runnable() {
            public void run() {
                String email = getUserEmail(context);

                try {
                    JSONObject json = syncSchemes(email);
                    JSONObject json2 = syncRewards(email);
                    if (json.getString("schemes") != null) {
                        String res = json.getString("schemes");
                        String res2 = json2.getString("rewards");
                        DatabaseHandler db = new DatabaseHandler(context);
                        JSONArray json_schemes = json.getJSONArray("schemes");
                        JSONArray json_schemes2 = json2.getJSONArray("rewards");
                        //Log.e("Schemes!", json_schemes.length() + "");
                        for (int i=0;i<json_schemes.length();i++) {
                            db.addScheme(json_schemes.getJSONObject(i).getString("SchemeID"),json_schemes.getJSONObject(i).getString("SchemeName"),json_schemes.getJSONObject(i).getString("StampsCurrent"),json_schemes.getJSONObject(i).getString("StampsForever"));
                        }
                        for (int i=0;i<json_schemes2.length();i++) {
                            db.addReward(json_schemes2.getJSONObject(i).getString("RewardID"),json_schemes2.getJSONObject(i).getString("SchemeID"),json_schemes2.getJSONObject(i).getString("Name"),json_schemes2.getJSONObject(i).getString("Description"),json_schemes2.getJSONObject(i).getString("Cost"),json_schemes2.getJSONObject(i).getString("Requirement"));
                        }
                    }else   {
                        // Error in login
                        Log.e("String", "WE HAVE NOT LOGGED IN");
                        //loginErrorMsg.setText("Incorrect username/password");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}