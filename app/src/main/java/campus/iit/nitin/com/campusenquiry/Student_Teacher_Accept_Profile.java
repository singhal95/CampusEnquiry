package campus.iit.nitin.com.campusenquiry;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("ValidFragment")
public class Student_Teacher_Accept_Profile extends Fragment {



    TextView name,empid,location,department,phonenumber,officenumberl,email;
    ImageView imageView;
    Button Chat;

    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    SharedPreferences database;
    SharedPreferences.Editor editor;
    String imageurl;
    String toname,toid;
    studentteacherchat studentteacherchat;
    @SuppressLint("ValidFragment")
    public Student_Teacher_Accept_Profile(studentteacherchat studentteacherchat) {
        this.studentteacherchat=studentteacherchat;
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_student__teacher__accept__profile, container, false);
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Teachers");
        database = getContext().getSharedPreferences("STUDENT", MODE_PRIVATE);
        editor = database.edit();
        name=view.findViewById(R.id.namevalue);
        empid=view.findViewById(R.id.empid);
        location=view.findViewById(R.id.locationvalue);
        department=view.findViewById(R.id.departmentvalue);
        officenumberl=view.findViewById(R.id.officenumbervalue);
        phonenumber=view.findViewById(R.id.mobilenumbervalue);
        imageView=view.findViewById(R.id.displayimage);
        Chat=view.findViewById(R.id.chat);
        email=view.findViewById(R.id.emailvalue);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Showing Profile...");
        progressDialog.show();
        myRef.child(database.getString("teacherid","TEST")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    toname=dataSnapshot.child("name").getValue(String.class);
                    name.setText(toname);
                    email.setText(dataSnapshot.child("email").getValue(String.class));
                    empid.setText(dataSnapshot.child("empid").getValue(String.class));
                    location.setText(dataSnapshot.child("location").getValue(String.class));
                    department.setText(dataSnapshot.child("department").getValue(String.class));
                    officenumberl.setText(dataSnapshot.child("officenumber").getValue(String.class));
                    phonenumber.setText(dataSnapshot.child("mobilenumber").getValue(String.class));
                    imageurl = dataSnapshot.child("profilephotourl").getValue(String.class);
                    if (!imageurl.equals("TEST")) {
                        Picasso.get().load(imageurl).into(imageView);
                    }
                    else {
                        Picasso.get().load(R.drawable.user_img).into(imageView);
                    }

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("chattoname",toname);
                editor.putString("chatoid",database.getString("teacherid","TEST"));
                editor.commit();
                studentteacherchat.onstudentteacherchattouch();
            }
        });


        return view;
    }

    public interface studentteacherchat{
        public void onstudentteacherchattouch();
    }

}
