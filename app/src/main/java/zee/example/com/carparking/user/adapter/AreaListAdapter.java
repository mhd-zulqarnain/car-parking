package zee.example.com.carparking.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zee.example.com.carparking.R;
import zee.example.com.carparking.admin.adapater.ParkingAdminAdapter;
import zee.example.com.carparking.models.Area;
import zee.example.com.carparking.models.ParkPlace;
import zee.example.com.carparking.service.ServiceError;
import zee.example.com.carparking.service.ServiceListener;
import zee.example.com.carparking.user.ParkingAreaActivity;
import zee.example.com.carparking.user.UserHomeActivity;
import zee.example.com.carparking.utilities.Messege;
import zee.example.com.carparking.utilities.utils;

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.MyViewHolder>{
    ArrayList<Area> data;
    Context ctx;
    public static  int PrkCount =0;
    public AreaListAdapter(Context ctx, ArrayList<Area> data) {
        this.ctx = ctx;
        this.data = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_area_row,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String a= data.get(position).getAid();
        holder.ttitle.setText(a);
        holder.bindView(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ttitle;
        TextView count;
        Area area;
        View v;
        public MyViewHolder(View v) {
            super(v);
            this.v=v;
            ttitle= v.findViewById(R.id.area_title);
            count= v.findViewById(R.id.area_park_available_text);

        }

        public void bindView(final Area area) {
            this.area = area;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, ParkingAreaActivity.class);
                    intent.putExtra("area",area.getAid());
                    ctx.startActivity(intent);
                }
            });


//            count.setText(String.valueOf(PrkCount));
            /*utils.getParkCount(area.getAid(), new ServiceListener<String>(){

                @Override
                public void success(String obj) {
                    PrkCount+=Integer.valueOf(obj);
                    count.setText(String.valueOf(PrkCount));
                }

                @Override
                public void fail(ServiceError error) {
                    Messege.messege(ctx,"error");
                }
            });*/
        }
    }}