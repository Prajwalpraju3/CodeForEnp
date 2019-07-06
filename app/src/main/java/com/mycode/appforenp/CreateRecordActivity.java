package com.mycode.appforenp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.mycode.appforenp.databinding.ActivityCreateRecordBinding;

import java.io.IOException;

public class CreateRecordActivity extends AppCompatActivity {

    ActivityCreateRecordBinding binding;
    private  static final int PERMISSION_REQUEST_ = 1;
    private  static final int CAMERA = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_record );

        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(CreateRecordActivity.this);
                    builder.setTitle(getString(R.string.select))
                            .setItems(R.array.list_array, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:
                                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(pickPhoto , 2);
                                            break;
                                        case 1:
                                            if (ActivityCompat.checkSelfPermission(CreateRecordActivity.this, Manifest.permission.CAMERA)
                                                    == PackageManager.PERMISSION_GRANTED) {
                                                startCamera();
                                            } else {
                                                // Permission is missing and must be requested.
                                                requestCameraPermission();
                                            }
                                            break;
                                            default:

                                                break;
                                    }
                                }
                            });
                    builder.create();
                    builder.show();
            }
        });
    }

    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(CreateRecordActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_);
        }

    }

    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch(requestCode) {
            case 2:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        binding.ivProfile.setImageBitmap(rotatedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                break;
            case CAMERA:
                if(resultCode == RESULT_OK){

                    try {
                        Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                        binding.ivProfile.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            }
        }
    }
}
