package zee.example.com.carparking.user;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
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
import zee.example.com.carparking.ui.LoginActivity;
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
    String formattedDate = " ";

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
            if (formattedDate.equals(" ")) {
                Messege.messege(ctx, "Select the date ");
            } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_menu_logout) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            Intent intent = new Intent(ParkingAreaActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return true;
    }

    public void selectDate(View v) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

            /*--------------------------time picker listeners ----------------------------------------------*/

    private TimePickerDialog.OnTimeSetListener timeEndSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            endtv.setText(hourOfDay + " " + minute);
            String time = utils.getTimeStamp(formattedDate, hourOfDay, minute);
            timeOut = time;
            endtv.setText(utils.getDate(time).substring(11));

        }
    };

    private TimePickerDialog.OnTimeSetListener timeStartSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String time = utils.getTimeStamp(formattedDate, hourOfDay, minute);
            timeIn = time;
            sttv.setText(utils.getDate(time).substring(11));
        }
    };

                /*--------------------------date picker listeners ----------------------------------------------*/

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
            Messege.messege(ctx, selectedDay + " " + selectedMonth + " " + " " + selectedDay);
            if(selectedMonth>10)
            formattedDate = selectedDay + "-" + selectedMonth + "-" + selectedYear;
            else {
                formattedDate = selectedDay + "-" + "0" + selectedMonth + "-" + selectedYear;
            }
            titleDate.setText("Booking Date: " +formattedDate);

        }
    };
}
