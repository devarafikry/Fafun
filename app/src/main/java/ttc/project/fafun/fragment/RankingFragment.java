package ttc.project.fafun.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import ttc.project.fafun.holder.RankingHolder;
import ttc.project.fafun.model.Family;
import ttc.project.fafun.model.FamilyMember;
import ttc.project.fafun.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class RankingFragment extends Fragment {

    @BindView(R.id.ranking_recyclerview)
    RecyclerView ranking_recyclerview;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public RankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        ButterKnife.bind(this, view);

        dbRef.child(getString(R.string.user_node))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User user = dataSnapshot.getValue(User.class);

                        dbRef.child(getString(R.string.family_node))
                                .child(user.getFamily_id())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Family family = dataSnapshot.getValue(Family.class);
                                        final int highestLifetimePoint = family.getHighestLifetimePoint();
                                        Query ref = dbRef.child(getString(R.string.family_member))
                                                .child(user.getFamily_id())
                                                .orderByChild(getString(R.string.user_point_lifetime_node))
                                                .getRef();

                                        final FirebaseRecyclerAdapter fireAdapter = new FirebaseRecyclerAdapter<FamilyMember, RankingHolder>(
                                                FamilyMember.class,
                                                R.layout.ranking_item,
                                                RankingHolder.class,
                                                ref
                                        ) {
                                            @Override
                                            protected void populateViewHolder(final RankingHolder viewHolder, final FamilyMember familyMember, int position) {
                                                StorageReference shopImageRef = storageRef.child(familyMember.getAvatar_link());
                                                shopImageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                    @Override
                                                    public void onSuccess(final byte[] bytes) {
                                                        // Use the bytes to display the image
                                                        Log.d("databaru", "Download Success");
                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                        viewHolder.member_avatar.setImageBitmap(bitmap);
//                        loadImageThumbnail(false, viewHolder.product_image, viewHolder.l);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        e.printStackTrace();
//                        errorToast.show();
                                                    }
                                                });

                                                viewHolder.member_point_text.setText(familyMember.getUser_point_lifetime()+" pts");
                                                viewHolder.member_name.setText(familyMember.getName());
                                                int memberPoint = familyMember.getUser_point_lifetime();
                                                int weight = (memberPoint  * 10)/highestLifetimePoint;
                                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0,75);
                                                param.gravity = Gravity.CENTER_VERTICAL;
                                                param.weight = weight;
                                                viewHolder.member_point_bar.setLayoutParams(param);
                                            }
                                        };
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                                        ranking_recyclerview.setLayoutManager(linearLayoutManager);
                                        ranking_recyclerview.setAdapter(fireAdapter);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
    }

}
