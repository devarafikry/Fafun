package ttc.project.fafun.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ttc.project.fafun.R;
import ttc.project.fafun.model.Family;
import ttc.project.fafun.model.FamilyMember;
import ttc.project.fafun.model.User;

/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    @BindView(R.id.email_sign_up_button)
    Button btnSignup;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.age)
    EditText age;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.re_password)
    EditText re_password;
    @BindView(R.id.to_login)
    TextView to_login;
    @BindView(R.id.back_icon)
    ImageView back_icon;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private PhoneAuthCredential credential;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth.AuthStateListener mAuthListener;
private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String ext = ".jpg";
    Toast mToast;

    @OnClick(R.id.to_login)
    public void goLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.back_icon)
    public void goBack(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void notifyUser(String s){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        mToast.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Set up the login form.
        ButterKnife.bind(this);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.signup || id == EditorInfo.IME_NULL) {
//                    attempSignup();
//                    return true;
//                }
//                return false;
//            }
//        });

        Button nEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
//  attem

        mLoginFormView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.signup_progress);

    }

    private boolean verifyData(){
        if(name.getText().toString().length() == 0){
            notifyUser(getString(R.string.error_field_required));
            return false;
        }
        if(!isEmailValid(email.getText().toString())){
            notifyUser(getString(R.string.error_invalid_email));
            return false;
        }
        if(password.getText().toString().length() < 6){
            notifyUser(getString(R.string.error_invalid_password));
            return false;
        }
        if(!isPasswordMatch()){
            return false;
        }
        return true;
    }

    private boolean isPasswordMatch(){
            String pass1 = password.getText().toString();
            String pass2 = re_password.getText().toString();

        if(pass1.equals(pass2)){
            return true;
        } else{
            password.getText().clear();
            re_password.getText().clear();

            notifyUser(getString(R.string.error_password_not_match));
            return false;
        }
    }


    @OnClick(R.id.email_sign_up_button)
    public void signUp(View view){

        if(verifyData()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            notifyUser(getString(R.string.success_signup));
                            signInWithEmail();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    notifyUser(getString(R.string.error_signup));
                }
            }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w("Test", "signInWithEmail:failed", task.getException());
                        Toast.makeText(SignupActivity.this, "GAGAL",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else{
//            uploadingDataFragmet.dismiss();
        }

    }

    private void signInWithEmail(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),
                password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(final AuthResult authResult) {

                final String family_id = UUID.randomUUID().toString();
                final User user = new User(
                        email.getText().toString(),
                        family_id,
                        getResources().getInteger(R.integer.family_admin_type)
                );
                Family family = new Family(family_id,name.getText().toString());
                dbRef.child(getString(R.string.family_node)).child(family_id)
                .setValue(family);

                String path = FirebaseStorage.getInstance().getReference().child(getString(R.string.storage_avatar_link))
                        .child("boy1.png")
                        .getPath();
                FamilyMember familyMember = new FamilyMember(
                        authResult.getUser().getUid(),
                        family_id,
                        name.getText().toString(),
                        age.getText().toString(),
                        getString(R.string.role_default),
                        path,
                        getResources().getInteger(R.integer.point_default_value),
                        getResources().getInteger(R.integer.point_default_value),
                        getResources().getInteger(R.integer.completed_task_default_value)
                );
                dbRef.child(getResources().getString(R.string.family_member))
                        .child(family_id).child(authResult.getUser().getUid())
                        .setValue(familyMember);

                dbRef.child(getString(R.string.user_node)).child(authResult.getUser().getUid()).setValue(user);
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

