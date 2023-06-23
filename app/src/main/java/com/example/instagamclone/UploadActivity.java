package com.example.instagamclone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Toast;

import com.example.instagamclone.databinding.ActivityUploadBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class UploadActivity extends AppCompatActivity {
    Uri imageData; //her yerde kullanabilmek için
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    private ActivityUploadBinding binding;
    Bitmap selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        registerLauncher();

    }

    public void uploadClicked(View view)
    {

    }

    public void selectImage(View view)
    {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES))
            {
                Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ask permission
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);

                    }
                }).show();
            }else {
                //ask permission
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);

            }
        }else {
            //izin görünmiş, galarye intent
            Intent intentToGallery =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }


    }

    private void registerLauncher() {
        activityResultLauncher = registerForActivityResult(//yeni bir aktivite başlatacağız sonuç için sonuç: o veriyi almak
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {//sonuç ne olacak
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intentFromResult = result.getData(); //veriyi alıyoruz
                            if (intentFromResult != null) {//veri boş mu kontrol

                                imageData = intentFromResult.getData(); //uri nerede tam adres döndürülüyor
                                //son *** binding.imageView.setImageURI(imageData);


                                try {

                                    if (Build.VERSION.SDK_INT >= 28) {
                                        ImageDecoder.Source source = ImageDecoder.createSource(UploadActivity.this.getContentResolver(),imageData);
                                        selectedImage=ImageDecoder.decodeBitmap(source);

                                        binding.imageView.setImageBitmap(selectedImage);

                                    } else {
                                        selectedImage = MediaStore.Images.Media.getBitmap(UploadActivity.this.getContentResolver(),imageData);
                                        binding.imageView.setImageBitmap(selectedImage);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }


                        }
                    }
                });




        permissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result) {
                            //permission granted
                            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activityResultLauncher.launch(intentToGallery);

                        } else {
                            //permission denied
                            Toast.makeText(UploadActivity.this,"Permisson needed!",Toast.LENGTH_LONG).show();
                        }
                    }

                });

    }

}