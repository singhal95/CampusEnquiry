package campus.iit.nitin.com.campusenquiry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mauth;
    private EditText emailText,passwordtext;
    private Button loginButton;
    private TextView signuplink;
    private RadioGroup radioGroup;
    private RadioButton teacherbutton,studentbutton;
    private SharedPreferences database;
    private SharedPreferences.Editor editor;
    private SharedPreferences database1;
    private SharedPreferences.Editor editor1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       mauth=FirebaseAuth.getInstance();
       emailText=findViewById(R.id.input_email);
       passwordtext=findViewById(R.id.input_password);
       signuplink=findViewById(R.id.link_signup);
       loginButton=findViewById(R.id.btn_login);
       radioGroup=findViewById(R.id.category);
       teacherbutton=findViewById(R.id.teacher);
       studentbutton=findViewById(R.id.student);
        database=getSharedPreferences("TEACHER",MODE_PRIVATE);
        editor=database.edit();
        database1=getSharedPreferences("STUDENT",MODE_PRIVATE);
        editor1=database1.edit();
       signuplink.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,SignupActivity.class));
               finish();
           }
       });
       loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             login();
           }
       });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


   public void onLoginFailed() {
        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();

       loginButton.setEnabled(true);
    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = emailText.getText().toString();
        final String password = passwordtext.getText().toString();

        mauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            if(radioGroup.getCheckedRadioButtonId()==R.id.teacher){
                                editor.putString("email",email);
                                editor.putString("userid",mauth.getUid());
                                editor.putInt("category",0);
                                editor.putString("password",password);
                                editor.commit();
                                progressDialog.dismiss();
                               onLoginSuccess();

                            }
                            else {
                                editor1.putString("email",email);
                                editor1.putInt("category",1);
                                editor1.putString("userid",mauth.getUid());
                                editor1.putString("password",password);
                                editor1.commit();
                                progressDialog.dismiss();
                               onLoginstudentsucess();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            onLoginFailed();
                            progressDialog.dismiss();

                        }

                        // ...
                    }
                });




    }
   private void onLoginstudentsucess(){
       loginButton.setEnabled(true);
       startActivity(new Intent(MainActivity.this,StudentMainScreen.class));
       finish();
    }

    private void onLoginSuccess() {
      loginButton.setEnabled(true);
        startActivity(new Intent(MainActivity.this,MainScreen.class));
        finish();
    }
    public boolean  validate(){
        boolean valid = true;

        String email = emailText.getText().toString();
        String password =passwordtext.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordtext.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordtext.setError(null);
        }

    if(!teacherbutton.isChecked() && !studentbutton.isChecked())
        {
            valid=false;
        }


        return valid;
    }
}
