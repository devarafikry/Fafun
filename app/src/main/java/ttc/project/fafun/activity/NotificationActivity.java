package ttc.project.fafun.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.fafun.R;
import ttc.project.fafun.helper.SnackbarUtils;
import ttc.project.fafun.holder.RequestedRewardHolder;
import ttc.project.fafun.holder.RewardHolder;
import ttc.project.fafun.model.FamilyMember;
import ttc.project.fafun.model.RequestedReward;
import ttc.project.fafun.model.Reward;
import ttc.project.fafun.model.User;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.reward_recyclerview)
    RecyclerView reward_recyclerview;
    @BindView(R.id.rootView)
    View rootView;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private Snackbar s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Permintaan Hadiah");
        dbRef.child(getString(R.string.user_node))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User user = dataSnapshot.getValue(User.class);
                        Query ref = dbRef.child(getString(R.string.requested_reward_node))
                                .child(user.getFamily_id());
                        final FirebaseRecyclerAdapter fireAdapter = new FirebaseRecyclerAdapter<RequestedReward, RequestedRewardHolder>(
                                RequestedReward.class,
                                R.layout.requested_reward_item,
                                RequestedRewardHolder.class,
                                ref
                        ) {
                            @Override
                            protected void populateViewHolder(final RequestedRewardHolder viewHolder, final RequestedReward model, final int position) {
                                viewHolder.reward_name.setText(model.getReward_name());
                                viewHolder.reward_cost_point.setText(model.getReward_cost_point() + " Point");
                                String reqeusted_text =
                                        DateUtils.getRelativeTimeSpanString(model.getRequested_time(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS).toString();
                                viewHolder.reward_date.setText("Diminta pada " + reqeusted_text);
                                viewHolder.btn_complete_reward.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
                                        builder.setMessage("Selesaikan hadiah " + model.getReward_name() + " ? (Item akan dihapus dari daftar setelah diselesaikan)")
                                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // FIRE ZE MISSILES!
                                                        getRef(viewHolder.getAdapterPosition()).removeValue();

                                                        SnackbarUtils.showSnackbar(
                                                                rootView,
                                                                s,
                                                                "Hadiah " + model.getReward_name() + " telah diselesaikan",
                                                                Snackbar.LENGTH_LONG
                                                        );
                                                    }
                                                })
                                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User cancelled the dialog
                                                        dialog.dismiss();
                                                    }
                                                });
                                        // Create the AlertDialog object and return it
                                        builder.create().show();
                                    }
                                });
                                StorageReference shopImageRef = storageRef.child(model.getReward_photo_link());
                                shopImageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(final byte[] bytes) {
                                        // Use the bytes to display the image
                                        Log.d("databaru", "Download Success");
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        viewHolder.reward_image.setImageBitmap(bitmap);
//                        loadImageThumbnail(false, viewHolder.product_image, viewHolder.l);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
//                        errorToast.show();
                                    }
                                });
                            }
                        };

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this);
                        reward_recyclerview.setLayoutManager(linearLayoutManager);
                        reward_recyclerview.setAdapter(fireAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}

