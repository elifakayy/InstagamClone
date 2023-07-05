package com.example.instagamclone.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.instagamclone.R;
import com.example.instagamclone.databinding.ActivityFeedBinding;
import com.example.instagamclone.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Post>postArrayList;
    private ActivityFeedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        binding=ActivityFeedBinding.inflate(getLayoutInflater());
        auth=FirebaseAuth.getInstance();

        View view=binding.getRoot();
        setContentView(view);

        postArrayList=new ArrayList<>();
        firebaseFirestore=FirebaseFirestore.getInstance();
        getData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //menu bağlama
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void getData()
    {
        //DocumentReference documentReference= firebaseFirestore.collection("Posts").document("sdas")
//CollectionReference documentReference= firebaseFirestore.collection("Posts")

        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null)
                {
                    Toast.makeText(FeedActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                }
                if(value != null)
                {
                    //içinde dökomanlar olan liste veriyor o yüzden for loop alıyoruz
                    for(DocumentSnapshot snapshot: value.getDocuments())
                    {
                        Map<String, Object> data=snapshot.getData();

                        //casting
                        String userEmail = (String) data.get("useremail");
                        String comment = (String) data.get("comment");
                        String downloadUrl = (String) data.get("downloadUrl");

                        Post post = new Post(userEmail,comment,downloadUrl);

                        postArrayList.add(post);

                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.add_post)
        {
            Intent intentUpload= new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intentUpload);
        }else if (item.getItemId()==R.id.signout){
            auth.signOut();

            Intent intentMain= new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}