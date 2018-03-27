package zee.example.com.carparking.admin.adapater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
import zee.example.com.carparking.admin.AdminParkingDetail;
import zee.example.com.carparking.models.ParkPlace;
import zee.example.com.carparking.utilities.utils;

/**
 * Created by Zul Qarnain on 3/14/2018.
 */

public class ParkingAdminAdapter extends RecyclerView.Adapter<ParkingAdminAdapter.MyViewHolder> {

    ArrayList<ParkPlace> data;
    Context ctx;
    public ParkingAdminAdapter(Context ctx, ArrayList<ParkPlace> data) {
        this.ctx = ctx;
        this.data = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_park_admin_view,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView desTv;
        ImageButton cancelBtn;
        ImageButton detBtn;
        ParkPlace parkPlace;
        View holderView;
        DatabaseReference ref;
        View itemView;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            desTv = itemView.findViewById(R.id.parking_des);
            cancelBtn = itemView.findViewById(R.id.del_btn);
            detBtn = itemView.findViewById(R.id.det_btn);
            holderView=itemView;
        }

        public void bindView(final ParkPlace parkPlace) {
            this.parkPlace=parkPlace;
            int postion = getAdapterPosition() + 1;
            desTv.setText(parkPlace.getDescription());

          itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(ctx, AdminParkingDetail.class);
                  intent.putExtra("bid",parkPlace.getPid());
                  ctx.startActivity(intent);
              }
          });
          detBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(ctx, AdminParkingDetail.class);
                  intent.putExtra("bid",parkPlace.getPid());
                  ctx.startActivity(intent);
              }
          });
            cancelBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("parking");
                    reference.child(parkPlace.getPid()).removeValue();
                    utils.removeParkBooking(parkPlace.getPid(),"flag");
                }
            });

        }
    }
}
