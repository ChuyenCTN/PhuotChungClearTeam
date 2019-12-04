package com.clearteam.phuotnhom.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.utils.Const;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.storage.FirebaseStorage.getInstance;


public class EditInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView imgAvata;
    private EditText edName, edEmail, edAddress, edNumberPhone, ed_number_phone_relatives, edSex;
    private Button btnSave;
    private ImageButton imgBlack;
    String name1, img1, email, address1, number_phone, tv_number_phone_relatives, sex;

    private String mStroragePath = "All_image_Uploads/";
    private int IMAGE_REQUSET_CODE = 5;
    Uri mFilePathUri;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private ProgressDialog mProgressDialog;
    private String mStoragePath = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        changeStatustBar();
        initView();
    }

    private void initView() {

        imgAvata = findViewById(R.id.img_avata);
        edName = findViewById(R.id.name);
        edEmail = findViewById(R.id.email);
        edNumberPhone = findViewById(R.id.number_phone);
        ed_number_phone_relatives = findViewById(R.id.ed_number_phone_relatives);
        edSex = findViewById(R.id.ed_sex);
        edAddress = findViewById(R.id.address);
        btnSave = findViewById(R.id.btnLuu);
        imgBlack = findViewById(R.id.img_back);

        imgBlack.setOnClickListener(this);
        imgAvata.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        mStorageReference = getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mStoragePath);
        mProgressDialog = new ProgressDialog(EditInformationActivity.this);
        getData();
    }
    private void changeStatustBar() {
        Window window = this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(EditInformationActivity.this, R.color.bg_tab));
        }
    }

    private void getData() {
        //  Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            User user = ((User) bundle.getSerializable(Const.KEY_USER));
            name1 = user.getUsername();
            img1 = user.getImageURL();
            email = user.getEmail();
            number_phone = user.getNumberPhone();
            tv_number_phone_relatives = user.getNumberPhoneRelatives();
            sex = user.getSex();
            address1 = user.getAddress();

            edName.setText(name1);
            edEmail.setText(email);
            edNumberPhone.setText(number_phone);
            edAddress.setText(address1);
            ed_number_phone_relatives.setText(tv_number_phone_relatives);
            edSex.setText(sex);
            edAddress.setText(address1);

            if (img1.equals("default")) {
                Glide.with(this).load(R.drawable.avatar).into(imgAvata);
            } else {
                Glide.with(this).load(img1).into(imgAvata);
            }
            btnSave.setText("Upload");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_avata:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select imgae"), IMAGE_REQUSET_CODE);
                break;
            case R.id.btnLuu:
                beginUpdate();
                break;
        }
    }

    private void beginUpdate() {
        mProgressDialog.setMessage("updating...");
        mProgressDialog.show();
        deletePreviousImage();
    }

    private void uploadDataToFirebase() {
        if (mFilePathUri != null) {
            // setting progress bar title
            mProgressDialog.setTitle("Image is Uploading...");
            // show progress dialog
            mProgressDialog.show();
            // create second storageReference
            StorageReference storageReference2nd = mStorageReference.child(mStoragePath + System.currentTimeMillis() + "." + getFileExtension(mFilePathUri));
            // adding addOnSuccessListerner to storageReference2nd
            storageReference2nd.putFile(mFilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String mName = edName.getText().toString().trim();
                    String mEmail = edEmail.getText().toString().trim();
                    String mAddress = edAddress.getText().toString().trim();
                    String mNumberPhone = edNumberPhone.getText().toString().trim();
                    //hid progress dialog
                    mProgressDialog.dismiss();
                    Toast.makeText(EditInformationActivity.this, "uploaded successfully...", Toast.LENGTH_SHORT).show();

                    User user = new User(mName, mEmail, mAddress, mNumberPhone, taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(), mName.toLowerCase());
                    String imageUpload = mDatabaseReference.push().getKey();
                    mDatabaseReference.child(imageUpload).setValue(user);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(EditInformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.setTitle("Image is uploading");
                }
            });
        } else {
            Toast.makeText(this, "Please select image or add image name", Toast.LENGTH_SHORT).show();
        }
    }

    // method to get tge selected image file extension from file path uri
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void deletePreviousImage() {
        if (!img1.equals("default")) {
            StorageReference reference1 = getInstance().getReferenceFromUrl(img1);
            reference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    updateNewImage();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditInformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        } else {
            updateNewImage();
        }

    }

    private void updateNewImage() {
        String imageName = System.currentTimeMillis() + ".png";
        StorageReference storageReference = mStorageReference.child(mStroragePath + imageName);
        Bitmap bitmap = ((BitmapDrawable) imgAvata.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        final UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditInformationActivity.this, "New image uploaded...", Toast.LENGTH_SHORT).show();
                final Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri dowloadUri = uriTask.getResult();

                        updateData(dowloadUri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditInformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void updateData(final String toString) {
        final String name = edName.getText().toString();
        final String email = edEmail.getText().toString();
        final String address = edAddress.getText().toString();
        final String numberPhone = edNumberPhone.getText().toString();
        final String numberPhoneRelatives = ed_number_phone_relatives.getText().toString();
        final String sexx = edSex.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(mStoragePath);
        Query query = reference.orderByChild("username").equalTo(name1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    dataSnapshot1.getRef().child("username").setValue(name);
                    dataSnapshot1.getRef().child("email").setValue(email);
                    dataSnapshot1.getRef().child("imageURL").setValue(toString);
                    dataSnapshot1.getRef().child("address").setValue(address);
                    dataSnapshot1.getRef().child("numberPhone").setValue(numberPhone);
                    dataSnapshot1.getRef().child("numberPhoneRelatives").setValue(numberPhoneRelatives);
                    dataSnapshot1.getRef().child("sex").setValue(sexx);
                }
                mProgressDialog.dismiss();
                Toast.makeText(EditInformationActivity.this, "sửa thành công", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditInformationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mFilePathUri = data.getData();

            Bitmap bitmap = null;
            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(mFilePathUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mFilePathUri);
                imgAvata.setImageBitmap(selectedImage);
                imgAvata.setImageURI(mFilePathUri);
                imgAvata.invalidate();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgAvata.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

        }
    }
}
