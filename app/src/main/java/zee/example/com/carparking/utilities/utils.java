package zee.example.com.carparking.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import zee.example.com.carparking.models.BookedParking;
import zee.example.com.carparking.models.ParkPlace;
import zee.example.com.carparking.service.ServiceListener;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Zul Qarnain on 11/28/2017.
 */

public class utils {

    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static String usertype = null;
    private static DatabaseReference ref;
    private static String comName = "";
    private static int count = 0;

    public static String getDeviceName() {
        String maufacaturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(maufacaturer)) {
            return maufacaturer.toUpperCase();
        } else
            return maufacaturer.toLowerCase() + " " + model;
    }

   /* public static String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }*/

   /* public static void updateFcm(String refreshedToken) {
        String mKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap<String, String> fcm = new HashMap<String, String>();
        fcm.put("token", refreshedToken);
        fcm.put("device", utils.getDeviceName());

        DatabaseReference ref = FirebaseDatabase.getInstance().
                getReference("users").child(mKey).child("fcm").child(getIMEI());
        ref.setValue(fcm);
    }*/

    public static String getuseype() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String key = auth.getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(key);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                usertype = dataSnapshot.child("type").getValue(String.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return usertype;

    }


    public static void getParkCount(final String area, final ServiceListener listener) {
        count = 0;
        ref = FirebaseDatabase.getInstance().getReference("parking");
        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ParkPlace parkPlace = dataSnapshot.getValue(ParkPlace.class);
                String allocated = parkPlace.getAlocated();
                if (parkPlace.equals(area) && allocated.equals("false")) {
                    listener.success(1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public static void isParkBelong(String pid, String uid, final ServiceListener listener) {
        ref = FirebaseDatabase.getInstance().getReference("allocted").child(pid).child("user");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("", "onDataChange: " + dataSnapshot.getValue());
                listener.success(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void isExpire(String pid,final ServiceListener listener) {
        count = 0;
        ref = FirebaseDatabase.getInstance().getReference("allocted").child(pid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("", "onDataChange: "+dataSnapshot);
                BookedParking obj = dataSnapshot.getValue(BookedParking.class);
                Log.d("", "onDataChange: " + dataSnapshot.getValue());
                listener.success(obj);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getTimeStamp(String date, int hours, int min) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss.SSS");
            String mString = date + " " + hours + ":" + min + ":" + "00.000";
            Date parsedDate = dateFormat.parse(mString);
            long time = parsedDate.getTime();
            return String.valueOf(time);
        } catch (Exception e) { //this generic but you can control another types of exception
            e.printStackTrace();
        }
        return null;
    }

    public static String getDate(String args) {
        long time = Long.parseLong(args);
        Date df = new java.util.Date(time);
        String vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
        return vv;
    }

    public static String getActiveUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        return uid;
    }

    public static Boolean isValidTime(String timeIn, String timeOut) {
        long in = Long.parseLong(timeIn);
        long out = Long.parseLong(timeOut);

        if (in < out) {
            return true;
        } else
            return false;
    }


}
