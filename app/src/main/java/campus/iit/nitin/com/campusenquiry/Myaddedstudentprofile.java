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

import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("ValidFragment")
public class Myaddedstudentprofile extends Fragment {


    ImageView imageView;
    TextView name, sid, department, mobilenumber, remark;
    Button chat;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    DatabaseReference myRef1;
    SharedPreferences database;
    SharedPreferences.Editor editor;
    String toname,toid;
    myaddedstudenttouch myaddedstudenttouch;

    @SuppressLint("ValidFragment")
    public Myaddedstudentprofile(myaddedstudenttouch myaddedstudenttouch){
        this.myaddedstudenttouch=myaddedstudenttouch;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_myaddedstudentprofile, container, false);
        imageView = view.findViewById(R.id.displayimage);
        name = view.findViewById(R.id.namevalue);
        sid = view.findViewById(R.id.sid);
        department = view.findViewById(R.id.departmentvalue);
        mobilenumber = view.findViewById(R.id.mobilenumbervalue);
        remark = view.findViewById(R.id.remarks);
        chat=view.findViewById(R.id.chat);
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Teachers");
        myRef1 = firebasedatabase.getReference("Students");
        database = getContext().getSharedPreferences("TEACHER", MODE_PRIVATE);
        editor = database.edit();
        remark.setText(database.getString("Studentremarks","TEST"));
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Showing Profile");
        progressDialog.show();
        myRef1.child(database.getString("studentid","TEST")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toname=dataSnapshot.child("name").getValue(String.class);
                name.setText(toname);
                mobilenumber.setText(dataSnapshot.child("mobilenumber").getValue(String.class));
                department.setText(dataSnapshot.child("department").getValue(String.class));
                toid=dataSnapshot.child("sid").getValue(String.class);
                sid.setText(toid);
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
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("chattoname",toname);
                editor.putString("chatoid",database.getString("studentid","TEST"));
                editor.commit();
                myaddedstudenttouch.onmyaddedstudenttouch1();
            }
        });
        return view;
    }

public interface myaddedstudenttouch{
        public void onmyaddedstudenttouch1();
}

}
