package ttc.project.fafun.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.fafun.R;
import ttc.project.fafun.SnackbarUtils;
import ttc.project.fafun.model.Task;

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.task_name)
    TextView task_name;
    @BindView(R.id.task_point) TextView task_point;

    @BindView(R.id.task_daily)
    RadioButton rb_task_daily;
    @BindView(R.id.task_weekly)
    RadioButton rb_task_weekly;
    @BindView(R.id.task_monthly)
    RadioButton rb_task_monthly;
    @BindView(R.id.task_once)
    RadioButton rb_task_once;

    @BindView(R.id.task_home)
    RadioButton rb_task_home;
    @BindView(R.id.task_academic)
    RadioButton rb_task_academic;
    @BindView(R.id.task_accomplishment)
    RadioButton rb_task_accomplishment;
    @BindView(R.id.task_manner)
    RadioButton rb_task_manner;
    @BindView(R.id.task_behaviour)
    RadioButton rb_task_behaviour;

    @BindView(R.id.radio_group_category)
    RadioGroup rbg_category;
    @BindView(R.id.radio_group_period) RadioGroup rbg_period;

    @BindView(R.id.rootView)
    View rootView;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    Snackbar s;
    String selectedUserId;
    public static String SELECTED_USER_ID = "selectedUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        selectedUserId = getIntent().getStringExtra(SELECTED_USER_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            submitTask();
        }
        return true;
    }

    private void submitTask(){
        if(TextUtils.isEmpty(task_name.getText())
                || TextUtils.isEmpty(task_point.getText())){
            SnackbarUtils.showSnackbar(rootView, s, "Mohon isi data tugas.", Snackbar.LENGTH_LONG);
            return;
        }

        String name = task_name.getText().toString();
        int point = Integer.valueOf(task_point.getText().toString());

        int selectedCategory = rbg_category.indexOfChild(findViewById(rbg_category.getCheckedRadioButtonId()));
        int selectedPeriod = rbg_period.indexOfChild(findViewById(rbg_period.getCheckedRadioButtonId()));

        Task task = new Task(
                name,
                selectedPeriod,
                point,
                selectedCategory
        );
        dbRef.child(getString(R.string.task_node))
                .child(selectedUserId)
                .push()
                .setValue(task);
    }
}
