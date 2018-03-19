package zee.example.com.carparking.admin.adapater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import zee.example.com.carparking.models.ParkPlace;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(data.get(position).getArea());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv= (TextView) itemView;
        }
    }
}
