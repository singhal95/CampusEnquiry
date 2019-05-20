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

public class SignupActivity extends AppCompatActivity {


    private FirebaseAuth mauth;
    private EditText emailText,passwordtext;
    private Button signupbutton;
    private TextView loginlink;
    private RadioGroup radioGroup;
    private TextView reenteredpassword;
    private RadioButton teacherbutton,studentbutton;
    private SharedPreferences database;
    private SharedPreferences.Editor editor;
    private SharedPreferences database1;
    private SharedPreferences.Editor editor1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mauth=FirebaseAuth.getInstance();
        emailText=findViewById(R.id.input_email);
        passwordtext=findViewById(R.id.input_password);
        signupbutton=findViewById(R.id.btn_signup);
       loginlink=findViewById(R.id.link_login);
        radioGroup=findViewById(R.id.category);
        teacherbutton=findViewById(R.id.teacher);
        studentbutton=findViewById(R.id.student);
        reenteredpassword=findViewById(R.id.input_reEnterPassword);
        database=getSharedPreferences("TEACHER",MODE_PRIVATE);
        editor=database.edit();
        database1=getSharedPreferences("STUDENT",MODE_PRIVATE);
        editor1=database1.edit();


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });

        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                finish();
            }
        });

    }

    public void signup(){
        if (!validate()) {
            onSignupFailed();
            return;
        }
        signupbutton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        final String email=emailText.getText().toString();
        final String password=passwordtext.getText().toString();
        String repassword=reenteredpassword.getText().toString();


        mauth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            editor.putString("email",email);
                            editor.putString("userid",mauth.getUid());
                            editor.putInt("category",0);
                            editor.putString("password",password);
                            editor.commit();

                            if(radioGroup.getCheckedRadioButtonId()==R.id.teacher){
                                editor.putString("email",email);
                                editor.putString("userid",mauth.getUid());
                                editor.putInt("category",0);
                                editor.putString("password",password);
                                editor.commit();

                                progressDialog.dismiss();
                                signupsuccesss();

                            }
                            else {
                                editor1.putString("email",email);
                                editor1.putString("userid",mauth.getUid());
                                editor1.putInt("category",0);
                                editor1.putString("password",password);
                                editor1.commit();

                                progressDialog.dismiss();
                               signupstudentsucess();
                            }





                        } else {

                            Toast.makeText(SignupActivity.this, "Successfully not  Created ", Toast.LENGTH_SHORT).show();
                            onSignupFailed();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });



    }

    public void signupstudentsucess(){
        signupbutton.setEnabled(true);
        startActivity(new Intent(SignupActivity.this,StudentRegisterActivity.class));
        finish();
    }

    public void signupsuccesss(){
      signupbutton.setEnabled(true);
        startActivity(new Intent(SignupActivity.this,TeacherRegisterActivity.class));
        finish();
    }
    public void onSignupFailed() {
        Toast.makeText(SignupActivity.this, "Login failed", Toast.LENGTH_LONG).show();
        signupbutton.setEnabled(true);
    }

    public  boolean validate(){
        boolean valid = true;

        String email = emailText.getText().toString();
        String password =passwordtext.getText().toString();
       String reEnterPassword = reenteredpassword.getText().toString();


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

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !reEnterPassword.equals(password)) {
            reenteredpassword.setError("Password Do not match");
            valid = false;
        } else {
            reenteredpassword.setError(null);

        }
         if(!teacherbutton.isChecked() && !studentbutton.isChecked())
        {
            valid=false;
        }

        return valid;
    }
}


