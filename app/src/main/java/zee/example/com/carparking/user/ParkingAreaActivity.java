package zee.example.com.carparking.user;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import zee.example.com.carparking.R;
import zee.example.com.carparking.models.Area;
import zee.example.com.carparking.models.ParkPlace;
import zee.example.com.carparking.user.adapter.ParkingAreaAdapter;
import zee.example.com.carparking.utilities.Messege;
import zee.example.com.carparking.utilities.utils;

public class ParkingAreaActivity extends AppCompatActivity implements View.OnClickListener {

    private String aid;
    private RecyclerView rv;
    private ParkingAreaAdapter adapter;
    private ArrayList<ParkPlace> list;
    DatabaseReference ref;
    Calendar calendar;
    Context ctx;

    TextView sttv;
    TextView endtv;
    TextView warnTv;
    TextView titleDate;

    Button fBtn;
    String timeIn = " ";
    String timeOut = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_area);
        ctx = this;
        calendar = Calendar.getInstance();
        aid = getIntent().getStringExtra("area");
        setTitle("Parking detail of " + aid);

        rv = findViewById(R.id.recycler_parking_activity);
        sttv = findViewById(R.id.starting_time);
        endtv = findViewById(R.id.ending_time);
        fBtn = findViewById(R.id.btn_filter);
        warnTv = findViewById(R.id.txt_warn);
        titleDate = findViewById(R.id.text_title_date);

        sttv.setOnClickListener(this);
        endtv.setOnClickListener(this);
        fBtn.setOnClickListener(this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        timeIn = sttv.getText().toString();
        timeOut = endtv.getText().toString();
        Long cuurentTime = System.currentTimeMillis();
        String date = utils.getDate(cuurentTime.toString());
        titleDate.setText("Booking Date: " + date.substring(0, 11));

    }

    private void updateUi() {

        adapter = new ParkingAreaAdapter(this, list, timeIn, timeOut, warnTv);
        rv.setAdapter(adapter);
        ref = FirebaseDatabase.getInstance().getReference("parking");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ParkPlace a = dataSnapshot.getValue(ParkPlace.class);

                int index = getIndexOf(a.getPid());
                if (index == -1) {
                    if (a.getArea().equals(aid)) {
                        list.add(a);
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ParkPlace prk = dataSnapshot.getValue(ParkPlace.class);
                int index = getIndexOf(prk.getPid());
                if (index != -1) {
                    list.remove(index);
                    list.add(index, prk);
                    adapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ParkPlace prk = dataSnapshot.getValue(ParkPlace.class);
                int index = getIndexOf(prk.getPid());
                if (index != -1) {
                    list.remove(index);
                    adapter.notifyItemRemoved(index);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public int getIndexOf(String key) {
        for (int i = 0; i < list.size(); i++) {
            ParkPlace mj = list.get(i);
            if (mj.getPid().equals(key)) {
                return list.indexOf(mj);
            }
        }
        return -1;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ending_time) {
            TimePickerDialog d = new TimePickerDialog(ctx,
                    timeEndSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), false);

            d.show();
        } else if (view.getId() == R.id.starting_time) {
            TimePickerDialog d = new TimePickerDialog(ctx,
                    timeStartSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), false);

            d.show();

        } else if (view.getId() == R.id.btn_filter) {
            long now = System.currentTimeMillis();
            long in = Long.parseLong(timeIn);
            if (in >= now) {
                if (!timeIn.equals(" ") && !timeOut.equals(" ")) {
                    if (utils.isValidTime(timeIn, timeOut)) {
                        if (list.size() == 0)
                            updateUi();
                        else {
                            clear();
                            updateUi();
                        }
                    } else {
                        Messege.messege(ctx, "Invalid time selection ");
                    }
                }
            } else
                Messege.messege(ctx, "Time in has passed ");

        }
    }

    public void clear() {
        final int size = list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                list.remove(0);
            }

            adapter.notifyItemRangeRemoved(0, size);
        }
    }
            /*--------------------------time picker listeners ----------------------------------------------*/

    private TimePickerDialog.OnTimeSetListener timeEndSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);
            endtv.setText(hourOfDay + " " + minute);
            String time = utils.getTimeStamp(formattedDate, hourOfDay, minute);
            timeOut = time;
            endtv.setText(utils.getDate(time).substring(11));

        }
    };

    private TimePickerDialog.OnTimeSetListener timeStartSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);
            String time = utils.getTimeStamp(formattedDate, hourOfDay, minute);
            timeIn = time;
            sttv.setText(utils.getDate(time).substring(11));
        }
    };
}
