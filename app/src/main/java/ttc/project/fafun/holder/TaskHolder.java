package ttc.project.fafun.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ttc.project.fafun.R;

/**
 * Created by Fikry-PC on 10/18/2017.
 */

public class TaskHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.task_category_label) public View task_category_label;
    @BindView(R.id.task_name) public TextView task_name;
    @BindView(R.id.task_reward) public TextView task_reward;
    @BindView(R.id.task_checkbox)
    public CheckBox task_checkbox;
    public TaskHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
