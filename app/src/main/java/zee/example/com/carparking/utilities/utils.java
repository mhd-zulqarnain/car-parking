package zee.example.com.carparking.utilities;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import zee.example.com.carparking.models.Booked;
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

    public static void isParkBelong(String pid, final String uid, final ServiceListener listener) {
        ref = FirebaseDatabase.getInstance().getReference("bookings");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Booked obj = snapshot.getValue(Booked.class);
                    String pid = obj.getPid();
                    if (obj.getPid().equals(pid) && obj.getUser().equals(uid))
                        listener.success(obj);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void isExpire(String pid, final ServiceListener listener) {
        count = 0;
        ref = FirebaseDatabase.getInstance().getReference("allocted").child(pid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("", "onDataChange: " + dataSnapshot);
                Booked obj = dataSnapshot.getValue(Booked.class);
                Log.d("", "onDataChange: " + dataSnapshot.getValue());
                listener.success(obj);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void isBooked(final String pid, final Long in, final Long out, final ServiceListener listener) {
        count = 0;


        ref = FirebaseDatabase.getInstance().getReference("bookings");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Booked obj = snapshot.getValue(Booked.class);
                    String pid = obj.getPid();

                    Long timIn = Long.valueOf(obj.getTimeIn());
                    Long timOut = Long.valueOf(obj.getTimeOut());

                    if (obj.getPid().equals(pid)) {
                        if (timIn <= in && timOut >= out) {
                            listener.success(obj);
                        } else if (timIn <= in && timOut >= in) {
                            listener.success(obj);
                        } else if (timIn <= out && timOut >= out) {
                            listener.success(obj);
                        }

                    }
                }
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
        } catch (Exception e) {
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
