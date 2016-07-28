package com.zprocessapps.mycustomcoupon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zprocessapps.mycustomcoupon.objects.C_Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import cz.msebera.android.httpclient.Header;

public class BusinessDataActivity extends AppCompatActivity {
    protected C_Application app;
    private Intent intent;
    MCCRestClient client = new MCCRestClient();
    Context con;

    /** Called when the user touches the button */
    public void tryServicesSelection(View view) {
        // TODO:check for proper fill-in of the address information
        //Intent intent = new Intent(this, ServicesSelectionActivity.class);
        //startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_data);
        fillInBusinessFields();

    }

    public void submitBusinessData() throws JSONException {

        final RequestParams params = new RequestParams();
        if(MCCDataStore.getInstance().currentBusinessData.businessName != ""
                && MCCDataStore.getInstance().currentBusinessData.tagline != ""
                && MCCDataStore.getInstance().currentBusinessData.phoneNumber != ""
                && MCCDataStore.getInstance().currentBusinessData.licenseNumber != ""
                && MCCDataStore.getInstance().currentBusinessData.website != ""
                && MCCDataStore.getInstance().currentBusinessData.defaultRestrictions != ""
                ){

            params.put("businessName",MCCDataStore.getInstance().currentBusinessData.businessName);
            params.put("tagline", MCCDataStore.getInstance().currentBusinessData.tagline);
            params.put("phonenumber", MCCDataStore.getInstance().currentBusinessData.phoneNumber);
            params.put("licensenumber", MCCDataStore.getInstance().currentBusinessData.licenseNumber);
            params.put("associatelicensenumber", MCCDataStore.getInstance().currentBusinessData.associateLicenseNumber);
            params.put("website",MCCDataStore.getInstance().currentBusinessData.website);
            params.put("defaultrestrictions",MCCDataStore.getInstance().currentBusinessData.defaultRestrictions);
            params.put("notificationsbyapns",MCCDataStore.getInstance().currentBusinessData.notificaitonsByAPNS);
            params.put("notificationsbyemail",MCCDataStore.getInstance().currentBusinessData.notificaitonsByEmail);
            intent = new Intent(this, BusinessDataActivity.class);



            MCCRestClient.post(MCCRestClient.kC_APIMerchantDataRegister, params, new JsonHttpResponseHandler() {

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


        }else {
            //TODO: Fire toast to fill out all fields
            Toast.makeText(getApplicationContext(), "Please fill in all fields",
                    Toast.LENGTH_LONG).show();
        }




        // TODO:check for email re-use, required fields, update current user object and send to address collection



    }

    private void fillInBusinessFields(){

        if (MCCDataStore.getInstance().currentBusinessData.businessName != "") {
            EditText businessNameText = (EditText) findViewById(R.id.edit_businessname);
            businessNameText.setText(MCCDataStore.getInstance().currentBusinessData.businessName);
        }

        if (MCCDataStore.getInstance().currentBusinessData.tagline != "") {
            EditText taglineText = (EditText) findViewById(R.id.edit_tagline);
            taglineText.setText(MCCDataStore.getInstance().currentBusinessData.tagline);
        }

        if (MCCDataStore.getInstance().currentBusinessData.phoneNumber != "") {
            EditText phoneText = (EditText) findViewById(R.id.edit_phone);
            phoneText.setText(MCCDataStore.getInstance().currentBusinessData.phoneNumber);
        }

        if (MCCDataStore.getInstance().currentBusinessData.licenseNumber != "") {
            EditText licenseNumberText = (EditText) findViewById(R.id.edit_license);
            licenseNumberText.setText(MCCDataStore.getInstance().currentBusinessData.licenseNumber);
        }

        if (MCCDataStore.getInstance().currentBusinessData.associateLicenseNumber != "") {
            EditText associateLicenseNumberText = (EditText) findViewById(R.id.edit_assocaite_license);
            associateLicenseNumberText.setText(MCCDataStore.getInstance().currentBusinessData.associateLicenseNumber);
        }

        if (MCCDataStore.getInstance().currentBusinessData.website != "") {
            EditText websiteText = (EditText) findViewById(R.id.edit_website);
            websiteText.setText(MCCDataStore.getInstance().currentBusinessData.website);
        }

        if (MCCDataStore.getInstance().currentBusinessData.defaultRestrictions != "") {
            EditText defaultRestrictionsText = (EditText) findViewById(R.id.edit_restrictions);
            defaultRestrictionsText.setText(MCCDataStore.getInstance().currentBusinessData.defaultRestrictions);
        }

        Switch apnsSwitch = (Switch) findViewById(R.id.apns_switch);
        apnsSwitch.setChecked( MCCDataStore.getInstance().currentBusinessData.notificaitonsByAPNS);

        Switch emailSwitch = (Switch) findViewById(R.id.email_switch);
        emailSwitch.setChecked( MCCDataStore.getInstance().currentBusinessData.notificaitonsByEmail);

    }

}
