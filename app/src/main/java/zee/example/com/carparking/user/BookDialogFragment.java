package zee.example.com.carparking.user;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;

import zee.example.com.carparking.R;
import zee.example.com.carparking.utilities.Messege;


/**
 * Created by Zul Qarnain on 2/14/2018.
 */

public class BookDialogFragment extends DialogFragment {
    TextView tv;
    TimePicker timePicker;
    Button btnClose;

    public static DialogFragment newInstance(String pid) {
        Bundle obj = new Bundle();
        obj.putString("pid", pid);

        BookDialogFragment frg = new BookDialogFragment();
        frg.setArguments(obj);
        return frg;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog alertDialog = new Dialog(getActivity());
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.booking_dialog_view);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        String parkingId = getArguments().getString("pid");

        timePicker =alertDialog.findViewById(R.id.time_Picker);
        btnClose =alertDialog.findViewById(R.id.btn_close);
       /* tv = alertDialog.findViewById(R.id.parking_id);
        tv.setText(parkingId);*/
        /*
        Picasso.with(getActivity()).load(img).fit().into(prfView);*/

       /* btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });*/
       btnClose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               alertDialog.dismiss();
               Messege.messege(getContext(), String.valueOf(timePicker.getMinute()));

           }
       });
        return alertDialog;
    }


}
