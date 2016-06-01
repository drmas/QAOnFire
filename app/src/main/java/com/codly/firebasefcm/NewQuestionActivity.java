package com.codly.firebasefcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewQuestionActivity extends AppCompatActivity {

    private DatabaseReference ref;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Questions");
    }

    public void save(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        Question question = new Question(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), user.getDisplayName(), editText.getText().toString(), false);
        ref.push().setValue(question);

        finish();
    }
}
