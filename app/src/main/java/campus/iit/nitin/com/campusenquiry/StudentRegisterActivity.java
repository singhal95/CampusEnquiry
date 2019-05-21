package campus.iit.nitin.com.campusenquiry;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class StudentRegisterActivity extends AppCompatActivity {


    private Button save;
    private EditText name,  mobilenumber, sid;
    private TextView  department;
    private ImageView profilephoto;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    private SharedPreferences database;
    private SharedPreferences.Editor editor;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    String fileurl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
        save = findViewById(R.id.save);
        name = findViewById(R.id.namevalue);
        mobilenumber = findViewById(R.id.mobilenumbervalue);
        sid= findViewById(R.id.sid);
        department = findViewById(R.id.departmentvalue);
        profilephoto = findViewById(R.id.displayimage);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Students");
        database=getSharedPreferences("STUDENT",MODE_PRIVATE);
        editor=database.edit();

        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(StudentRegisterActivity.this);
                builderSingle.setTitle("Select Department:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StudentRegisterActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("CSE");
                arrayAdapter.add("IT");
                arrayAdapter.add("ECE");
                arrayAdapter.add("EEE");
                arrayAdapter.add("ME");
                arrayAdapter.add("Civil");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(StudentRegisterActivity.this);
                        builderInner.setMessage(strName);
                        department.setText(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();





            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onsave();
            }
        });
        profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
            }
        });
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(StudentRegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    fileurl=downloadUrl.toString();
                                    //Do what you want with the url
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(StudentRegisterActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilephoto.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void onsave(){
        if(!validate()){
            Toast.makeText(StudentRegisterActivity.this,"Please make sure all fields are field correctly",Toast.LENGTH_SHORT).show();
            return;
        }
        Student student;
        if(fileurl.equals("")){
          student=new Student(name.getText().toString(),sid.getText().toString(),department.getText().toString(),mobilenumber.getText().toString(),"TEST",database.getString("email","test"));
        }
        else {
          student = new Student(name.getText().toString(), sid.getText().toString(), department.getText().toString(), mobilenumber.getText().toString(), fileurl,database.getString("email","test"));
        }
        myRef.child(database.getString("userid","TEST")).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(StudentRegisterActivity.this,StudentMainScreen.class));
                finish();
            }
        });



    }

    public boolean validate(){
        boolean valid=true;
        if(name.getText().toString().isEmpty()){
            valid=false;
            name.setError("Please enter the name");
        }
        else {
            name.setError(null);
            valid=true;
        }
        if (sid.getText().toString().isEmpty()){
            valid=false;
            name.setError("Please enter the employee id");

        }
        else {
            name.setError(null);
            valid=true;
        }
        if (department.getText().toString().isEmpty()){
            valid=false;
            department.setError("Please enter the department");

        }
        else {
            department.setError(null);
            valid=true;
        }

        if (mobilenumber.getText().toString().isEmpty()){
            valid=false;
            mobilenumber.setError("Please enter the mobilenumber");

        }
        else {
            mobilenumber.setError(null);
            valid=true;
        }
        return valid;
    }
}
