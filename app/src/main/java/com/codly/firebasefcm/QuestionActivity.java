package com.codly.firebasefcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuestionActivity extends AppCompatActivity {

    private FirebaseListAdapter<Comment> mAdapter;
    private DatabaseReference answersRef;
    private EditText edtAnswer;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        String key = getIntent().getExtras().getString("key");

        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("Questions").child(key);
        answersRef = FirebaseDatabase.getInstance().getReference("Answers").child(key);

        final ListView listView = (ListView) findViewById(R.id.listview);

        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                View v = getLayoutInflater().inflate(R.layout.question, null);
                TextView question = (TextView) v.findViewById(R.id.txtQuestion);

                question.setText(dataSnapshot.getValue(Question.class).getQuestion());
                listView.addHeaderView(v);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAdapter = new FirebaseListAdapter<Comment>(this, Comment.class, android.R.layout.two_line_list_item, answersRef) {
            @Override
            protected void populateView(View view, Comment model, final int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(model.getComment());
                ((TextView)view.findViewById(android.R.id.text2)).setText(model.getDate());

            }
        };
        listView.setAdapter(mAdapter);


        // add answer
        edtAnswer = (EditText) findViewById(R.id.edtAnswer);
        edtAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                answersRef.push().setValue(new Comment(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), user.getDisplayName(), edtAnswer.getText().toString()));
                edtAnswer.setText("");
                return false;
            }
        });
    }
}
