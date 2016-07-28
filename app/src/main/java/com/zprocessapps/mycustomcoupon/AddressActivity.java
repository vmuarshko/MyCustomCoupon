package com.zprocessapps.mycustomcoupon;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zprocessapps.mycustomcoupon.objects.C_Application;
import com.zprocessapps.mycustomcoupon.objects.C_Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class AddressActivity extends AppCompatActivity {
    private GoogleApiClient mClient;
    protected C_Application app;
    private Intent intent;
    MCCRestClient client = new MCCRestClient();
    Context con;
    /** Called when the user touches the button */
    public void loadMerchantDataActivity(View view) {
        // TODO:check for proper fill-in of the address information
        Intent intent = new Intent(this, BusinessDataActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // GoogleApiAvailability gAPI = new GoogleApiAvailability.getInstance();
        int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog =  GoogleApiAvailability.getInstance().getErrorDialog(this, errorCode, 1,
                            new DialogInterface.OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    // Leave if services are unavailable.
                                    finish();
                                }
                            });

            errorDialog.show();
        }

        // Get the application instance
        app = (C_Application) getApplication();

        // Call a custom application method
        //app.customAppMethod();

        // Call a custom method in MySingleton
        //MCCDataStore.getInstance().customSingletonMethod();

        // Read the value of a variable in MySingleton
        String singletonVar = C_Singleton.getInstance().customVar;

        // setHasOptionsMenu(true);

        mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        //this.invalidateOptionsMenu(this);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        setContentView(R.layout.activity_address);
        if(MCCDataStore.getInstance().currentUser.userType == "CONSUMER"){
            removeAddressFields();
        }
        fillAddressFields();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_locate);
//        searchItem.setEnabled(mClient.isConnected());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);

        PackageManager pm = this.getPackageManager();
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = this.checkCallingOrSelfPermission(permission);
        res = PackageManager.PERMISSION_GRANTED;

        if(res != 0) {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mClient, request, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            //convert coordinates to address
                            insertVerboseAddress(location);
                        }
                    });
        }else{
            finish();
        }
    }
    private void insertVerboseAddress(Location location){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();
            //String knownName = addresses.get(0).getFeatureName();

            EditText address1Text = (EditText) findViewById(R.id.edit_address1);
            address1Text.setText(address);
            MCCDataStore.getInstance().currentAddress.address1 = address;

            EditText cityText = (EditText) findViewById(R.id.edit_city);
            cityText.setText(city);
            MCCDataStore.getInstance().currentAddress.city = city;

            EditText stateText = (EditText) findViewById(R.id.edit_state);
            stateText.setText(state);
            MCCDataStore.getInstance().currentAddress.state = state;

            EditText zipcodeText = (EditText) findViewById(R.id.edit_zipcode);
            zipcodeText.setText(postalCode);
            MCCDataStore.getInstance().currentAddress.zipCode = postalCode;

        }
        catch(IOException error){
            System.out.println (error.toString());
        }

    }

    public void submitAddress() throws JSONException {


        EditText address1Text = (EditText) findViewById(R.id.edit_address1);
        EditText cityText = (EditText) findViewById(R.id.edit_city);
        EditText stateText = (EditText) findViewById(R.id.edit_state);
        EditText zipcodeText = (EditText) findViewById(R.id.edit_zipcode);


        final RequestParams params = new RequestParams();
        if(address1Text.getText().toString() != "" &&
                address1Text.getText().toString() != "" &&
                address1Text.getText().toString() != "" &&
                address1Text.getText().toString() != "" &&
                address1Text.getText().toString() != ""
                ){
            if (MCCDataStore.getInstance().currentUser.userType == "CONSUMER") {
                params.put("zipcode", zipcodeText);
                intent = new Intent(this, LoginActivity.class);
            }else {
                params.put("email", MCCDataStore.getInstance().currentUser.email);
                params.put("address1", address1Text);
                params.put("city", cityText);
                params.put("state", stateText);
                params.put("zipcode", zipcodeText);
                intent = new Intent(this, BusinessDataActivity.class);
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


        }else {
            //TODO: Fire toast to fill out all fields
            Toast.makeText(getApplicationContext(), "Please fill in all fields",
                    Toast.LENGTH_LONG).show();
        }




        // TODO:check for email re-use, required fields, update current user object and send to address collection



    }

    private void fillAddressFields(){
        if(MCCDataStore.getInstance().currentUser.userType == "MERCHANT") {
            if (MCCDataStore.getInstance().currentAddress.address1 != "") {
                EditText address1Text = (EditText) findViewById(R.id.edit_address1);
                address1Text.setText(MCCDataStore.getInstance().currentAddress.address1);
            }

            if (MCCDataStore.getInstance().currentAddress.city != "") {
                EditText cityText = (EditText) findViewById(R.id.edit_city);
                cityText.setText(MCCDataStore.getInstance().currentAddress.city);
            }

            if (MCCDataStore.getInstance().currentAddress.state != "") {
                EditText stateText = (EditText) findViewById(R.id.edit_state);
                stateText.setText(MCCDataStore.getInstance().currentAddress.state);
            }
        }

        if (MCCDataStore.getInstance().currentAddress.zipCode != ""){
            EditText zipText = (EditText) findViewById(R.id.edit_zipcode);
            zipText.setText(MCCDataStore.getInstance().currentAddress.zipCode);
        }


    }

    private void removeAddressFields(){
        EditText address = (EditText) findViewById(R.id.edit_address1);
        ((ViewGroup) address.getParent()).removeView(address);

        EditText city = (EditText) findViewById(R.id.edit_city);
        ((ViewGroup) address.getParent()).removeView(city);

        EditText state = (EditText) findViewById(R.id.edit_state);
        ((ViewGroup) address.getParent()).removeView(state);
    }

}
