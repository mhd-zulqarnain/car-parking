package zee.example.com.carparking.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import zee.example.com.carparking.R;
import zee.example.com.carparking.models.Area;
import zee.example.com.carparking.models.ParkPlace;
import zee.example.com.carparking.user.adapter.ParkingAreaAdapter;
import zee.example.com.carparking.utilities.Messege;

public class ParkingAreaActivity extends AppCompatActivity {

    private String aid;
    private RecyclerView rv;
    private ParkingAreaAdapter adapter;
    private ArrayList<ParkPlace> list;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_area);

        aid = getIntent().getStringExtra("area");
        setTitle("Parking detail of " + aid);
        rv = findViewById(R.id.recycler_parking_activity);

        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new ParkingAreaAdapter(this, list);
        rv.setAdapter(adapter);
        updateUi();
    }

    private void updateUi() {
        ref = FirebaseDatabase.getInstance().getReference("parking");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ParkPlace a = dataSnapshot.getValue(ParkPlace.class);
                if (a.getArea().equals(aid)) {
                    list.add(a);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ParkPlace prk = dataSnapshot.getValue(ParkPlace.class);
                int index = getIndexOf(prk.getPid());
                if (index != -1) {
                    list.remove(index);
                    list.add(index,prk);
                    adapter.notifyItemChanged(index);
                }
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
    public int getIndexOf(String key) {
        for (int i = 0; i < list.size(); i++) {
            ParkPlace mj = list.get(i);
            if (mj.getPid().equals(key)) {
                return list.indexOf(mj);
            }
        }
        return -1;
    }

}
