package campus.iit.nitin.com.campusenquiry;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("ValidFragment")
public class Student_Profile extends Fragment {


    ImageView imageView;
    TextView name, sid, department, mobilenumber, remark;
    Button Accept, decline;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    DatabaseReference myRef1;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    List<Address> addresses;
    SharedPreferences database;
    SharedPreferences.Editor editor;
action action;
    @SuppressLint("ValidFragment")
    public Student_Profile(action action) {
        this.action=action;
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student__profile, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }

        }
        else {
          getlocation();
        }
        imageView = view.findViewById(R.id.displayimage);
        name = view.findViewById(R.id.namevalue);
        sid = view.findViewById(R.id.sid);
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Teachers");
        myRef1 = firebasedatabase.getReference("Students");
        database = getContext().getSharedPreferences("TEACHER", MODE_PRIVATE);
        editor = database.edit();
        department = view.findViewById(R.id.departmentvalue);
        mobilenumber = view.findViewById(R.id.mobilenumbervalue);
        remark = view.findViewById(R.id.remarks);
        remark.setText(database.getString("Studentremarks","TEST"));
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Showing Profile");
        progressDialog.show();
        myRef1.child(database.getString("studentid","TEST")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue(String.class));
                mobilenumber.setText(dataSnapshot.child("mobilenumber").getValue(String.class));
                department.setText(dataSnapshot.child("department").getValue(String.class));
                sid.setText(dataSnapshot.child("sid").getValue(String.class));
                String imageurl = dataSnapshot.child("photourl").getValue(String.class);
                if (!imageurl.equals("TEST")) {
                    Picasso.get().load(imageurl).into(imageView);
                }
                else {

                    Picasso.get().load(R.drawable.user_img).into(imageView);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Accept = view.findViewById(R.id.accept);
        decline = view.findViewById(R.id.decline);
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Adding To Student");
                progressDialog.show();

                myRef.child(database.getString("userid", "TEST")).child("Request").child(database.getString("studentid", "TEST")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        myRef.child(database.getString("userid", "TEST")).child("Student").child(database.getString("studentid", "TEST")).setValue(database.getString("Studentremarks", "TEST")).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                myRef1.child(database.getString("studentid", "TEST")).child("Teacher").child(database.getString("userid", "TEST")).setValue(database.getString("email", "TEST")).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Successfull added", Toast.LENGTH_SHORT).show();

                                        Log.i("nitin123",Constant.location);

                                        myRef.child(database.getString("userid", "TEST")).child("location").setValue("Block AB-3 , c-20/1 , JSSATEN sector 62 noida").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "Location Suceessfully updated", Toast.LENGTH_SHORT).show();

                                          progressDialog.dismiss();
                                            }
                                        });

                                    }
                                });
                            }
                        });

                    }
                });

            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Decline To Student");
                progressDialog.show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Any Remarks For Decline");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String m_Text = input.getText().toString();
                        myRef.child(database.getString("userid", "TEST")).child("Request").child(database.getString("studentid", "TEST")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                myRef1.child(database.getString("studentid", "TEST")).child("Teacher_DECLINE").child(database.getString("userid", "TEST")).setValue(m_Text).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "Successfull Decliend", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();

                                            }
                                        });

                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });
        return view;
    }

    @SuppressLint("MissingPermission")
    public String getlocation() {
        final String[] completeaddress=new String[1];

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String address = addresses.get(0).getAddressLine(0);
                                Constant.location=address;
                                Log.i("nitin123",Constant.location);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                completeaddress[0]=address + " " + city + " " + state + " " + country + " " + postalCode;




                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
        return completeaddress[0];
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
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
            }

        }
    }


    public interface action{
        public void onAction();
    }
}
