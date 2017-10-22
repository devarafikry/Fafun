package ttc.project.fafun.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;
import timber.log.Timber;
import ttc.project.fafun.fragment.RankingFragment;
import ttc.project.fafun.fragment.RewardFragment;
import ttc.project.fafun.service.DailyCleanupJobService;
import ttc.project.fafun.adapter.FafunPagerAdapter;
import ttc.project.fafun.fragment.FamilyFragment;
import ttc.project.fafun.R;
import ttc.project.fafun.fragment.TaskFragment;

public class MainActivity extends AppCompatActivity {

    int DAILY_INTERVAL_CHECK = 3600;
    //cek setiap 1 jam
    int DAILY_INTERVAL_FLEXTIME = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Tugas");
        initUI();
        Timber.plant(new Timber.DebugTree());

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        //creating new job and adding it with dispatcher
        Job job = createJob(dispatcher);
        dispatcher.mustSchedule(job);
    }

    private Job createJob(FirebaseJobDispatcher dispatcher){
        Job job = dispatcher.newJobBuilder()
                //persist the task across boots
                .setLifetime(Lifetime.FOREVER)
                //.setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                //call this service when the criteria are met.
                .setService(DailyCleanupJobService.class)
                //unique id of the task
                .setTag("UniqueTagForYourJob")
                //don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // We are mentioning that the job is periodic.
                .setRecurring(true)
                // Run between 30 - 60 seconds from now.
                .setTrigger(Trigger.executionWindow(DAILY_INTERVAL_CHECK, DAILY_INTERVAL_CHECK+DAILY_INTERVAL_FLEXTIME))
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                //.setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                //Run this job only when the network is available.
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        return job;
    }

    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        FafunPagerAdapter adapter = new FafunPagerAdapter(this, getSupportFragmentManager());
        adapter.addFrag(new FamilyFragment(), "Fam");
        adapter.addFrag(new TaskFragment(), "Fam");
        adapter.addFrag(new RankingFragment(), "Fam");
        adapter.addFrag(new RewardFragment(), "Fam");
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
                switch (position){
                    case 0:
                        getSupportActionBar().setTitle("Keluarga");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Tugas");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Peringkat");
                        break;
                    case 3:
                        getSupportActionBar().setTitle("Hadiah");
                        break;
                }
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
