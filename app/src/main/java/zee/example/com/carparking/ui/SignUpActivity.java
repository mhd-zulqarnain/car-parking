package zee.example.com.carparking.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import zee.example.com.carparking.R;
import zee.example.com.carparking.utilities.Messege;
import zee.example.com.carparking.utilities.Validation;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private Button btSignUp;
    private EditText edName;
    private EditText edEmail;
    private EditText edPass;
    private ProgressBar barProgress;
    private final String TAG = "con.log.zeelog";
    private String uType;
    private String mKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edEmail = (EditText) findViewById(R.id.email);
        edPass = (EditText) findViewById(R.id.password);
        edName = (EditText) findViewById(R.id.name);
        btSignUp = (Button) findViewById(R.id.email_sign_up_button);
        barProgress = (ProgressBar) findViewById(R.id.signup_progress);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("auth");



    }

    public void signUp(View v) {
//        Messege.messege(getBaseContext(),"response "+response);
        String email = edEmail.getText().toString();
        String pass = edPass.getText().toString();
        final String name = edName.getText().toString();


        if (TextUtils.isEmpty(name)) {
            Messege.messege(getBaseContext(), "Enter the name ");
            return;
        }
        if (TextUtils.isEmpty(email) || !Validation.isEmailValid(email)) {
            Messege.messege(getBaseContext(), "Invalid email");
            return;
        }
        if (TextUtils.isEmpty(pass) || TextUtils.getTrimmedLength(pass) < 6) {
            Messege.messege(getBaseContext(), "invalid password");
            return;
        }
        barProgress.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            barProgress.setVisibility(View.GONE);
                            mKey = auth.getCurrentUser().getUid().toString();
                            HashMap<String, String> user = new HashMap<String, String>();
                            user.put("type", "user");
//                            user.put("disabled","false");
                            mDatabase.child(mKey).setValue(user);
                            //inserting name
                            UserProfileChangeRequest profileName = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            auth.getCurrentUser().updateProfile(profileName);
                            Messege.messege(getBaseContext(), "Successfully signed up");

                            //----storing access token------
                              /*  String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                                utils.updateFcm(refreshedToken);*/
                            //------------------------------

                            updataUi(profileName.getDisplayName());

                        } else {
                            barProgress.setVisibility(View.GONE);
                            Messege.messege(getBaseContext(), "Failed");
                            Log.d(TAG, "onComplete: " + task.getException());
                        }

                        // ...
                    }
                });
    }

    public void updataUi(String name) {
        HashMap<String, String> user = new HashMap<String, String>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
            user.put("name", name);
            user.put("email", edEmail.getText().toString());
            user.put("uid", auth.getCurrentUser().getUid());
            mDatabase.child("users").child(mKey).setValue(user);
//            Intent intent = new  Intent(SignUpActivity.this, StudentActivity.class);
               /* intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent );
            finish();*/

    }

    @Override
    protected void onStart() {
       super.onStart();
       /*  if (auth.getCurrentUser() != null) {
            mKey = auth.getCurrentUser().getUid().toString();

            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(mKey).child("type");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String val = dataSnapshot.getValue().toString();
                    if (val.equals("user")) {
                        startActivity(new Intent(SignUpActivity.this, UserHomeActivity.class));
                        finish();


                    } else if (val.equals("admin")) {
                        startActivity(new Intent(SignUpActivity.this, AdminHomeActivitysd.class));
                        finish();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }*/
    }
}

