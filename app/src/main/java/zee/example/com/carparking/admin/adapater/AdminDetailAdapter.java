package zee.example.com.carparking.admin.adapater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;

import zee.example.com.carparking.R;
import zee.example.com.carparking.models.Booked;
import zee.example.com.carparking.utilities.utils;

/**
 * Created by Zul Qarnain on 3/26/2018.
 */

public class AdminDetailAdapter extends RecyclerView.Adapter<AdminDetailAdapter.MyViewHolder> {
    ArrayList<Booked> data;
    Context ctx;
    TextView warnTv;
    public AdminDetailAdapter(Context ctx, ArrayList<Booked> data,TextView warnTv) {
        this.ctx = ctx;
        this.data = data;
        this.warnTv=warnTv;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_admin_booked_view,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindView(data.get(position));
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

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView timinTv;
        TextView timeoutTv;
        TextView des;
        ImageButton delBtn;

        Booked booked;
        View holderView;
        public MyViewHolder(View itemView) {
            super(itemView);
            timinTv = itemView.findViewById(R.id.starting_time);
            timeoutTv = itemView.findViewById(R.id.ending_time);
            delBtn = itemView.findViewById(R.id.btn_booking_remove);
            des = itemView.findViewById(R.id.booking_des);
            holderView=itemView;
        }

        public void bindView(final Booked booked) {
            this.booked=booked;
            String in= utils.getDate(booked.getTimeIn()).substring(11);
            String out= utils.getDate(booked.getTimeOut()).substring(11);
            des.setText("Booking :"+booked.getbid().substring(11,15));
            timinTv.setText(in);
            timeoutTv.setText(out);

        }
    }
}
