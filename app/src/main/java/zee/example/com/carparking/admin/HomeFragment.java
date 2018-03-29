package zee.example.com.carparking.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import zee.example.com.carparking.R;
import zee.example.com.carparking.admin.adapater.ParkingAdminAdapter;
import zee.example.com.carparking.models.ParkPlace;
import zee.example.com.carparking.utilities.Messege;

import static android.content.ContentValues.TAG;

/**
 * Created by Zul Qarnain on 3/14/2018.
 */

public class HomeFragment extends Fragment {

    TextView tv;
    private ImageButton addBtn;
    DatabaseReference ref;
    private String area;
    private ParkingAdminAdapter adapter;
    private ArrayList<ParkPlace> pList;
    RecyclerView rv;

    public static HomeFragment newInstance(String area) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        args.putString("area", area);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_admin_frgment_view, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tv = view.findViewById(R.id.fragement_des);
        rv = view.findViewById(R.id.admin_recycler);
        area = getArguments().getString("area");
        ref = FirebaseDatabase.getInstance().getReference("parking");
        updateUi();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        addBtn = view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pList.size() <3) {
                    String key = ref.push().getKey();
                    ParkPlace model = new ParkPlace(area, "Parking "+key.substring(9,12), key);
                    ref.child(key).setValue(model);
                }else
                Messege.messege(getActivity(), "no more area left");

            }
        });
        tv.setText(getArguments().getString("area"));
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateUi() {
        pList = new ArrayList<>();
        adapter = new ParkingAdminAdapter(getActivity(), pList);
        rv.setAdapter(adapter);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ParkPlace plc = dataSnapshot.getValue(ParkPlace.class);
                Log.d(TAG, "onChildAdded: " + area);
                if (plc.getArea().equals(area)) {
                    pList.add(plc);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ParkPlace prk = dataSnapshot.getValue(ParkPlace.class);
                int index = getIndexOf(prk.getPid());
                if (index != -1) {
                    pList.remove(index);
                    pList.add(index,prk);
                    adapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ParkPlace plc = dataSnapshot.getValue(ParkPlace.class);
                Log.d(TAG, "onChildAdded: " + area);
                int index =getIndexOf(plc.getPid());
                if (plc.getArea().equals(area) && index!=-1 ) {
                    pList.remove(index);
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
        for (int i = 0; i < pList.size(); i++) {
            ParkPlace mj = pList.get(i);
            if (mj.getPid().equals(key)) {
                return pList.indexOf(mj);
            }
        }
        return -1;
    }
}
