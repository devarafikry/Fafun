package ttc.project.fafun.fragment;

/**
 * Created by Kamil on 10/22/17.
 */

import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import ttc.project.fafun.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadingDataFragment extends DialogFragment {


    public UploadingDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.fragment_uploading_data, container, false);
    }

}
