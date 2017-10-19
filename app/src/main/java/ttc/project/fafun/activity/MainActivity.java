package ttc.project.fafun.activity;

import android.graphics.Color;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;
import timber.log.Timber;
import ttc.project.fafun.FafunPagerAdapter;
import ttc.project.fafun.FamilyFragment;
import ttc.project.fafun.R;
import ttc.project.fafun.TaskFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        Timber.plant(new Timber.DebugTree());
    }

    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        FafunPagerAdapter adapter = new FafunPagerAdapter(this, getSupportFragmentManager());
        adapter.addFrag(new FamilyFragment(), "Fam");
        adapter.addFrag(new TaskFragment(), "Fam");
        adapter.addFrag(new FamilyFragment(), "Fam");
        adapter.addFrag(new FamilyFragment(), "Fam");
        viewPager.setAdapter(adapter);

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.family),
                        getResources().getColor(R.color.colorAccent))
//                        .selectedIcon(getResources().getDrawable(R.drawable.family))
                        .title("Keluarga")
//                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.task),
                        getResources().getColor(R.color.colorAccent))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Tugas")
//                        .badgeTitle("icon")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_seventh),
                        getResources().getColor(R.color.colorAccent))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Peringkat")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fifth),
                        getResources().getColor(R.color.colorAccent))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Hadiah")
//                        .badgeTitle("777")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 1);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }
}
