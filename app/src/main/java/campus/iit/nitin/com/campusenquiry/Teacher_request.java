package campus.iit.nitin.com.campusenquiry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("ValidFragment")
public class Teacher_request extends Fragment {



    itemteachertouch itemteachertouch;
    @SuppressLint("ValidFragment")
    public Teacher_request(itemteachertouch itemteachertouch) {
        this.itemteachertouch=itemteachertouch;
    }

    ListView listView;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    SharedPreferences database;
    SharedPreferences.Editor editor;
    ArrayList<String> studentid;
    ArrayList<String> studentname;
    ArrayList<StudentRequest> studentRequests;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_request, container, false);
        listView = view.findViewById(R.id.list);
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Teachers");
        database = getContext().getSharedPreferences("TEACHER", MODE_PRIVATE);
        editor = database.edit();
        studentid = new ArrayList<>();
        studentname = new ArrayList<>();
        studentRequests = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, studentname);
        listView.setAdapter(arrayAdapter);

        myRef.child(database.getString("userid", "TEST")).child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot dataSnapshot1 : dataSnapshots) {
                    StudentRequest studentRequest = dataSnapshot1.getValue(StudentRequest.class);
                    studentid.add(dataSnapshot1.getKey());
                    studentRequests.add(studentRequest);
                    studentname.add(studentRequest.getName());


                }
                arrayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editor.putString("Studentname", studentRequests.get(position).getName());
                editor.putString("Studentremarks", studentRequests.get(position).getRemaks());
                editor.putString("studentid", studentid.get(position));
                editor.commit();
                itemteachertouch.onitemteachertouch();
            }
        });


        return view;
    }
    public interface itemteachertouch{
        public void onitemteachertouch();
    }





}
