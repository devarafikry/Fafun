package ttc.project.fafun.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ttc.project.fafun.activity.AddFamilyMemberActivity;

/**
 * Created by Fikry-PC on 10/13/2017.
 */

public class FafunPagerAdapter extends FragmentPagerAdapter {
    Context mContext;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mFragmentTitleList = new ArrayList<>();


    public FafunPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public void destroyItem(final View container, final int position, final Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

//    @Override
//    public Object instantiateItem(final ViewGroup container, final int position) {
//
////                final TextView txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
////                txtPage.setText(String.format("Page #%d", position));
//        View view = null;
//        switch (position){
//            case 0:
//                view = LayoutInflater.from(
//                        mContext).inflate(R.layout.fragment_family, null, false);
//
//                FloatingActionButton btn_add_family = view.findViewById(R.id.add_family);
//                btn_add_family.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(mContext, AddFamilyMemberActivity.class);
//                        mContext.startActivity(intent);
//                    }
//                });
//                break;
//        }
//        container.addView(view);
//        return view;
//    }

}
