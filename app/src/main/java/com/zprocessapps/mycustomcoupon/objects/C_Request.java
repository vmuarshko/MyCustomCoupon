package com.zprocessapps.mycustomcoupon.objects;

import java.util.Date;

/**
 * Created by jasonwolfe on 2/17/16.
 */
public class C_Request {

    public String requestID;
    public String lat, lon, requestText;
    public Date insertstamp, requestdate;
    public C_BusinessType businessType;
    public C_ServiceType serviceType;
    public C_User user;

}
