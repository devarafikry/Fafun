package ttc.project.fafun;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import timber.log.Timber;
import ttc.project.fafun.activity.AddTaskActivity;
import ttc.project.fafun.model.Family;
import ttc.project.fafun.model.FamilyMember;
import ttc.project.fafun.model.Task;
import ttc.project.fafun.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    @BindView(R.id.member_recyclerview)
    RecyclerView member_recyclerview;
    @BindView(R.id.member_task_name)
    TextView member_task_name;
    @BindView(R.id.add_task)
    FloatingActionButton fab_add_task;
    @BindView(R.id.task_recyclerview) RecyclerView task_recyclerview;

    FirebaseRecyclerAdapter fireAdapter;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, view);
        fab_add_task.setVisibility(View.INVISIBLE);
        Timber.d("Path :"+ dbRef.child(getString(R.string.user_node)).child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ).toString());
        Timber.d("userid: "+FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbRef.child(getString(R.string.user_node))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Query ref = dbRef.child(getString(R.string.family_member))
                                .child(user.getFamily_id());


                        dbRef.child(getString(R.string.family_member))
                                .child(user.getFamily_id())
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final FamilyMember member = dataSnapshot.getValue(FamilyMember.class);
                                        member_task_name.setText("Tugas "+member.getName());

                                        Query ref = dbRef.child(getString(R.string.task_node))
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        Timber.d(ref.toString());

                                        fireAdapter = new FirebaseRecyclerAdapter<Task, TaskHolder>(
                                                Task.class,
                                                R.layout.task_item,
                                                TaskHolder.class,
                                                ref
                                        ) {

                                            @Override
                                            protected void populateViewHolder(TaskHolder viewHolder, Task model, int position) {
                                                viewHolder.task_name.setText(model.getTask_name());
                                                viewHolder.task_reward.setText(String.valueOf(model.getTask_reward_point()));
                                                viewHolder.task_category_label.setBackgroundColor(
                                                        getResources().getColor(CategoryHelper.getCategoryColor(model.getCategory()))
                                                );
                                            }
                                        };
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                                        task_recyclerview.setLayoutManager(linearLayoutManager);
                                        task_recyclerview.setAdapter(fireAdapter);
                                        fab_add_task.setVisibility(View.VISIBLE);

                                        fab_add_task.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                                                intent.putExtra(AddTaskActivity.SELECTED_USER_ID, member.getUser_id());
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                        final FirebaseRecyclerAdapter fireAdapter = new FirebaseRecyclerAdapter<FamilyMember, MemberListHolder>(
                                FamilyMember.class,
                                R.layout.member_item,
                                MemberListHolder.class,
                                ref
                        ) {
                            @Override
                            protected void populateViewHolder(final MemberListHolder viewHolder, final FamilyMember model, int position) {

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

                                viewHolder.member_avatar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        member_task_name.setText("Tugas "+model.getName());

                                        fab_add_task.setVisibility(View.VISIBLE);
                                        //test
                                        Query ref = dbRef.child(getString(R.string.task_node))
                                                .child(model.getUser_id());

                                        FirebaseRecyclerAdapter newAdapter = new FirebaseRecyclerAdapter<Task, TaskHolder>(
                                                Task.class,
                                                R.layout.task_item,
                                                TaskHolder.class,
                                                ref
                                        ) {

                                            @Override
                                            protected void populateViewHolder(TaskHolder viewHolder, Task model, int position) {
                                                viewHolder.task_name.setText(model.getTask_name());
                                                viewHolder.task_reward.setText(String.valueOf(model.getTask_reward_point()));
                                                viewHolder.task_category_label.setBackgroundColor(
                                                        getResources().getColor(CategoryHelper.getCategoryColor(model.getCategory()))
                                                );
                                            }
                                        };
                                        Timber.d(ref.toString());
                                        task_recyclerview.swapAdapter(newAdapter, true);


                                        //test

                                        fab_add_task.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                                                intent.putExtra(AddTaskActivity.SELECTED_USER_ID, model.getUser_id());
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }
                        };
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
                        member_recyclerview.setLayoutManager(linearLayoutManager);
                        member_recyclerview.setAdapter(fireAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return view;
    }

}
