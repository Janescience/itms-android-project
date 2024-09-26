package system.management.information.itms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

/**
 * Created by janescience on 8/8/2560.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mUsername;
    private EditText mPassword;
    private String status;

    private Button login;
    private TextView mLogin;
    Typeface Fonts;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedIntanceState) {

        super.onCreate(savedIntanceState);
        setContentView(R.layout.activity_login);

        bindView();

        if (isUserLogged()) {
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), BottombarMainActivity.class));
        }
    }

    private Boolean isUserLogged() {
        return firebaseAuth.getCurrentUser() != null;
    }

    private void bindView() {
        firebaseAuth = FirebaseAuth.getInstance();

        Fonts = Typeface.createFromAsset(this.getAssets(), "fonts/Kanit-Light.ttf");


        login = (Button) findViewById(R.id.button_login);
        mLogin = (TextView) findViewById(R.id.text_login);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);


        login.setTypeface(Fonts);
        mUsername.setTypeface(Fonts);
        mPassword.setTypeface(Fonts);
        mLogin.setTypeface(Fonts);

        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(this);
    }

    private void loginUser() {
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();


        if (TextUtils.isEmpty(username)) {
            Toast.makeText(LoginActivity.this, "กรุณากรอกอีเมลล์", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("กำลังเข้าสู่ระบบ...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "เข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), CheckLoginActivity.class));

                        } else {
                            Toast.makeText(LoginActivity.this, "ไม่สามารถเข้าสู่ระบบได้ กรุณาลองอีกครั้ง", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }

                });
    }


    @Override
    public void onClick(View view) {
        loginUser();
    }
}