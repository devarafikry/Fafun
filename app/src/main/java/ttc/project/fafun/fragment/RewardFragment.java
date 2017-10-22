package ttc.project.fafun.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import ttc.project.fafun.activity.AddRewardActivity;
import ttc.project.fafun.activity.NotificationActivity;
import ttc.project.fafun.helper.SnackbarUtils;
import ttc.project.fafun.holder.RequestedRewardHolder;
import ttc.project.fafun.holder.RewardHolder;
import ttc.project.fafun.model.Family;
import ttc.project.fafun.model.FamilyMember;
import ttc.project.fafun.model.RequestedReward;
import ttc.project.fafun.model.Reward;
import ttc.project.fafun.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardFragment extends Fragment {

    @BindView(R.id.add_reward)
    FloatingActionButton btn_add_reward;
    @BindView(R.id.reward_recyclerview)
    RecyclerView reward_recyclerview;
    @BindView(R.id.rootView) View rootView;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private Snackbar s;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbRef.child(getString(R.string.user_node))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user.getUser_type() ==
                                getResources().getInteger(R.integer.family_admin_type)){
                            setHasOptionsMenu(true);
                        } else{
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public RewardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_reward_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_notification){
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward, container, false);
        ButterKnife.bind(this, view);

        dbRef.child(getString(R.string.user_node))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User user = dataSnapshot.getValue(User.class);
                        Query ref = dbRef.child(getString(R.string.reward_node))
                                .child(user.getFamily_id());
                        if(user.getUser_type() ==
                                getResources().getInteger(R.integer.family_admin_type)){
                            btn_add_reward.setVisibility(View.VISIBLE);
                        } else{
                            btn_add_reward.setVisibility(View.GONE);
                        }
                        FirebaseRecyclerAdapter fireAdapter = new FirebaseRecyclerAdapter<Reward, RewardHolder>(
                                Reward.class,
                                R.layout.reward_item,
                                RewardHolder.class,
                                ref
                        ) {
                            @Override
                            protected void populateViewHolder(final RewardHolder viewHolder, final Reward model, final int position) {
                                viewHolder.reward_name.setText(model.getReward_name());
                                viewHolder.reward_cost_point.setText(model.getReward_cost_point()+" Point");
                                if(user.getUser_type() == getResources().getInteger(R.integer.family_admin_type)){
                                    viewHolder.btn_delete_reward.setVisibility(View.VISIBLE);
                                } else{
                                    viewHolder.btn_delete_reward.setVisibility(View.GONE);
                                }
                                viewHolder.btn_delete_reward.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Hapus hadiah "+model.getReward_name()+" ?")
                                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // FIRE ZE MISSILES!
                                                        getRef(position).removeValue();

                                                        SnackbarUtils.showSnackbar(
                                                                rootView,
                                                                s,
                                                                "Hadiah "+model.getReward_name()+" telah dihapus",
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

                                dbRef.child(getString(R.string.family_member))
                                        .child(user.getFamily_id())
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                                final FamilyMember member = dataSnapshot.getValue(FamilyMember.class);
                                                if(member.getUser_point() >= model.getReward_cost_point()){
                                                    viewHolder.btn_claim_reward.setVisibility(View.VISIBLE);
                                                    viewHolder.btn_claim_reward_not_enough.setVisibility(View.GONE);
                                                } else{
                                                    viewHolder.btn_claim_reward.setVisibility(View.GONE);
                                                    viewHolder.btn_claim_reward_not_enough.setVisibility(View.VISIBLE);
                                                }

                                                viewHolder.btn_claim_reward_not_enough.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        SnackbarUtils.showSnackbar(
                                                                rootView,
                                                                s,
                                                                "Pointmu tidak mencukupi untuk mengambil hadiah ini",
                                                                Snackbar.LENGTH_LONG
                                                        );
                                                    }
                                                });

                                                viewHolder.btn_claim_reward.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                        builder.setMessage("Tukar point untuk reward "+model.getReward_name()+" ? (Sisa point :"+ member.getUser_point()+")")
                                                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        // FIRE ZE MISSILES!
                                                                        int currentPoint = member.getUser_point();
                                                                        currentPoint-=model.getReward_cost_point();
                                                                        member.setUser_point(currentPoint);
                                                                        dataSnapshot.getRef().setValue(member);

                                                                        RequestedReward requestedReward = new RequestedReward(
                                                                                model.getReward_name(),
                                                                                model.getReward_photo_link(),
                                                                                model.getReward_cost_point(),
                                                                                System.currentTimeMillis()
                                                                        );

                                                                        dbRef.child(getString(R.string.requested_reward_node))
                                                                                .child(member.getFamily_id())
                                                                                .push()
                                                                                .setValue(requestedReward);

                                                                        SnackbarUtils.showSnackbar(
                                                                                rootView,
                                                                                s,
                                                                                "Request hadiah telah dikirimkan kepada admin",
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
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                            }
                        };
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        reward_recyclerview.setLayoutManager(linearLayoutManager);
                        reward_recyclerview.setAdapter(fireAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        btn_add_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddRewardActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
