package com.zprocessapps.mycustomcoupon;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static com.zprocessapps.mycustomcoupon.Constants.DEV_MODE;

/**
 * Created by jasonwolfe on 10/14/15.
 */


public class MCCRestClient {

    public static final String kC_APIBaseImagePath = "https://www.mccpapp.com/api";
    public static final String kC_APIBaseURLProduction = "https://www.mccpapp.com/api/";
    public static final String kC_APIBaseURLStaging = "http://192.168.0.50:8000/";
    public static final String kC_APIBaseURLStagingLocal = "http://localhost:8000/";
    public static final String kC_APIAuthTokenKey = "Authorization";

    // Account
    public static final String kC_APIAccountLogIn = "user/login/";
    public static final String kC_APIAccountCheckEmail = "user/checkemail/";
    public static final String kC_APIAccountRegister = "user/register/";
    public static final String kC_APIAccountUploadAPNSKey = "user/uploadapnskey/";
    public static final String kC_APIAddressRegister = "user/uploadaddress/";
    public static final String kC_APIMerchantDataRegister = "user/uploadmerchantdata/";
    public static final String kC_APIServicesRegister = "user/uploadservices/";

    //reports
    public static final String kC_APIGetMerchantReport = "coupon/getmerchantreport/";
    public static final String kC_APIGetConsumerReport = "coupon/getconsumerreport/";
    public static final String kC_APIClearMerchantReport = "coupon/clearmerchantreport/";
    public static final String kC_APIClearConsumerReport = "coupon/clearconsumerreport/";


    // Create Coupon Requests & Responses
    public static final String kC_APICreateCoupon = "coupon/createcoupon/";
    public static final String kC_APICreateCouponRequest = "coupon/createrequest/";

    // Get Coupon Requests & Responses
    public static final String kC_APICoupons = "coupon/couponlist/";
    public static final String kC_APICouponRequests = "coupon/requestlist/";

    // MCCTypes
    public static final String kC_APIMerchanteTypes = "mcctypes/merchanttypes/";
    public static final String kC_APIServiceTypes = "mcctypes/servicetypes/";
    public static final String kC_APIServiceTypesForUser = "mcctypes/servicetypesforuser/";

    //flag/delete
    public static final String kC_APIFlagCoupon = "coupon/flagcoupon/";
    public static final String kC_APIDeleteCoupon = "coupon/deletecoupon/";
    public static final String kC_APIFlagRequest = "coupon/flagcrequest/";
    public static final String kC_APIDeleteRequest = "coupon/deleterequest/";

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("token","test");
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        if(DEV_MODE){
            return kC_APIBaseURLProduction + relativeUrl;
        }else{
            return kC_APIBaseURLStaging + relativeUrl;
        }

    }

}
