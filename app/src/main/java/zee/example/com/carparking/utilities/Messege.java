package zee.example.com.carparking.utilities;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Zul Qarnain on 11/3/2017.
 */

public class Messege {
    public static void messege(Context context,String args){
        Toast.makeText(context,args,Toast.LENGTH_LONG).show();
    }
}
