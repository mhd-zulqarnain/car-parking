package zee.example.com.carparking.user.adapter;

import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import zee.example.com.carparking.R;
import zee.example.com.carparking.models.BookedParking;
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

    public ParkingAreaAdapter(Context ctx, ArrayList<ParkPlace> data) {
        this.ctx = ctx;
        this.data = data;
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
        holder.bindView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sttv;
        TextView endtv;
        TextView desTv;
        Button bookBtn;
        ImageButton detailBtn;
        ParkPlace parkPlace;
        View holderView;
        DatabaseReference ref;
        String timeIn = " ";
        String timeOut = " ";
        Calendar calendar = Calendar.getInstance();

        public MyViewHolder(View itemView) {
            super(itemView);
            endtv = itemView.findViewById(R.id.ending_time);
            sttv = itemView.findViewById(R.id.starting_time);
            desTv = itemView.findViewById(R.id.parking_des);
            bookBtn = itemView.findViewById(R.id.book_btn);
            detailBtn = itemView.findViewById(R.id.detail_btn);
            holderView = itemView;

            sttv.setOnClickListener(this);
            endtv.setOnClickListener(this);
            detailBtn.setOnClickListener(this);
            bookBtn.setOnClickListener(this);

        }

        public void bindView(final ParkPlace parkPlace) {
            this.parkPlace = parkPlace;
            int postion = getAdapterPosition() + 1;
            desTv.setText("Parking place " + postion);
            if (parkPlace.getAlocated().equals("false")) {
                holderView.setBackgroundColor(Color.GREEN);
                sttv.setEnabled(true);
                endtv.setEnabled(true);
                bookBtn.setEnabled(true);
            } else {
                holderView.setBackgroundColor(Color.RED);
                sttv.setEnabled(false);
                endtv.setEnabled(false);
                bookBtn.setEnabled(false);
                checkExpiry(parkPlace.getPid());
            }

        }

        private void checkExpiry(String pid) {

            utils.isExpire(pid, new ServiceListener() {
                @Override
                public void success(Object obj) {
                    if(!obj.equals(null))
                    {
                        BookedParking res= (BookedParking) obj;
                        Long timeOut =Long.parseLong(res.getTimeOut());
                        Long cuurentTime=System.currentTimeMillis();
                        Log.d("", "show current: "+cuurentTime);
                        if(cuurentTime>timeOut){
                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("parking");
                            mRef.child(res.getPid()).child("alocated").setValue("false");
                        }

                    }
                }

                @Override
                public void fail(ServiceError error) {
                    Messege.messege(ctx,"error in call back");
                }
            });
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

            }/*cancel booking*/
            else if (view.getId() == R.id.detail_btn) {
                DialogFragment dialog = BookDialogFragment.newInstance(parkPlace.getPid(),utils.getActiveUserUid(),parkPlace.getAlocated());
                dialog.show(((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction(), "mydialog");


            } /*did booking*/
            else if (view.getId() == R.id.book_btn) {
                if (!timeIn.equals(" ") && !timeOut.equals(" ")) {
                    if (utils.isValidTime(timeIn, timeOut)) {
                        String parkId = parkPlace.getPid();
                        ref = FirebaseDatabase.getInstance().getReference("allocted");
                        BookedParking bkprk = new BookedParking(timeIn, timeOut, parkId, utils.getActiveUserUid());
                        ref.child(parkId).setValue(bkprk);
                        ref = FirebaseDatabase.getInstance().getReference("parking");
                        ref.child(parkId).child("alocated").setValue("true");
                        Messege.messege(ctx, "Booked ");
                    } else {
                        Messege.messege(ctx, "Invalid time selection ");
                    }
                } else {
                    Messege.messege(ctx, "Select both time ");

                }


            }

        }

        /*--------------------------time picker listeners ----------------------------------------------*/
        private TimePickerDialog.OnTimeSetListener timeEndSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                endtv.setText(hourOfDay + " " + minute);
                String time = utils.getTimeStamp(formattedDate, hourOfDay, minute);
                    timeOut = time;
                endtv.setText(utils.getDate(time));

            }
        };

        private TimePickerDialog.OnTimeSetListener timeStartSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                String time = utils.getTimeStamp(formattedDate, hourOfDay, minute);
                timeIn = time;
                sttv.setText(utils.getDate(time));
            }
        };

    }

}
