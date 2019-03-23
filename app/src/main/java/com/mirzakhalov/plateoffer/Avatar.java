package com.mirzakhalov.plateoffer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Avatar extends AppCompatActivity implements View.OnClickListener {

    ImageButton photo;
    ImageButton gallery;
    ImageView avatar;
    TextView continueNext;

    final int CAMERA_CAPTURE = 1;
    private static final int REQUEST_CODE_CHOOSE_IMAGE = 2;
    private static final int WRITE_PERMISSION = 786;

    private Uri picUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        photo = findViewById(R.id.photo);
        gallery = findViewById(R.id.gallery);
        avatar = findViewById(R.id.avatar);
        continueNext = findViewById(R.id.continueNext);

        photo.setOnClickListener(this);
        gallery.setOnClickListener(this);
        continueNext.setOnClickListener(this);

        // request permission in the runtime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.photo:
                takePhoto();
                break;
            case R.id.gallery:
                openGallery();
                break;
            case R.id.continueNext:
                break;
        }
    }

    private void openGallery() {
        try {
            // use the intent to open gallery
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "Gallery"), REQUEST_CODE_CHOOSE_IMAGE);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            Toast toast = Toast.makeText(Avatar.this, "Gallery cannot be opened", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void takePhoto() {

        try {
            //use standard intent to capture an image
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //we will handle the returned data in onActivityResult
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            Toast toast = Toast.makeText(Avatar.this, "Camera is not supported", Toast.LENGTH_SHORT);

            toast.show();
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case WRITE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                }
                else{

                    //  String errorMessage = "Are you sure you don't want to choose a picture for your avatar?";
                    Toast toast = Toast.makeText(Avatar.this, "Camera Permission needed", Toast.LENGTH_SHORT);
                    toast.show();
                    // Permission denied - Show a message to inform the user that this app only works
                    // with these permissions granted

                }
                return;
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            //user is returning from capturing an image using the camera
            if (requestCode == CAMERA_CAPTURE) {
                //get the Uri for the captured image
                if(data != null) {
                    picUri = data.getData();
                    Bundle extras = data.getExtras();
                    //get the cropped bitmap
                    Bitmap thePic = extras.getParcelable("data");
                    if(thePic != null){
                        // resizing the picture to 256 by 256 to save space
                        thePic = Bitmap.createScaledBitmap(thePic, 256, 256, true);
                        uploadAvatar(thePic);
                        avatar.setImageBitmap(thePic);
                    }
                    else{
                        //TODO handle
                    }

                }

            }

            if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap thePic = BitmapFactory.decodeFile(picturePath);
                if(thePic != null){

                    thePic = Bitmap.createScaledBitmap(thePic, 256, 256, true);
                    uploadAvatar(thePic);
                    avatar.setImageBitmap(thePic);
                }
                else{
                    //TODO handle
                }

            }
        }
    }


    public void uploadAvatar(Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String uid = getIntent().getStringExtra("uid");
        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("Avatars/" + uid + ".jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast toast = Toast.makeText(Avatar.this, "Something went wrong", Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast toast = Toast.makeText(Avatar.this, "Upload was successfull", Toast.LENGTH_SHORT);
                toast.show();

                Intent next = new Intent(Avatar.this, MainActivity.class);
                finish();
                startActivity(next);
            }
        });
    }




}
