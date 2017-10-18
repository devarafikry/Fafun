package ttc.project.fafun;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import ttc.project.fafun.activity.AddFamilyMemberActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {

    @BindView(R.id.add_family)
    FloatingActionButton btn_add_family;
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
        return view;
    }

}
