package zee.example.com.carparking.admin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import zee.example.com.carparking.R;
import zee.example.com.carparking.admin.adapater.AdminDetailAdapter;
import zee.example.com.carparking.models.Booked;
import zee.example.com.carparking.models.ParkPlace;
import zee.example.com.carparking.user.adapter.ParkingAreaAdapter;

public class AdminParkingDetail extends AppCompatActivity {
    private String pid;
    private RecyclerView rv;
    private AdminDetailAdapter adapter;
    private ArrayList<Booked> list;
    DatabaseReference ref;
    TextView warnTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_parking_detail);
        rv = findViewById(R.id.admin_detail_parking_recycler);
        warnTv = findViewById(R.id.txt_warn);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        pid= getIntent().getStringExtra("bid");
        Log.d("", "onCreate: "+pid);
        updateUi();

    }

    private void updateUi() {

        adapter = new AdminDetailAdapter(this, list,warnTv);
        rv.setAdapter(adapter);
        ref = FirebaseDatabase.getInstance().getReference("bookings");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Booked a = dataSnapshot.getValue(Booked.class);
                if (a.getPid().equals(pid)) {
                    list.add(a);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Booked prk = dataSnapshot.getValue(Booked.class);
                int index = getIndexOf(prk.getbid());
                if (index != -1) {
                    list.remove(index);
                    list.add(index, prk);
                    adapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Booked prk = dataSnapshot.getValue(Booked.class);
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
            Booked mj = list.get(i);
            if (mj.getbid().equals(key)) {
                return list.indexOf(mj);
            }
        }
        return -1;
    }
}
