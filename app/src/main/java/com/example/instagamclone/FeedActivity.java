package com.example.instagamclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        auth=FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //menu bağlama
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);
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