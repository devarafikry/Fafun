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

public class RankingHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.member_point_bar) public View member_point_bar;
    @BindView(R.id.member_point_text) public TextView member_point_text;
    @BindView(R.id.member_avatar) public CircleImageView member_avatar;
    @BindView(R.id.member_name) public TextView member_name;

    public RankingHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
