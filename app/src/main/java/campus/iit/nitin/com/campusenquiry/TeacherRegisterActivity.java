package campus.iit.nitin.com.campusenquiry;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import java.security.Permission;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherRegisterActivity extends AppCompatActivity {


    private Button save;
    private EditText name, officenumber, mobilenumber, employeid;
    private TextView locationvalue, department;
    private ImageView profilephoto;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    List<Address> addresses;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    String fileurl="";
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    private SharedPreferences database;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);
        save = findViewById(R.id.save);
        name = findViewById(R.id.namevalue);
        officenumber = findViewById(R.id.officenumbervalue);
        mobilenumber = findViewById(R.id.mobilenumbervalue);
        employeid = findViewById(R.id.empid);
        locationvalue = findViewById(R.id.locationvalue);
        department = findViewById(R.id.departmentvalue);
        profilephoto = findViewById(R.id.displayimage);
        geocoder = new Geocoder(this, Locale.getDefault());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Teachers");
        database=getSharedPreferences("TEACHER",MODE_PRIVATE);
        editor=database.edit();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(TeacherRegisterActivity.this);
        locationvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(TeacherRegisterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TeacherRegisterActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }

                }
                else {
                    getlocation();
                }
            }
        });
        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(TeacherRegisterActivity.this);
                builderSingle.setTitle("Select Department:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TeacherRegisterActivity.this, android.R.layout.select_dialog_singlechoice);
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
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(TeacherRegisterActivity.this);
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
                            Toast.makeText(TeacherRegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(TeacherRegisterActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                getlocation();
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
            }

        }
    }

    @SuppressLint("MissingPermission")
    public void getlocation(){
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String completeaddress=address+" "+city+" "+state+" "+country+" "+postalCode;
                                locationvalue.setText(completeaddress);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    public void onsave(){
        if(!validate()){
            Toast.makeText(TeacherRegisterActivity.this,"Please make sure all fields are field correctly",Toast.LENGTH_SHORT).show();
            return;
        }
        Teacher teacher;
        if(fileurl.equals("")){
            teacher=new Teacher(name.getText().toString(),employeid.getText().toString(),"Block AB-3 , c-20/1 , JSSATEN sector 62 noida",department.getText().toString(),officenumber.getText().toString(),mobilenumber.getText().toString(),"TEST",database.getString("email","test"));
        }
        else {
           teacher = new Teacher(name.getText().toString(), employeid.getText().toString(), "Block AB-3 , c-20/1 , JSSATEN sector 62 noida", department.getText().toString(), officenumber.getText().toString(), mobilenumber.getText().toString(), fileurl,database.getString("email","test"));
        }
        myRef.child(database.getString("userid","TEST")).setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                   startActivity(new Intent(TeacherRegisterActivity.this,MainScreen.class));
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
        if (employeid.getText().toString().isEmpty()){
            valid=false;
            name.setError("Please enter the employee id");

        }
        else {
            name.setError(null);
            valid=true;
        }
        if (locationvalue.getText().toString().isEmpty()){
            valid=false;
            locationvalue.setError("Please enter location");

        }
        else {
          locationvalue.setError(null);
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
        if (officenumber.getText().toString().isEmpty()){
            valid=false;
            officenumber.setError("Please enter the officenumber");

        }
        else {
            officenumber.setError(null);
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
