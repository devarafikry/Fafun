package ttc.project.fafun.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import ttc.project.fafun.holder.FamilyMemberHolder;
import ttc.project.fafun.R;
import ttc.project.fafun.activity.AddFamilyMemberActivity;
import ttc.project.fafun.model.FamilyMember;
import ttc.project.fafun.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {

    @BindView(R.id.add_family)
    FloatingActionButton btn_add_family;
    @BindView(R.id.family_recyclerview)
    RecyclerView family_recyclerview;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_family, container, false);
        ButterKnife.bind(this, view);
        btn_add_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddFamilyMemberActivity.class);
                startActivity(intent);
            }
        });
        dbRef.child(getString(R.string.user_node))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Query ref = dbRef.child(getString(R.string.family_member))
                                .child(user.getFamily_id());

                        FirebaseRecyclerAdapter fireAdapter = new FirebaseRecyclerAdapter<FamilyMember, FamilyMemberHolder>(
                                FamilyMember.class,
                                R.layout.family_member_item,
                                FamilyMemberHolder.class,
                                ref
                        ) {
                            @Override
                            protected void populateViewHolder(final FamilyMemberHolder viewHolder, FamilyMember model, int position) {
                                viewHolder.member_name.setText(model.getName());

                                StorageReference shopImageRef = storageRef.child(model.getAvatar_link());
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
                            }
                        };
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                        family_recyclerview.setLayoutManager(gridLayoutManager);
                        family_recyclerview.setAdapter(fireAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return view;
    }

}
