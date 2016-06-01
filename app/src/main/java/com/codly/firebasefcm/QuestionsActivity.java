package com.codly.firebasefcm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionsActivity extends AppCompatActivity {

    private FirebaseListAdapter<Question> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuestionsActivity.this, NewQuestionActivity.class));
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Questions");

        ListView questionsList = (ListView) findViewById(R.id.listview);

        mAdapter = new FirebaseListAdapter<Question>(this, Question.class, android.R.layout.two_line_list_item, ref) {
            @Override
            protected void populateView(View view, Question model, final int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(model.getQuestion());
                ((TextView)view.findViewById(android.R.id.text2)).setText(model.getDate());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QuestionsActivity.this, QuestionActivity.class);
                        intent.putExtra("key", getRef(position).getKey());

                        startActivity(intent);
                    }
                });
            }
        };
        questionsList.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_questions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sign_out:
                signout();
        }
        return true;
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        openLoginActivity();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
