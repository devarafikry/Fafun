package ttc.project.fafun.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.fafun.R;
import ttc.project.fafun.activity.AddRewardActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoPickOptionFragment extends DialogFragment {

    final static int TAKE_PHOTO_CONSTANT =1;
    final static int PICK_PHOTO_CONSTANT =2;
    final static int SELECT_ACTION_CONSTANT =0;
    final static String key = "action_selected";

    @BindView(R.id.take_photo)
    ImageView take_photo;
    @BindView(R.id.pick_photo)
    ImageView pick_photo;
    public PhotoPickOptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_pick_option, container, false);
        ButterKnife.bind(this,view);


        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(TAKE_PHOTO_CONSTANT);
            }
        });
        pick_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(PICK_PHOTO_CONSTANT);
            }
        });

        return view;
    }

    private void setResult(int i){
        Bundle bundle = new Bundle();
        bundle.putInt(key, i);

        Intent intent = new Intent().putExtras(bundle);
        if(getTargetFragment() != null){
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        } else{
            if(getActivity() instanceof AddRewardActivity){
                ((AddRewardActivity) getActivity()).launchImageCreator(i);
            }
        }

        dismiss();
    }

}
