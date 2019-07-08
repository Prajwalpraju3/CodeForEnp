package com.mycode.appforenp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.mycode.appforenp.database.DatabaseHelper;
import com.mycode.appforenp.databinding.ActivityCreateRecordBinding;
import com.mycode.appforenp.models.MyModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreateRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CAMERA = 111;
    ActivityCreateRecordBinding binding;
    private static final int CAMERA_CLICK = 7;
    private static final int GALLERY_CLICK = 2;
    Bitmap finalbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_record);

        binding.ivProfile.setOnClickListener(this);
        binding.etDic.setOnClickListener(this);
        binding.etHeader.setOnClickListener(this);
        binding.btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_profile:
                createDialogue();
                break;
            case R.id.bt_submit:
                validateAndInsertInDatabase();
                break;

        }
    }

    private void validateAndInsertInDatabase() {
        if (TextUtils.isEmpty(binding.etHeader.getText().toString()) && TextUtils.isEmpty(binding.etDic.getText().toString()) && finalbitmap == null) {
            Toast.makeText(this, "All fields are mandatory...!", Toast.LENGTH_SHORT).show();
        } else {
            addDatatoDataBase(binding.etHeader.getText().toString(), binding.etDic.getText().toString(), finalbitmap);
        }
    }

    private void addDatatoDataBase(String header, String dec, Bitmap bitmap) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        MyModel myModel = new MyModel(header, dec, byteArray);

        if (databaseHelper.addData(myModel)) {
            Intent data = new Intent();

            data.setData(Uri.parse(getString(R.string.data_updated)));
            setResult(RESULT_OK, data);
            finish();
        } else {
            Toast.makeText(this, "Failed to add the record...", Toast.LENGTH_SHORT).show();
        }

    }

    private void createDialogue() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CreateRecordActivity.this);
        builder.setTitle(getString(R.string.select))
                .setItems(R.array.list_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, GALLERY_CLICK);
                                break;
                            case 1:
                                if (ActivityCompat.checkSelfPermission(CreateRecordActivity.this, Manifest.permission.CAMERA)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    // Permission is already available, start camera preview

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

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Request the permission
            ActivityCompat.requestPermissions(CreateRecordActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_CLICK);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case GALLERY_CLICK:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                        Matrix matrix = new Matrix();
//                        matrix.postRotate(90);
//                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        binding.ivProfile.setImageBitmap(bitmap);
                        DrawImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case CAMERA_CLICK:
                if (resultCode == RESULT_OK) {
                    DrawImage((Bitmap) imageReturnedIntent.getExtras().get("data"));
                }
                break;
        }
    }

    private void DrawImage(Bitmap rotatedBitmap) {
        finalbitmap = rotatedBitmap;
        binding.ivProfile.setImageBitmap(finalbitmap);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.

                startCamera();
            } else {
                // Permission request was denied.

            }
        }
    }


}
