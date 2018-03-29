package zee.example.com.carparking.user;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import zee.example.com.carparking.R;
import zee.example.com.carparking.models.Booked;
import zee.example.com.carparking.service.ServiceError;
import zee.example.com.carparking.service.ServiceListener;
import zee.example.com.carparking.utilities.Messege;
import zee.example.com.carparking.utilities.utils;


/**
 * Created by Zul Qarnain on 2/14/2018.
 */

public class BookDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView prkStatus;
    private Button btnCancel;
    private Button btnClose;
    private DatabaseReference ref;
    private String parkingId;
    private String isBook;
    private String uid;
    private String bid;  //for checking the valid booking

    Dialog alertDialog;

    public static DialogFragment newInstance(String pid, String uid,String bid) {
        Bundle obj = new Bundle();
        obj.putString("pid", pid);
        obj.putString("uid", uid);
        obj.putString("bid", bid);
        BookDialogFragment frg = new BookDialogFragment();
        frg.setArguments(obj);
        return frg;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        alertDialog = new Dialog(getActivity());
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.booking_dialog_view);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ref = FirebaseDatabase.getInstance().getReference("allocted");
        parkingId = getArguments().getString("pid");
        uid = getArguments().getString("uid");
        bid = getArguments().getString("bid");

        btnClose = alertDialog.findViewById(R.id.btn_close);
        prkStatus = alertDialog.findViewById(R.id.prk_status);
        btnCancel = alertDialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
//               Messege.messege(getContext(), String.valueOf(timePicker.getMinute()));

            }
        });
        updateUi();
        return alertDialog;
    }

    private void updateUi() {
        utils.isParkBelong(parkingId, uid, new ServiceListener() {
            @Override
            public void success(Object obj) {
                Booked ob = (Booked) obj;
                if (ob.getUser().equals(uid) && ob.getPid().equals(parkingId) &&ob.getbid().equals(bid)) {
                    prkStatus.setText("Parking is booked");
                    btnCancel.setEnabled(true);
                    btnCancel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void fail(ServiceError error) {

            }
        });

        if(!bid.equals("")){
            prkStatus.setText("Parking is booked");
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_cancel) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("bookings");
            reference.child(bid).removeValue();
            alertDialog.dismiss();

        }
    }
}
