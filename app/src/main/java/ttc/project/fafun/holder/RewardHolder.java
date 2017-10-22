package ttc.project.fafun.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.fafun.R;

/**
 * Created by Fikry-PC on 10/18/2017.
 */

public class RewardHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.btn_claim_reward) public Button btn_claim_reward;
    @BindView(R.id.btn_claim_reward_not_enough) public Button btn_claim_reward_not_enough;
    @BindView(R.id.reward_name) public TextView reward_name;
    @BindView(R.id.reward_point) public TextView reward_cost_point;
    @BindView(R.id.btn_delete_reward) public Button btn_delete_reward;
    @BindView(R.id.reward_image)
    public ImageView reward_image;
    public RewardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
