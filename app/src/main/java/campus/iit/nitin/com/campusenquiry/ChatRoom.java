package campus.iit.nitin.com.campusenquiry;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class ChatRoom extends Fragment {



    EditText Message;
    ListView listView;
    Button send;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    DatabaseReference myRef1;
    SharedPreferences database;
    SharedPreferences.Editor editor;
    ArrayList<String> messagestring;
    ArrayAdapter<String> arrayAdapter;
    public ChatRoom() {
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
      View view= inflater.inflate(R.layout.fragment_chat_room, container, false);
    listView=view.findViewById(R.id.list);
    Message=view.findViewById(R.id.message);
    send=view.findViewById(R.id.send);
    messagestring=new ArrayList<>();
    arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,messagestring);
    listView.setAdapter(arrayAdapter);
    firebasedatabase = FirebaseDatabase.getInstance();
    myRef = firebasedatabase.getReference("Teachers");
    myRef1 = firebasedatabase.getReference("Students");
    database = getContext().getSharedPreferences("TEACHER", MODE_PRIVATE);
    editor = database.edit();
    myRef.child(database.getString("userid","TEST")).child("MESSAGE").child(database.getString("chatoid","TEST")).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot!=null){
                messagestring.clear();
                Iterable<DataSnapshot> dataSnapshots=dataSnapshot.getChildren();
                for(DataSnapshot dataSnapshot1:dataSnapshots){
                     String name=dataSnapshot1.getKey();
                     String message=dataSnapshot1.getValue(String.class);
                     messagestring.add(name+"\n"+" :-"+message);
                     arrayAdapter.notifyDataSetChanged();

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Date currentTime = Calendar.getInstance().getTime();
            final String mesaagevalue=database.getString("username","TEST")+" :-"+Message.getText().toString();
            myRef.child(database.getString("userid","TEST")).child("MESSAGE").child(database.getString("chatoid","TEST")).child(currentTime.toString()).setValue(mesaagevalue).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    myRef1.child(database.getString("chatoid", "TEST")).child("MESSAGE").child(database.getString("userid", "TEST")).child(currentTime.toString()).setValue(mesaagevalue).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Message.setText("");
                            Toast.makeText(getContext(),"Message Suceessfully sent",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }
    });

      return view;
    }





}
