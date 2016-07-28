package com.zprocessapps.mycustomcoupon;

import com.zprocessapps.mycustomcoupon.objects.C_Address;
import com.zprocessapps.mycustomcoupon.objects.C_BusinessData;
import com.zprocessapps.mycustomcoupon.objects.C_User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jasonwolfe on 2/17/16.
 */

 /*
    From iOS Code
    @property (nonatomic) NSArray *merchantsAndServicesItemList;

    @property (nonatomic) BOOL emailAvailable;
    @property (nonatomic) NSString *currentRequestService;
    @property (nonatomic) BOOL isNewServiceSelection;
    @property (nonatomic) NSString *archivedFilePath;
    @property (nonatomic) BOOL isInEditMode;

    #pragma mark - User Methods

    - (void)archiveUserToFile:(C_User *)user;
    - (void)archiveCouponsToFile:(NSArray *)object;
    - (void)archiveRequestsToFile:(NSArray *)object;
    - (void)resetUser:(void(^)(void))completion;
    */
public class MCCDataStore implements Serializable{

    private static MCCDataStore instance;
    public C_User currentUser;
    public C_Address currentAddress;
    public C_BusinessData currentBusinessData;
    public ArrayList couponArrayList;
    public ArrayList requestArrayList;
    public ArrayList businessTypeObjects;
    public ArrayList serviceTypeObjects;
    public ArrayList serviceTypeObjectsForUser;

    public static void initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new MCCDataStore();
            instance.currentAddress = new C_Address();
            instance.currentBusinessData = new C_BusinessData();
        }
    }

    public static MCCDataStore getInstance()
    {
        // Return the instance
        return instance;
    }


//    public void customSingletonMethod()
//    {
//        // Custom method
//    }


    public void persistObject(String objectType){
        String filename = objectType + ".ser";
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(this.currentUser);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void loadObject(){
        String filename = "user.ser";
        FileInputStream fis = null;
        ObjectInputStream in = null;
        C_User currentUser = null;

        try
            {
                fis = new FileInputStream(filename);
                in = new ObjectInputStream(fis);
                this.currentUser = (C_User)in.readObject();
                in.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            catch(ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
    }

    public void persistCouponList(){

    }

    public void loadCouponList(){

    }

    public void persistRequestList(){

    }

    public void loadRequestList(){

    }

}
