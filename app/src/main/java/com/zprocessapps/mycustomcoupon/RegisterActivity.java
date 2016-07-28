package com.zprocessapps.mycustomcoupon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    String merchant_or_consumer = "";
    MCCRestClient client = new MCCRestClient();
    Context con;
    /*
    testEmail() is used to check whether or not the indicated email is available. It should be
    as it belongs to the user but it may be that they have entered already registered and need
    to be rerouted to the sign-in view.
    */

    protected void testEmail() throws JSONException {

        EditText emailText = (EditText) findViewById(R.id.edit_email);
        EditText passwordText = (EditText) findViewById(R.id.edit_password);
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        final RequestParams params = new RequestParams();
        params.add("email",email);
        params.add("password",password);

        MCCRestClient.post(MCCRestClient.kC_APIAccountCheckEmail, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //the email test passed so go ahead and try to register
                try {
                    registerUser(email, password);
                } catch (JSONException except){
                    System.out.println(except.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse);
            }

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

        });
    }

    /*
    registerUser() is called from testEmail() if the email is unused. The response handler moves the
    user to the AddressActivity.
     */

    public void registerUser(String email, String password) throws JSONException {
        final RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        Intent intent;
        // TODO:check for email re-use, required fields, update current user object and send to address collection
        if(merchant_or_consumer != "" && email != "" && password != ""){
            if (merchant_or_consumer == "CONSUMER") {

                intent = new Intent(this, LoginActivity.class);
            } else {
                intent = new Intent(this, AddressActivity.class);

            }
            startActivity(intent);
        }else {
            //TODO: Fire toast to fill out all fields
            Toast.makeText(getApplicationContext(), "Please fill in all fields",
                    Toast.LENGTH_LONG).show();
            return;
        }

        MCCRestClient.post(MCCRestClient.kC_APIAccountRegister, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response);
                Intent intent = new Intent(con, AddressActivity.class);
                startActivity(intent);
            }

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_consumer:
                if (checked)
                    // consumer selected
                    merchant_or_consumer = "CONSUMER";
                    break;
            case R.id.radio_merchant:
                if (checked)
                    // merchant selelcted
                    merchant_or_consumer = "MERCHANT";
                    break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }


}

