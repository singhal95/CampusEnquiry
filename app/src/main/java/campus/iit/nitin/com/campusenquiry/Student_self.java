package campus.iit.nitin.com.campusenquiry;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


public class Student_self extends Fragment {



    TextView depatment,name,sid,mobile,email;
    ImageView imageView;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    SharedPreferences database;
    SharedPreferences.Editor editor;
    String imageurl;
    public Student_self() {
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
        View view= inflater.inflate(R.layout.fragment_student_self, container, false);
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Students");
        database = getContext().getSharedPreferences("STUDENT", MODE_PRIVATE);
        editor = database.edit();
        name=view.findViewById(R.id.namevalue);
        depatment=view.findViewById(R.id.departmentvalue);
        sid=view.findViewById(R.id.sid);
        email=view.findViewById(R.id.emailvalue);
        mobile=view.findViewById(R.id.mobilenumbervalue);
        imageView=view.findViewById(R.id.displayimage);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        myRef.child(database.getString("userid","TEST")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    String namevalue=dataSnapshot.child("name").getValue(String.class);
                    name.setText(namevalue);
                    editor.putString("username",namevalue);
                    editor.commit();
                    email.setText(dataSnapshot.child("email").getValue(String.class));
                 depatment.setText(dataSnapshot.child("department").getValue(String.class));
                    mobile.setText(dataSnapshot.child("mobilenumber").getValue(String.class));
                    sid.setText(dataSnapshot.child("sid").getValue(String.class));
                    imageurl = dataSnapshot.child("photourl").getValue(String.class);
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

        return view;
    }



}
