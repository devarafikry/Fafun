package ttc.project.fafun.activity;

import android.Manifest;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import ttc.project.fafun.fragment.PhotoPickOptionFragment;
import ttc.project.fafun.R;
import ttc.project.fafun.helper.SnackbarUtils;
import ttc.project.fafun.model.Reward;
import ttc.project.fafun.model.User;

public class AddRewardActivity extends AppCompatActivity {

    @BindView(R.id.upload_prompt)
    FrameLayout upload_prompt;
    @BindView(R.id.reward_image)
    ImageView reward_image;
    @BindView(R.id.reward_name)
    TextView reward_name;
    @BindView(R.id.reward_point) TextView reward_point;
    @BindView(R.id.rootView) View rootView;

    Snackbar s;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    String imageExt = ".jpg";
    String uid = UUID.randomUUID().toString();
    String imageName = uid+imageExt;

    final static int TAKE_PHOTO_CONSTANT =1;
    final static int PICK_PHOTO_CONSTANT =2;
    String mCurrentPhotoPath;
    Uri photoURI;

    final static int PERMISSION_READ_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward);
        ButterKnife.bind(this);

        upload_prompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reward, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save){
            if(checkUserInput()){
                Timber.d("You are ready to go.");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Masukkan "+reward_name.getText().toString()+
                        " sebagai hadiah?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbRef.child(getString(R.string.user_node))
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                User user = dataSnapshot.getValue(User.class);
                                                String familyId = user.getFamily_id();
                                                Reward reward = new Reward(
                                                        reward_name.getText().toString(),
                                                        storageRef.child(getString(R.string.reward_storage_node)).child(imageName).getPath(),
                                                        Integer.valueOf(reward_point.getText().toString())
                                                );
                                                dbRef.child(getString(R.string.reward_node))
                                                        .child(familyId)
                                                        .push()
                                                        .setValue(reward)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        finish();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();

            }
            else{
                SnackbarUtils.showSnackbar(rootView, s, "Mohon isi semua data terlebih dahulu", Snackbar.LENGTH_LONG);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkUserInput(){
        if(TextUtils.isEmpty(reward_name.getText().toString())
                || TextUtils.isEmpty(reward_point.getText().toString())
                || imageName.equals("")){
            return false;
        }
        return true;
    }

    private void showFileChooser() {
        DialogFragment photoPickOptionFragment = new PhotoPickOptionFragment();

        FragmentManager fm = getFragmentManager();
        photoPickOptionFragment.show(fm,"Show");
    }

    public void launchImageCreator(int constant){
        int selected_action = constant;
        if(selected_action<999){
            if(selected_action == TAKE_PHOTO_CONSTANT){
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePicture.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        photoURI = FileProvider.getUriForFile(this,
                                "com.example.android.fileprovider.fafun",
                                photoFile);
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePicture, TAKE_PHOTO_CONSTANT);
                    }
                }
            } else if(selected_action == PICK_PHOTO_CONSTANT){
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(pickPhoto.resolveActivity(getPackageManager()) != null){
                    int permissionCheck = ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                    if(permissionCheck != PackageManager.PERMISSION_GRANTED){
                        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_READ_EXTERNAL_STORAGE);
                    }
                    startActivityForResult(pickPhoto , PICK_PHOTO_CONSTANT);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case TAKE_PHOTO_CONSTANT:
                if(resultCode == RESULT_OK){
                    uploadImage(photoURI);
                }

                break;
            case PICK_PHOTO_CONSTANT:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    uploadImage(selectedImage);
                }
                break;
        }
    }

    private void requestPermission(String permission, int permission_constant){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                permission)) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    permission_constant);
        }
    }
    private void uploadImage(final Uri selectedImage){
        Bitmap bitmap;
        StorageReference rewardStorage = storageRef.child(getString(R.string.reward_storage_node))
                .child(imageName);

        try {
            final long SIZE = 529 * 278/200;
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
            int countSebelum = bitmap.getByteCount();
            final Bitmap fixedBitmap = scaleDown(bitmap,SIZE,true);
            int countSaatIni = fixedBitmap.getByteCount();

            Log.d("Profile","Image Size : sebelum("+countSebelum+") saat ini("+countSaatIni+")");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            fixedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            UploadTask uploadTask = rewardStorage.putBytes(byteArray);


            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("Profile","Upload Fail!");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.d("Profile","Upload Success!");
                    upload_prompt.setVisibility(View.INVISIBLE);
                    reward_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showFileChooser();
                        }
                    });
                    reward_image.setImageBitmap(fixedBitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
