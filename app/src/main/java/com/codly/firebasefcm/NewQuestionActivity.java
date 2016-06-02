package com.codly.firebasefcm;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import rx.functions.Action1;

public class NewQuestionActivity extends AppCompatActivity {

    private DatabaseReference ref;
    FirebaseUser user;
    private StorageReference imagesRef;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Questions");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://fir-authapp.appspot.com");
        imagesRef = storageRef.child("images/" + UUID.randomUUID() + ".png");

    }

    public void save(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        Question question = new Question(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), user.getDisplayName(), editText.getText().toString(), false, imageUrl);
        ref.push().setValue(question);

        finish();
    }

    public void uploadImage() {
        RxImagePicker.with(this).requestImage(Sources.CAMERA).subscribe(new Action1<Uri>() {
            public UploadTask uploadTask;

            @Override
            public void call(Uri uri) {
                uploadTask = imagesRef.putFile(uri);
                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setVisibility(View.VISIBLE);

                Glide.with(NewQuestionActivity.this)
                        .load(uri)
                        .into(imageView);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        imageUrl = taskSnapshot.getDownloadUrl().toString();
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_new_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_upload_photo)
            uploadImage();
        return true;
    }
}
