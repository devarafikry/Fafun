package ttc.project.fafun.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.fafun.R;
import ttc.project.fafun.helper.SnackbarUtils;
import ttc.project.fafun.model.FamilyMember;
import ttc.project.fafun.model.User;

public class AddFamilyMemberActivity extends AppCompatActivity {
    FirebaseAuth mAuth1;
    FirebaseAuth mAuth2;
    int[] defaultAvatars = {
            R.drawable.boy1,
            R.drawable.boy2,
            R.drawable.girl1,
            R.drawable.girl2
    };
    @BindView(R.id.carouselSelectAvatar)
    CarouselView carouselSelectAvatar;
    @BindView(R.id.btn_next)
    ImageView btn_next;
    @BindView(R.id.btn_prev)
    ImageView btn_prev;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_password_retype)
    EditText edt_password_retype;
    @BindView(R.id.edt_age)
    EditText edt_age;
    @BindView(R.id.edt_role)
    EditText edt_role;
    @BindView(R.id.rootView)
    View rootView;

    private int currentPosition = 0;
    private Snackbar s;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    ViewListener featuredViewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            TextView featured_description;
            ImageView featured_image;
            TextView featured_title;

            View view = getLayoutInflater().inflate(R.layout.select_avatar_item, null);
            ImageView imageAvatar = view.findViewById(R.id.avatar);
            imageAvatar.setImageResource(defaultAvatars[position]);
            return view;
        }

        ;
    };

    private void promptCreateNewFamilyMember() {
        if (checkUserInput()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Masukkan " + edt_name.getText().toString() + " sebagai anggota keluarga?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                createNewFamilyMember();
                            } catch (Exception e){
                                Toast.makeText(AddFamilyMemberActivity.this, getString(R.string.kesalahan_gagal, "menambahkan anggota keluarga"), Toast.LENGTH_LONG).show();
                                FirebaseCrash.report(e);
                            }
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(AddFamilyMemberActivity.this, "Anda Telah Membatalkan Penambahan Anggota Keluarga", Toast.LENGTH_SHORT).show();
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();
        }
    }

    private void createNewFamilyMember() {
        dbRef.child(getString(R.string.user_node))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        final String family_id = user.getFamily_id();
                        try {
                            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                                    .setDatabaseUrl(getString(R.string.firebase_db_url))
                                    .setApiKey(getString(R.string.firebase_api_key))
                                    .setApplicationId(getString(R.string.firebase_app_id)).build();
                            FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions,
                                    "AnyAppName");

                            mAuth2 = FirebaseAuth.getInstance(myApp);
                        } catch (Exception e) {
                            FirebaseCrash.report(e);
                            SnackbarUtils.showSnackbar(rootView, s, getString(R.string.kesalahan_gagal, "menghubungkan dengan server "),
                                    Snackbar.LENGTH_LONG);
                            return;
                        }
                        mAuth2.createUserWithEmailAndPassword(
                                edt_email.getText().toString(), edt_password.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        setResult(RESULT_OK);
                                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(getString(R.string.storage_avatar_link));
                                        String avatarPath = null;
                                        int avatarSelected = carouselSelectAvatar.getCurrentItem();
                                        switch (avatarSelected) {
                                            case 0:
                                                avatarPath = storageReference.child("boy1.png").getPath();
                                                break;
                                            case 1:
                                                avatarPath = storageReference.child("boy2.png").getPath();
                                                break;
                                            case 2:
                                                avatarPath = storageReference.child("girl1.png").getPath();
                                                break;
                                            case 3:
                                                avatarPath = storageReference.child("girl2.png").getPath();
                                                break;
                                        }

                                        FamilyMember familyMember = new FamilyMember(
                                                authResult.getUser().getUid(),
                                                family_id,
                                                edt_name.getText().toString(),
                                                edt_age.getText().toString(),
                                                edt_role.getText().toString(),
                                                avatarPath,
                                                getResources().getInteger(R.integer.point_default_value),
                                                getResources().getInteger(R.integer.point_default_value),
                                                getResources().getInteger(R.integer.completed_task_default_value)
                                        );
                                        dbRef.child(getResources().getString(R.string.family_member))
                                                .child(family_id).child(authResult.getUser().getUid())
                                                .setValue(familyMember);
                                        dbRef.child(getString(R.string.family_member))
                                                .child(family_id)
                                                .child(authResult.getUser().getUid())
                                                .setValue(familyMember);

                                        User user = new User(
                                                edt_email.getText().toString(),
                                                family_id,
                                                getResources().getInteger(R.integer.family_regular_type)
                                        );
                                        dbRef.child(getString(R.string.user_node)).child(authResult.getUser().getUid()).setValue(user);
                                        SnackbarUtils.showSnackbar(rootView, s, "Berhasil menambahkan anggota keluarga",
                                                Snackbar.LENGTH_LONG);
//                                        path+=;
//                                        User user = new User(
//                                                edt_name.getText().toString(),
//                                                edt_email.getText().toString(),
//                                                family_id,
//
//                                                getResources().getInteger(R.integer.family_admin_type),
//                                                getResources().getInteger(R.integer.point_default_value),
//                                                getResources().getInteger(R.integer.completed_task_default_value)
//                                        );

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                SnackbarUtils.showSnackbar(rootView, s, "Gagal menambah anggota keluarga, cek kembali koneksi anda.",
                                        Snackbar.LENGTH_LONG);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private boolean checkUserInput() {
        if (TextUtils.isEmpty(edt_name.getText().toString())
                || TextUtils.isEmpty(edt_email.getText().toString())
                || TextUtils.isEmpty(edt_password.getText().toString())
                || TextUtils.isEmpty(edt_password_retype.getText().toString())
                || TextUtils.isEmpty(edt_age.getText().toString())
                || TextUtils.isEmpty(edt_role.getText().toString())) {
            SnackbarUtils.showSnackbar(rootView, s, "Mohon isi field yang kosong terlebih dahulu.", Snackbar.LENGTH_LONG);
            return false;
        } else if (!edt_password.getText().toString()
                .equals(edt_password_retype.getText().toString())) {
            SnackbarUtils.showSnackbar(rootView, s, "Password tidak sama. Mohon ketik ulang password", Snackbar.LENGTH_LONG);
            edt_password.getText().clear();
            edt_password_retype.getText().clear();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_family, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            promptCreateNewFamilyMember();
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family_member);
        ButterKnife.bind(this);

        mAuth1 = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tambah Anggota Keluarga");
        carouselSelectAvatar.setPageCount(defaultAvatars.length);
        carouselSelectAvatar.setViewListener(featuredViewListener);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition++;
                if (currentPosition == carouselSelectAvatar.getPageCount()) {
                    currentPosition = 0;
                }
                carouselSelectAvatar.setCurrentItem(currentPosition);
            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition--;
                if (currentPosition == -1) {
                    currentPosition = carouselSelectAvatar.getPageCount();
                }
                carouselSelectAvatar.setCurrentItem(currentPosition);
            }
        });
    }
}
