package zee.example.com.carparking.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
import zee.example.com.carparking.ui.LoginActivity;
import zee.example.com.carparking.ui.SignUpActivity;
import zee.example.com.carparking.user.ParkingAreaActivity;
import zee.example.com.carparking.user.UserHomeActivity;
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
                int index = getIndexOf(prk.getbid());
                if (index != -1) {
                    list.remove(index);
                    adapter.customNotifyRemove(index);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_menu_logout) {
            FirebaseAuth auth =FirebaseAuth.getInstance();
            auth.signOut();
            Intent intent = new Intent(AdminParkingDetail.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }
        return true;
    }
}
