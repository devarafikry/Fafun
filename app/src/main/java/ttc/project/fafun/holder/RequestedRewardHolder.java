package ttc.project.fafun.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.fafun.R;

/**
 * Created by Fikry-PC on 10/18/2017.
 */

public class RequestedRewardHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.btn_complete_reward) public Button btn_complete_reward;
    @BindView(R.id.reward_name) public TextView reward_name;
    @BindView(R.id.reward_point) public TextView reward_cost_point;
    @BindView(R.id.reward_date) public TextView reward_date;
    @BindView(R.id.reward_image)
    public ImageView reward_image;
    public RequestedRewardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
