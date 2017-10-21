package ttc.project.fafun.service;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import timber.log.Timber;
import ttc.project.fafun.R;
import ttc.project.fafun.model.Task;

/**
 * Created by Fikry-PC on 10/21/2017.
 */

public class DailyCleanupJobService extends JobService {

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    @Override
    public boolean onStartJob(final JobParameters job) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dailyTaskCleanup(job);
            }
        }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    private void dailyTaskCleanup(final JobParameters parameters){
        try {
            Timber.d("DailyTaskCleanup is started...");

            //This task takes 2 seconds to complete.
            Thread.sleep(2000);
            resetCompletedDailyTask();
            Timber.d("DailyTaskCleanup is finished...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //Tell the framework that the job has completed and doesnot needs to be reschedule
            jobFinished(parameters, true);
        }
    }

    private void resetCompletedDailyTask(){
        dbRef.child(getString(R.string.task_node))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Task task = dataSnapshot.getValue(Task.class);

                        if(task.getPeriod() ==
                                getResources().getInteger(R.integer.period_daily)){
                            Long last_checked_timemillis = task.getLast_checked_timemillis();

                            Calendar cData = Calendar.getInstance();
                            cData.setTimeInMillis(last_checked_timemillis);
                            int dayData = cData.get(Calendar.DATE);

                            Calendar cNow = Calendar.getInstance();
                            int dayNow = cNow.get(Calendar.DATE);

                            if(dayNow - dayData == 1){
                                Timber.d("Day passed.");
                                task.setLast_checked_timemillis(System.currentTimeMillis());
                                Timber.d("Set new checkPoint :"+System.currentTimeMillis());
                                task.setCompleted(false);
                                dataSnapshot.getRef().setValue(task);
                            } else{
                                Timber.d("Day not passed.");
                            }
                        } else if(task.getPeriod() ==
                                getResources().getInteger(R.integer.period_once)){
                            dataSnapshot.getRef().removeValue();
                        } else if(task.getPeriod() ==
                                getResources().getInteger(R.integer.period_weekly)){
                            Long last_checked_timemillis = task.getLast_checked_timemillis();

                            Calendar cData = Calendar.getInstance();
                            cData.setTimeInMillis(last_checked_timemillis);
                            int dayData = cData.get(Calendar.DATE);

                            Calendar cNow = Calendar.getInstance();
                            int dayNow = cNow.get(Calendar.DATE);

                            if(dayNow - dayData == 7){
                                Timber.d("Week passed.");
                                task.setLast_checked_timemillis(System.currentTimeMillis());
                                Timber.d("Set new checkPoint :"+System.currentTimeMillis());
                                task.setCompleted(false);
                                dataSnapshot.getRef().setValue(task);
                            } else{
                                Timber.d("Week not passed.");
                            }

                        } else if(task.getPeriod() ==
                                getResources().getInteger(R.integer.period_monthly)){
                            Long last_checked_timemillis = task.getLast_checked_timemillis();

                            Calendar cData = Calendar.getInstance();
                            cData.setTimeInMillis(last_checked_timemillis);
                            int dayData = cData.get(Calendar.DATE);

                            Calendar cNow = Calendar.getInstance();
                            int dayNow = cNow.get(Calendar.DATE);

                            if(dayNow - dayData == 30){
                                Timber.d("Month passed.");
                                task.setLast_checked_timemillis(System.currentTimeMillis());
                                Timber.d("Set new checkPoint :"+System.currentTimeMillis());
                                task.setCompleted(false);
                                dataSnapshot.getRef().setValue(task);
                            } else{
                                Timber.d("Month not passed.");
                            }

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
