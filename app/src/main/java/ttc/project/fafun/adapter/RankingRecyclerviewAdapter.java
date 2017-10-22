package ttc.project.fafun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

import ttc.project.fafun.R;
import ttc.project.fafun.holder.RankingHolder;

/**
 * Created by Fikry-PC on 10/21/2017.
 */

public class RankingRecyclerviewAdapter extends RecyclerView.Adapter<RankingHolder> {
    Context mContext;


    public RankingRecyclerviewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RankingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ranking_item,
                parent,
                false);
        return new RankingHolder(view);
    }

    @Override
    public void onBindViewHolder(RankingHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
