package com.yan.stamped;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import static android.os.StrictMode.setThreadPolicy;


public class Login extends ActionBarActivity {

    EditText login;
    EditText passwordText;

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "UserID";
    private static String KEY_NAME = "Username";
    private static String KEY_EMAIL = "Email";
    private static String KEY_CREATED_AT = "created_at";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.userText);
        passwordText = (EditText) findViewById(R.id.passText);
    }


    public void onClick(View v) {
        String email = login.getText().toString();
        String password = passwordText.getText().toString();

        new Thread(new Runnable() {
            public void run() {
                String email = login.getText().toString();
                String password = passwordText.getText().toString();
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.loginUser(email, password);

                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        //loginErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1){
                            // user successfully logged in
                            // Store user details in SQLite Database
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");
                           db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID));
                            Log.e("String", "WE HAVE LOGGED IN");
                            Intent dashboard = new Intent(getApplicationContext(), Home.class);
                            Log.e("String", "WE HAVE LOGGED IN");
                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);
                            // Clear all previous data in database
                           // userFunction.logoutUser(getApplicationContext());

                            // Launch Dashboard Screen

                            // Close Login Screen
                            finish();
                        }else{
                            // Error in login
                            Log.e("String", "WE HAVE NOT LOGGED IN");
                            //loginErrorMsg.setText("Incorrect username/password");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
