package zee.example.com.carparking.user.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import zee.example.com.carparking.R;
import zee.example.com.carparking.models.Booked;
import zee.example.com.carparking.models.ParkPlace;
import zee.example.com.carparking.service.ServiceError;
import zee.example.com.carparking.service.ServiceListener;
import zee.example.com.carparking.user.BookDialogFragment;
import zee.example.com.carparking.utilities.Messege;
import zee.example.com.carparking.utilities.utils;

/**
 * Created by Zul Qarnain on 3/18/2018.
 */

public class ParkingAreaAdapter extends RecyclerView.Adapter<ParkingAreaAdapter.MyViewHolder> {

    ArrayList<ParkPlace> data;
    Context ctx;
    public static int PrkCount = 0;
    String timeIn = " ";
    String timeOut = " ";
    TextView warnTv;

    public ParkingAreaAdapter(Context ctx, ArrayList<ParkPlace> data, String timeIn, String timeOut, TextView warnTv) {
        this.ctx = ctx;
        this.data = data;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.warnTv = warnTv;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_main_parking_view
                , parent,
                false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.tv.setText(data.get(position).getAlocated());
        if (data.size() == 0) {
            warnTv.setText("No parking avaliable");
            warnTv.setVisibility(View.VISIBLE);
        } else
            warnTv.setVisibility(View.GONE);
        holder.bindView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /* TextView sttv;
         TextView endtv;*/
        TextView desTv;
        Button bookBtn;
        ImageButton detailBtn;
        ParkPlace parkPlace;
        View holderView;
        DatabaseReference ref;


        public MyViewHolder(View itemView) {
            super(itemView);

            desTv = itemView.findViewById(R.id.parking_des);
            bookBtn = itemView.findViewById(R.id.book_btn);
            detailBtn = itemView.findViewById(R.id.detail_btn);
            holderView = itemView;
            detailBtn.setOnClickListener(this);
            bookBtn.setOnClickListener(this);

        }

        public void bindView(final ParkPlace parkPlace) {
            this.parkPlace = parkPlace;
            int postion = getAdapterPosition() + 1;
            desTv.setText("Parking place " + postion);
            /*if (parkPlace.getAlocated().equals("false")) {
                holderView.setBackgroundColor(Color.GREEN);
                bookBtn.setEnabled(true);
            } else {
                holderView.setBackgroundColor(Color.RED);
                bookBtn.setEnabled(false);
                checkExpiry(parkPlace.getPid());
            }*/
            Long in = Long.parseLong(timeIn);
            Long out = Long.parseLong(timeOut);
            utils.isBooked(parkPlace.getPid(), in, out, new ServiceListener() {
                @Override
                public void success(Object obj) {
                    Messege.messege(ctx, "Book1");
                    Booked booked = (Booked) obj;
                    Log.d("", "success: " + booked.getPid());
                    if (booked.getPid().equals(parkPlace.getPid())) {
                        bookBtn.setBackgroundColor(Color.RED);
                        bookBtn.setEnabled(false);
                    }
                }

                @Override
                public void fail(ServiceError error) {

                }
            });

        }

        private void checkExpiry(String pid) {

            utils.isExpire(pid, new ServiceListener() {
                @Override
                public void success(Object obj) {
                    if (!obj.equals(null)) {
                        Booked res = (Booked) obj;
                        Long timeOut = Long.parseLong(res.getTimeOut());
                        Long cuurentTime = System.currentTimeMillis();
                        Log.d("", "show current: " + cuurentTime);
                        if (cuurentTime > timeOut) {
                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("parking");
                            mRef.child(res.getPid()).child("alocated").setValue("false");
                        }

                    }
                }

                @Override
                public void fail(ServiceError error) {
                    Messege.messege(ctx, "error in call back");
                }
            });
        }

        @Override
        public void onClick(View view) {

            if (view.getId() == R.id.detail_btn) {
                DialogFragment dialog = BookDialogFragment.newInstance(parkPlace.getPid(), utils.getActiveUserUid(), parkPlace.getAlocated());
                dialog.show(((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction(), "mydialog");

            } //**did booking*//*
            else if (view.getId() == R.id.book_btn) {
                ref = FirebaseDatabase.getInstance().getReference("bookings");
                String parkId = parkPlace.getPid();
                String area = parkPlace.getArea();
                String bookingId = ref.push().getKey();
                Booked bkprk = new Booked(timeIn, timeOut, parkId, utils.getActiveUserUid(), area, bookingId);
                ref.child(bookingId).setValue(bkprk);

                /*ref = FirebaseDatabase.getInstance().getReference("parking");
                ref.child(parkId).child("alocated").setValue("true");
                Messege.messege(ctx, "Booked ");*/
            }
        }
    }
}
